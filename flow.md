flow.md

# Transaction Monitoring & Risk Management — Flow Documentation

This document describes the developer flow, application request flow, data flow, domain models, repository responsibilities, and security customizations (JWT + session management) for this project.

---

Quick plan (what this doc provides):
- Dev Flow: how to run, test, branch, and iterate.
- App Flow: request lifecycle (controller → service → repo), error handling, transactions.
- Data Flow: how data moves, event boundaries, audit & retention.
- Models: entities with key fields and invariants.
- Repositories: responsibilities and common method signatures.
- Security: recommended JWT scheme, refresh tokens, session management options, and hardening tips.
- Examples: sequence + sample JWT payloads and administrative review flow.

---

Table of contents
- Dev Flow
- App Flow
- Data Flow
- Models (entities)
- Repos (interfaces & contracts)
- Security customization: JWT + Session management
- Example sequences and sample payloads
- Operational notes & next steps

---

Dev Flow
--------
1. Branching / PRs
   - Use feature branches: `feature/<short-desc>`, `hotfix/<id>`, `chore/<desc>`.
   - Open PRs against `main` (or `develop` if you use Git Flow). Include a checklist: build, unit tests, basic manual scenario.

2. Local run
   - Build: `./mvnw clean package` (Windows: `mvnw.cmd clean package`).
   - Run: `./mvnw spring-boot:run` (or run from IDE).
   - By default the repo contains H2 as development DB (pom). Configure `application.properties` to switch DBs for staging/prod.

3. Tests
   - Unit tests: JUnit + Spring Boot Test (already present). Add happy-path + at least one edge-case test per service.
   - Integration tests: use an in-memory DB (H2) or testcontainers for DB parity with prod.

4. Developer checklist for changes
   - Implement business logic in `service/` classes (thin controllers only).
   - Mark money-movement methods with `@Transactional` at service boundary.
   - Add unit tests for rule evaluation and transactional behavior.
   - Add integration test for a transfer scenario (debit/credit + audit record).


App Flow (request lifecycle)
---------------------------
High-level path for an API request (example: transfer request):

1. HTTP client → Controller (`controller.TransactionController`)
   - Controller validates request DTOs, checks authentication/authorization via annotations (e.g., `@PreAuthorize("hasRole('USER')")`).
2. Controller calls Service (`service.TransactionService`)
   - Service performs domain validation (accounts exist, status active, sufficient balance).
   - Service calls RiskEvaluator (component) to compute a risk score.
3. Decision by RiskEvaluator
   - LOW: proceed with money movement.
   - MEDIUM: save transaction as FLAGGED and create a TransactionReview entry for admin approval (no money moved yet).
   - HIGH: save transaction as BLOCKED, return 403 or domain-specific code to caller.
4. Money movement (if LOW)
   - Performed inside a single `@Transactional`
   - Steps: debit source account, credit destination account, save Transaction entity with final status.
   - Emit an audit record (separate table) and optionally publish an event (for async processing).
5. Response
   - Controller returns DTO with transaction id, status, riskScore and metadata.

Error handling / retries
- Failures in the `@Transactional` block should throw runtime exceptions to trigger rollback.
- For idempotency, use a client-supplied idempotency key header or dedup via unique constraint on `clientRequestId`.


Data Flow
---------
- Source of truth: relational DB (JPA/Hibernate).
- Eventual async flows: publishing domain events after commit (use TransactionSynchronizationManager or post-commit events).
- Audit: all Transaction attempts (successful or flagged) are stored permanently with timestamp, actor, and decision history.
- Retention: audit retention policies should be configured separately (DB partitioning or archival jobs).

Example data movement: User initiates transfer → `Transaction` row (PENDING/FLAGGED) → if approved, amounts updated in `Account` rows and `Transaction` updated to SUCCESS.


Models (entities)
------------------
Below are recommended entities and core fields. Use JPA entities with appropriate indexes and constraints.

1. User
- id: Long
- username: String (unique)
- passwordHash: String
- roles: Set<Role> (ROLE_USER, ROLE_ADMIN)
- enabled: boolean
- createdAt, updatedAt

2. Account
- id: Long
- ownerUserId: Long (FK)
- balance: BigDecimal (scale/precision configured)
- currency: String
- status: ENUM { ACTIVE, BLOCKED }
- createdAt, updatedAt
- Optimistic locking: @Version field recommended

3. Transaction
- id: Long
- clientRequestId: String (nullable, for idempotency)
- fromAccountId: Long
- toAccountId: Long
- amount: BigDecimal
- currency: String
- riskScore: Integer or Decimal
- status: ENUM { PENDING, SUCCESS, FLAGGED, BLOCKED, REJECTED }
- initiatedBy: Long (user id)
- createdAt, updatedAt
- comment/adminDecision: String

4. RiskRule
- id: Long
- name: String
- description: String
- type: ENUM (AMOUNT_THRESHOLD, FREQUENCY, GEO, CUSTOM)
- parameters: JSON/text (e.g., threshold: 10000)
- active: boolean

