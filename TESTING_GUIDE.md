# Bank API - End-to-End Testing Guide

## 📦 Setup

### 1. Import Postman Collection
1. Open Postman
2. Click **Import** button
3. Select `Bank_API_Complete_Testing.postman_collection.json`
4. Collection will appear in your workspace

### 2. Configure Environment Variables
The collection uses these variables (already pre-configured):
- `base_url`: `http://localhost:8080`
- `admin_id`: `1`
- `user_id`: `1`
- `branch_id`: `1`
- `account_id`: `1`
- `account_id_2`: `2`
- `transaction_id`: `1`
- `approval_id`: `1`
- `approval_id_2`: `2`
- `account_approval_id`: `1`

**To edit variables:**
1. Click on the collection
2. Go to **Variables** tab
3. Update `Current Value` as needed after each test

---

## 🧪 End-to-End Testing Flow

### **Phase 1: Initial Setup** 🔧

#### Step 1: Start Your Application
```bash
cd bank
./mvnw spring-boot:run
```
Wait for application to start on `http://localhost:8080`

#### Step 2: Create Admin Account
- **Folder**: `1. Admin Setup`
- **Request**: `Create Admin`
- **Action**: Run request
- **Result**: Admin created with ID 1
- **Update Variable**: Set `admin_id = 1`

#### Step 3: Admin Login
- **Request**: `Admin Login`
- **Action**: Run and verify admin can login
- **Expected**: Login successful message

---

### **Phase 2: User Registration Flow** 👥

#### Step 4: Create User Registration Requests
- **Folder**: `2. User Registration Approval Flow`
- **Request 1**: `Create User Registration Request` (John Doe)
- **Request 2**: `Create Second User Request` (Jane Smith)
- **Expected**: Both users pending approval

