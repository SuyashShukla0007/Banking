package com.example.bank.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
@Entity
@Table(name = "account_approvals")
public class AccountApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user; // requester

    @Column(nullable = false)
    private String accountType; // SAVINGS, CURRENT

    @Column(nullable = false)
    private String status; // PENDING, APPROVED, REJECTED

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime reviewedAt;
}

    @Transactional
    public User createUser(UserRegisterRequest request) {
        // ...existing code...
    }

    public Account createAccount(AccountCreation accountCreation, Long userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accountNumber = new GenerateAccountNumber().generate();

        // Extract last 4 digits
        String last4 = accountNumber.substring(Math.max(0, accountNumber.length() - 4));

        Account account = new Account();
        account.setAccountType(accountCreation.getAccountType());
        account.setBalance(0.0);
        account.setAccountNumber(accountNumber);
        account.setLast4AccountNumber(Long.parseLong(last4));
        account.setStatus("PENDING"); // or "ACTIVE" based on your flow
        account.setUser(user);
        // account.setBranch() - needs to be set based on
        // accountCreation.getBranchName()

        return accountRepo.save(account);
    }

    package com.example.bank.exception;

    public class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
}

package com.example.bank.DTO.Account
;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSensitive {
    private String accountNumber;
    private long balance;
    private String accountType;
    private String branchName;
}

package com.example.bank.utility;

import org.springframework.stereotype.Component;

@Component
public class GenerateAccountNumber {
    public String generate() {
        return "PNB" + System.currentTimeMillis();
    }

}

@DeleteMapping("/{id}")
public String delete(@PathVariable Long id) {
    try {
        userService.deleteUser(id);
        return "user deleted successfully";
    } catch (RuntimeException e) {
        throw new RuntimeException(e);
    }
}