5. TransactionReview
- id: Long
- transactionId: Long (FK)
- reviewerId: Long
- decision: ENUM { APPROVED, REJECTED }
- comments: String
- reviewedAt: timestamp

6. AuditLog (optional separate table)
- id, actorId, actionType, payload (JSON), timestamp


Repositories (contracts)
-------------------------
- `UserRepository extends JpaRepository<User, Long>`
  - findByUsername(String username)
- `AccountRepository extends JpaRepository<Account, Long>`
  - findByOwnerUserId(Long userId)
  - debit/credit via custom @Modifying queries (but prefer loading and saving inside transaction for consistency)
- `TransactionRepository extends JpaRepository<Transaction, Long>`
  - findTopByFromAccountIdOrderByCreatedAtDesc(...) for history-based checks
  - findByClientRequestId(...) for idempotency
- `RiskRuleRepository extends JpaRepository<RiskRule, Long>`

Design notes:
- Keep repo methods narrowly focused. Business rules should stay out of repositories.
- Prefer query methods that support paging for audit/history endpoints.


Security customization: JWT + session management
-----------------------------------------------
Goals:
- Authenticate users with username/password (bcrypt hashed) and issue JWTs.
- Short-lived access JWTs (e.g., 5–15 minutes) + long-lived secure refresh token (7–30 days).
- Protect admin endpoints with role checks.

Suggested components to implement:
1. `security.JwtTokenProvider`
   - generateAccessToken(Authentication/Claims)
   - generateRefreshToken(subject, jti)
   - validateToken(token)
   - parseClaims(token)
   - secret in application properties or external vault

2. `security.JwtFilter` / `OncePerRequestFilter`
   - Extract `Authorization: Bearer <token>` header, validate, and set SecurityContext

3. `security.RefreshTokenService`
   - Store refresh tokens securely (DB table `refresh_tokens` with token hash, userId, expiry, revoked flag).
   - Issue refresh token on login as an HTTP-only Secure SameSite cookie (recommended), and return access token in response (or both in cookie depending on front-end).

4. Token revocation / logout
   - For stateless access tokens, maintain a blacklist for revoked tokens (if you need immediate revocation) or rely on short lifetimes.
   - For refresh tokens, store and mark revoked in DB to prevent reuse.

5. Session management options
   - Stateless (recommended): rely on JWTs only. Advantages: scalable, no server-side session store. Tradeoffs: hard to revoke access tokens immediately.
   - Stateful (hybrid): store sessions or refresh tokens server-side. Advantages: easy to revoke, manageable security. Tradeoffs: requires cache/DB (Redis) for session store.

6. CSRF and Cookie handling
   - If you store tokens in cookies, protect cookies with HttpOnly, Secure, SameSite=Strict/Lax, and use CSRF tokens for state-changing requests when necessary.
   - If using Authorization header (Bearer token), CSRF is less of a concern for APIs used by SPAs, but still consider best practices for CORS and XSS protection.

7. Role-based authorization
   - Annotate controllers or service methods: `@PreAuthorize("hasRole('ADMIN')")` for admin endpoints.

8. Password hashing
   - Use BCryptPasswordEncoder (strength >= 10). Never store raw passwords.

9. Example JWT payload (claims):
{
  "sub": "user-123",
  "roles": ["ROLE_USER"],
  "iat": 1610000000,
  "exp": 1610003600,
  "jti": "uuid-v4"
}

Practical advice for implementation
- Keep access token short-lived and rely on refresh token workflow.
- Persist refresh tokens hashed (not plaintext) to protect DB leaks.
- On refresh rotate refresh tokens (issue new refresh token and revoke old one).
- Protect sensitive endpoints (admin reviews) behind `ROLE_ADMIN` checks and audit reviewer id and comments in `TransactionReview`.


Example: transfer + flagged review sequence (ASCII)
--------------------------------------------------
Client -> POST /api/transactions -> Controller -> Service -> RiskEvaluator -> DB:Transaction(PENDING/FLAGGED)
If FLAGGED -> create TransactionReview record (status WAITING)
Admin -> POST /api/transactions/{id}/review -> Controller -> Service -> if APPROVED -> perform money movement in @Transactional and update Transaction to SUCCESS; else mark REJECTED and do not move funds.


Operational notes & next steps
-----------------------------
- Monitoring: instrument critical flows (failed transfers, blocked transactions) with metrics and alerts.
- Observability: log correlation id and user id on all logs. Consider structured logging (JSON).
- Security: periodic rotation of JWT secret and review of refresh-token lifecycle.
- Performance: use optimistic locking on Account entity, and consider serialized transfer queue only if concurrency issues appear.


Appendix: Recommended DB indexes
- Transaction(createdAt)
- Transaction(clientRequestId) unique (if used)
- Account(ownerUserId)
- RefreshToken(userId, revoked)


Contact / ownership
- Keep this file updated as domain changes. Use it as a contract between backend, frontend, and operations.

---

End of flow.md