#### Step 5: View Pending Approvals
- **Request**: `Get All Pending User Approvals`
- **Expected**: See 2 pending approval requests
- **Action**: Copy approval IDs from response
- **Update Variables**: 
  - `approval_id = 1` (John's approval)
  - `approval_id_2 = 2` (Jane's approval)

#### Step 6: Approve First User
- **Request**: `Approve User Registration`
- **Expected**: User created successfully, returns User ID
- **Update Variable**: Set `user_id = 1`

#### Step 7: Reject Second User (Optional)
- **Request**: `Reject User Registration`
- **Expected**: Jane's registration rejected

#### Step 8: User Login
- **Folder**: `3. User Authentication`
- **Request**: `User Login - Success`
- **Expected**: Login successful for john.doe@example.com

#### Step 9: Test Wrong Password
- **Request**: `User Login - Wrong Password`
- **Expected**: Login fails with error

---

### **Phase 3: Branch Management** 🏦

#### Step 10: Create Branch
- **Folder**: `4. Branch Management`
- **Request**: `Create Branch`
- **Expected**: Branch created
- **Action**: Copy branch ID from response
- **Update Variable**: Set `branch_id = 1`

#### Step 11: View All Branches
- **Request**: `Get All Branches`
- **Expected**: See list of all branches

---

### **Phase 4: Account Creation Flow** 💳

#### Step 12: Create Account Requests
- **Folder**: `5. Account Creation Approval Flow`
- **Request 1**: `Create Account Request - Savings`
- **Request 2**: `Create Account Request - Checking`
- **Expected**: Both account requests pending approval

#### Step 13: View Pending Account Approvals
- **Request**: `Get Pending Account Approvals`
- **Expected**: See 2 pending account approval requests
- **Action**: Copy account approval IDs
- **Update Variable**: Set `account_approval_id = 1`

#### Step 14: Approve First Account
- **Request**: `Approve Account Creation`
- **Expected**: Account created, returns account number
- **Action**: Copy account ID
- **Update Variable**: Set `account_id = 1`

#### Step 15: Approve Second Account
- **Action**: Change `account_approval_id = 2` in URL
- **Request**: `Approve Account Creation`
- **Update Variable**: Set `account_id_2 = 2`

---

### **Phase 5: Account Operations** 📊

#### Step 16: View Account Details
- **Folder**: `6. Account Operations`
- **Request**: `Get Account by ID`
- **Expected**: See full account details

#### Step 17: Check Balance (Should be 0)
- **Request**: `Get Account Balance`
- **Expected**: Balance = 0.0

#### Step 18: View User's All Accounts
- **Request**: `Get User Accounts`
- **Expected**: See both savings and checking accounts

---

### **Phase 6: Transactions** 💰

#### Step 19: Deposit Money
- **Folder**: `7. Transactions`
- **Request**: `Deposit Money` (₹1000)
- **Expected**: Deposit successful
- **Action**: Copy transaction ID
- **Update Variable**: Set `transaction_id = 1`

#### Step 20: Check Balance (Should be 1000)
- **Folder**: `6. Account Operations`
- **Request**: `Get Account Balance`
- **Expected**: Balance = 1000.0

#### Step 21: Withdraw Money
- **Folder**: `7. Transactions`
- **Request**: `Withdraw Money` (₹200)
- **Expected**: Withdrawal successful
- **New Balance**: 800.0

#### Step 22: Transfer Money
- **Request**: `Transfer Money` (₹100 from account 1 to account 2)
- **Expected**: Transfer successful
- **New Balances**: 
  - Account 1: 700.0
  - Account 2: 100.0

#### Step 23: View Transaction History
- **Request**: `Get Transaction History`
- **Expected**: See all 3 transactions (deposit, withdraw, transfer)

#### Step 24: Get Specific Transaction
- **Request**: `Get Transaction by ID`
- **Expected**: See transaction details

---

### **Phase 7: User Profile Management** 👤

#### Step 25: Update Profile
- **Folder**: `8. User Profile Management`
- **Request**: `Update User Profile`
- **Expected**: Name and email updated successfully

#### Step 26: Change Password
- **Request**: `Change Password`
- **Expected**: Password changed successfully

#### Step 27: Test Login with New Password
- **Folder**: `3. User Authentication`
- **Action**: Update password in body to `newpassword456`
- **Request**: `User Login - Success`
- **Expected**: Login successful with new password

---

### **Phase 8: Admin User Management** 🛡️

#### Step 28: View All Users
- **Folder**: `9. Admin User Management`
- **Request**: `Get All Users`
- **Expected**: See list of all users in system

#### Step 29: View User Details
- **Request**: `Get User by ID`
- **Expected**: See detailed user information

#### Step 30: Suspend User
- **Request**: `Suspend User`
- **Expected**: User suspended, status = SUSPENDED

#### Step 31: Test Login While Suspended (Should Fail)
- **Folder**: `3. User Authentication`
- **Request**: `User Login - Success`
- **Expected**: Login should fail (user suspended)

#### Step 32: View User's Accounts Status
- **Folder**: `9. Admin User Management`
- **Request**: `Get User Accounts`
- **Expected**: All accounts show SUSPENDED status

#### Step 33: Activate User
- **Request**: `Activate User`
- **Expected**: User activated, status = ACTIVE

#### Step 34: Verify Login Works Again
- **Folder**: `3. User Authentication`
- **Request**: `User Login - Success`
- **Expected**: Login successful (user active again)

---

### **Phase 9: Account Status Management** 🔒

#### Step 35: Suspend Specific Account
- **Folder**: `10. Account Status Management`
- **Request**: `Suspend Account`
- **Expected**: Account suspended

#### Step 36: Try Transaction on Suspended Account (Should Fail)
- **Folder**: `7. Transactions`
- **Request**: `Deposit Money`
- **Expected**: Should fail (account suspended)

#### Step 37: Activate Account
- **Folder**: `10. Account Status Management`
- **Request**: `Activate Account`
- **Expected**: Account active again

#### Step 38: Try Transaction Again (Should Work)
- **Folder**: `7. Transactions`
- **Request**: `Deposit Money`
- **Expected**: Deposit successful

---

### **Phase 10: Cleanup & Reports** 📈

#### Step 39: Get System Report
- **Folder**: `11. Admin Reports`
- **Request**: `Get System Report`
- **Expected**: See complete system statistics

#### Step 40: Deactivate User (Final Test)
- **Folder**: `8. User Profile Management`
- **Request**: `Deactivate User Account`
- **Expected**: User and all accounts deactivated

---

## 📋 Testing Checklist

### Core Functionality
- [ ] Admin creation and login
- [ ] User registration approval workflow
- [ ] User authentication (login/logout)
- [ ] Branch management
- [ ] Account creation approval workflow
- [ ] Account balance queries
- [ ] Deposit transactions
- [ ] Withdrawal transactions
- [ ] Transfer transactions
- [ ] Transaction history

### User Management
- [ ] Update user profile
- [ ] Change password
- [ ] Password validation
- [ ] User deactivation
- [ ] Admin suspend user
- [ ] Admin activate user

### Account Management
- [ ] Multiple account types (savings/checking)
- [ ] Account status changes
- [ ] Account suspension
- [ ] Account activation
- [ ] Account closure

### Error Handling
- [ ] Invalid credentials
- [ ] Duplicate email registration
- [ ] Insufficient balance withdrawal
- [ ] Transaction on suspended account
- [ ] Non-existent user/account queries

---

## 🎯 Quick Test Sequence (Minimal)

For rapid testing of core features:

1. **Create Admin** → `POST /admin/create`
2. **Create User Request** → `POST /users/create`
3. **Approve User** → `POST /api/admin/user-approvals/1/approve?adminId=1`
4. **User Login** → `POST /users/login`
5. **Create Branch** → `POST /branches/create`
6. **Create Account Request** → `POST /accounts/create`
7. **Approve Account** → `POST /account-approvals/1/approve?adminId=1`
8. **Deposit** → `POST /transactions/deposit?accountId=1&amount=1000`
9. **Check Balance** → `GET /accounts/1/balance`
10. **Withdraw** → `POST /transactions/withdraw?accountId=1&amount=200`

---

## 🐛 Common Issues & Solutions

### Issue 1: "Admin not found"
**Solution**: Run `Create Admin` request first

### Issue 2: "User not found"
**Solution**: 
1. Check user approval was completed
2. Verify `user_id` variable is correct

### Issue 3: "Account not found"
**Solution**: 
1. Check account approval was completed
2. Verify `account_id` variable is correct

### Issue 4: "Insufficient balance"
**Solution**: Deposit money before withdrawing

### Issue 5: "Transaction failed - account suspended"
**Solution**: Activate account using `/account-status/{id}/activate`

### Issue 6: Variables not updating
**Solution**: Manually update collection variables after each entity creation

---

## 📊 Expected Database State After Full Test

**Users**: 1 (John Doe)
**Accounts**: 2 (1 Savings, 1 Checking)
**Branches**: 1 (Downtown Branch)
**Transactions**: ~5-10 (deposits, withdrawals, transfers)
**User Status**: DEACTIVATED (if final test completed)
**Account Status**: INACTIVE (if final test completed)

---

## 🚀 Tips for Efficient Testing

1. **Run requests sequentially** - Don't skip steps
2. **Update variables** - Always update IDs after entity creation
3. **Check responses** - Verify success messages and returned IDs
4. **Use folders** - Collapse/expand folders to stay organized
5. **Save results** - Use Postman's test results for documentation
6. **Test error cases** - Try invalid inputs to verify error handling
7. **Reset database** - Drop and recreate DB between full test runs

---

## 🎓 Learning Exercises

After completing the full test:

1. **Modify test data** - Use different names, amounts, etc.
2. **Test edge cases** - Zero amounts, negative numbers, special characters
3. **Concurrent users** - Create multiple users and test interactions
4. **Transaction patterns** - Test complex multi-step transaction flows
5. **Error recovery** - Test suspend → activate → retry patterns

---

## ✅ Post-Testing Verification

After completing all tests, verify:

1. **Database Consistency**
   - Check user-account relationships
   - Verify transaction balances add up
   - Confirm status fields are correct

2. **Business Rules**
   - No negative balances
   - All transactions have both debit and credit entries
   - Suspended accounts cannot transact

3. **Security**
   - Passwords are encrypted
   - Invalid credentials are rejected
   - Proper role separation (user vs admin)

---

**Good luck with your testing! 🎉**

For issues or questions, check the application logs or contact the development team.
