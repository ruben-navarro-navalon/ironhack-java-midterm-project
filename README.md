# ironhack-java-midterm-project
# Demo banking app

## Introduction & Features:
This is a demo banking app made with spring. It uses a SQL database for storing data, and it can:
- Register new users by an admin: admins, account_holders or third_parties
- Create new accounts by an admin: checking, student checking, savings and credit cards. Each account type has its own properties (Minimum balance, interest rates, etc.)
- Make transfers between Account holders, or between Account holders and third parties.
- Show balance of an account: only admins and account owners can do this.
- Apply interest or maintenance fees if needed when checking balances.
- Apply penalty fees when transferring if needed.
- Detect fraud in transfers, according to fraud rules described in the comments of the code.

## Use
- First of all, run the .sql files located in <code>sql_creation</code> folder. They create the two databases needed: working one and testing one.
- A default admin is created in the working database. To use it, you will need to log as admin/1234.
- You can run the program typing <code>mvn spring-boot:run</code>.
- Now, you can create other users, accounts, and make transfers between them using http requests (for example using Postman software).
- If you want, you can run a lot of tests automatically with your favourite IDE (intelliJ Idea was used to create this app).

## HTTP routes:
| Route | Method* | Description |
| ---| --- | --- |
| /admin/new | post | Creates a new admin. Only admins can do this. |
| /admin/account-holder | post | Creates a new account holder. Only admins can do this. |
| /admin/third-party | post | Creates a new third party. Only admins can do this. |
| /admin/checking | post | Creates a new checking or student checking account deppending on owners age. Only admins can do this. |
| /admin/checking | get | Show all checking accounts. Only admins can do this. |
| /admin/checking/{id} | get | Show checking account with id {id}. Only admins can do this. |
| /admin/student-checking | get | Show all student checking accounts. Only admins can do this. |
| /admin/student-checking/{id} | get | Show student checking account with id {id}. Only admins can do this. |
| /admin/savings | post | Creates a new savings account. Only admins can do this. |
| /admin/savings | get | Show all savings accounts. Only admins can do this. |
| /admin/savings/{id} | get | Show savings account with id {id}. Only admins can do this. |
| /admin/credit-card | post | Creates a new credit card account. Only admins can do this. |
| /admin/credit-card | get | Show all credit card accounts. Only admins can do this. |
| /admin/credit-card/{id} | get | Show credit card account with id {id}. Only admins can do this. |
| /admin/modify-balance/{id} | patch | Modifies balance of the account with id {id}. Only admins can do this. |
| /check-balance/{id} | get | Show balance of the account with id {id}. Only admins and owners can do this. |
| /transfer | post | Makes a transfer between accounts holders. Only account owners can do this. |
| /transfer-to-third-party/{hashKey}/{secretKey} | post | Makes a transfer from account holder (with {secretKey}) to third party (with {hashKey}). |
| /transfer-from-third-party/{hashKey}/{secretKey} | post | Makes a transfer from third party (with {hashKey}) to account holder (with {secretKey}). |

***Post and patch routes requires valid JSONs. See coding for more info**
