# Bank API - Quick Reference

## 📡 All Endpoints Overview

### Base URL: `http://localhost:8080`

---

## 1️⃣ Admin Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/admin/create` | Create admin account |
| POST | `/admin/login` | Admin login |
| GET | `/admin/{id}` | Get admin by ID |
| PUT | `/admin/{id}` | Update admin profile |
| DELETE | `/admin/{id}` | Delete admin |
| POST | `/admin/change-password` | Change admin password |
| GET | `/admin/report` | Get system report |

---

## 2️⃣ User Registration & Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/users/create` | Create user registration request |
| POST | `/users/login` | User login |
| GET | `/users/delete-user/{id}` | Delete user |

---

## 3️⃣ User Approval Management (Admin)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/user-approvals/pending` | Get all pending user approvals |
| GET | `/api/admin/user-approvals/{id}` | Get approval by ID |
| GET | `/api/admin/user-approvals/by-email?email=` | Get approval by email |
| POST | `/api/admin/user-approvals/{id}/approve?adminId=` | Approve user registration |
| POST | `/api/admin/user-approvals/{id}/reject?adminId=&reason=` | Reject user registration |

---

## 4️⃣ User Profile Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| PUT | `/users/{id}/profile?name=&email=` | Update user profile |
| POST | `/users/{id}/change-password?oldPassword=&newPassword=` | Change password |
| POST | `/users/{id}/deactivate` | Deactivate user account |

---

## 5️⃣ Admin User Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/users/all` | Get all users |
| GET | `/api/admin/users/{id}` | Get user by ID |
| POST | `/api/admin/users/{id}/suspend` | Suspend user |
| POST | `/api/admin/users/{id}/activate` | Activate user |
| GET | `/api/admin/users/{id}/accounts` | Get user's accounts |

---

## 6️⃣ Branch Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/branches/create` | Create branch |
| GET | `/branches/all` | Get all branches |
| GET | `/branches/{id}` | Get branch by ID |
| PUT | `/branches/{id}` | Update branch |
| DELETE | `/branches/{id}` | Delete branch |

---

## 7️⃣ Account Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/accounts/create` | Create account request |
| GET | `/accounts/all` | Get all accounts |
| GET | `/accounts/{id}` | Get account by ID |
| GET | `/accounts/user/{userId}` | Get user's accounts |
| GET | `/accounts/{id}/balance` | Get account balance |

---

## 8️⃣ Account Approval Management (Admin)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/account-approvals/pending` | Get pending account approvals |
| GET | `/account-approvals/{id}` | Get account approval by ID |
| POST | `/account-approvals/{id}/approve?adminId=` | Approve account creation |
| POST | `/account-approvals/{id}/reject?adminId=&reason=` | Reject account creation |

---

## 9️⃣ Account Status Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| PUT | `/account-status/{id}/activate` | Activate account |
| PUT | `/account-status/{id}/suspend` | Suspend account |
| PUT | `/account-status/{id}/close` | Close account |

---

## 🔟 Transaction Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/transactions/deposit?accountId=&amount=` | Deposit money |
| POST | `/transactions/withdraw?accountId=&amount=` | Withdraw money |
| POST | `/transactions/transfer?fromAccountId=&toAccountId=&amount=` | Transfer money |
| GET | `/transactions/{id}` | Get transaction by ID |
| GET | `/transactions/{accountId}/history` | Get account transaction history |
| GET | `/transactions/all` | Get all transactions |

---

## 📊 Request Body Examples

### Create Admin
```json
{
  "name": "Admin User",
  "email": "admin@bank.com",
  "password": "admin123"
}
```

### Create User Registration
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

### User Login
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

### Create Branch
```json
{
  "branchName": "Downtown Branch",
  "branchAddress": "123 Main Street",
  "branchContactNumber": "555-0100"
}
```

### Create Account
```json
{
  "userId": 1,
  "accountType": "SAVINGS",
  "branchId": 1
}
```

---

## 🎯 Common Query Parameters

| Parameter | Used In | Example |
|-----------|---------|---------|
| `adminId` | Approval endpoints | `?adminId=1` |
| `accountId` | Transaction endpoints | `?accountId=1` |
| `amount` | Transaction endpoints | `?amount=1000` |
| `email` | User queries | `?email=user@example.com` |
| `reason` | Rejection endpoints | `?reason=Insufficient docs` |
| `name` | Profile update | `?name=John Doe` |
| `oldPassword` | Password change | `?oldPassword=old123` |
| `newPassword` | Password change | `?newPassword=new456` |

---

## 🔐 Account Types

- `SAVINGS` - Savings account
- `CHECKING` - Checking account

---

## 📍 Status Values

### User Status
- `ACTIVE` - User can login and perform operations
- `SUSPENDED` - User temporarily blocked by admin
- `DEACTIVATED` - User chose to deactivate account
- `INACTIVE` - User marked inactive

### Account Status
- `ACTIVE` - Account can transact
- `SUSPENDED` - Account temporarily blocked
- `INACTIVE` - Account deactivated
- `CLOSED` - Account permanently closed

### Transaction Types
- `DEPOSIT` - Money added to account
- `WITHDRAWAL` - Money removed from account
- `TRANSFER` - Money moved between accounts

---

## ⚠️ Important Notes

1. **User Registration**: Requires admin approval before user can login
2. **Account Creation**: Requires admin approval before account is active
3. **Passwords**: Automatically encrypted using BCrypt
4. **Balance**: Cannot go negative (withdrawals/transfers will fail)
5. **Suspended Accounts**: Cannot perform any transactions
6. **User Suspension**: Automatically suspends all user's accounts
7. **Deactivation**: Sets user and all accounts to inactive status

---

## 🚀 Typical Workflow

```
1. Create Admin
2. Admin Login
3. User Creates Registration Request
4. Admin Approves User
5. User Login
6. Create Branch (if not exists)
7. User Requests Account
8. Admin Approves Account
9. User Deposits Money
10. User Performs Transactions
```

---

## 📱 Response Codes

| Code | Meaning |
|------|---------|
| 200 | Success |
| 400 | Bad Request (validation error) |
| 404 | Not Found |
| 500 | Server Error |

---

## 🔍 Search & Filter Tips

- Use `/all` endpoints to get lists
- Filter by `userId` for user-specific data
- Check `/pending` for approval workflows
- Use transaction history for account audits

---

**Print this reference card for quick access during development! 📋**
