# Credit Banking System

A desktop banking application built with *Java 11, **Swing (FlatLaf), **MySQL, and **Maven*.  
It features customer management, deposits, withdrawals, credit requests, penalties, and transaction history with a modern UI.

---

## ✨ Features

- Secure authentication (Admin & Customer) via [com.creditbank.db.DatabaseManager](src/main/java/com/creditbank/db/DatabaseManager.java)
- Admin dashboard:
  - View all customers & credit requests ([com.creditbank.ui.AdminPanel](src/main/java/com/creditbank/ui/AdminPanel.java))
  - Register customers
  - Impose penalties
  - Approve credit requests
- Customer portal:
  - View balance, penalties, credit status ([com.creditbank.ui.CustomerPanel](src/main/java/com/creditbank/ui/CustomerPanel.java))
  - Deposit & withdraw funds
  - Submit credit requests
- Transaction logging ([com.creditbank.model.Transaction](src/main/java/com/creditbank/model/Transaction.java))
- Credit request workflow ([com.creditbank.model.CreditRequest](src/main/java/com/creditbank/model/CreditRequest.java))
- Modern theming with FlatLaf in [com.creditbank.App](src/main/java/com/creditbank/App.java)
- Auto-refresh dashboards (Timers in UI panels)

---

## 🧱 Architecture Overview

| Layer | Purpose | Key Classes |
|-------|---------|-------------|
| UI (Swing) | Presentation & interaction | [com.creditbank.ui.MainFrame](src/main/java/com/creditbank/ui/MainFrame.java), [com.creditbank.ui.LoginPanel](src/main/java/com/creditbank/ui/LoginPanel.java), [com.creditbank.ui.AdminPanel](src/main/java/com/creditbank/ui/AdminPanel.java), [com.creditbank.ui.CustomerPanel](src/main/java/com/creditbank/ui/CustomerPanel.java) |
| Data Access | JDBC operations | [com.creditbank.db.DatabaseManager](src/main/java/com/creditbank/db/DatabaseManager.java) |
| Domain Models | Entities | [com.creditbank.model.Customer](src/main/java/com/creditbank/model/Customer.java), [com.creditbank.model.Admin](src/main/java/com/creditbank/model/Admin.java), [com.creditbank.model.Transaction](src/main/java/com/creditbank/model/Transaction.java), [com.creditbank.model.CreditRequest](src/main/java/com/creditbank/model/CreditRequest.java), [com.creditbank.model.User](src/main/java/com/creditbank/model/User.java) |
| Bootstrapping | Startup, Look & Feel | [com.creditbank.App](src/main/java/com/creditbank/App.java) |
| Database Schema | DDL + seed data | [src/main/resources/schema.sql](src/main/resources/schema.sql) |

---

## 📂 Project Structure


credit-banking-system/
  pom.xml
  src/
    main/
      java/
        com/creditbank/
          App.java
          db/DatabaseManager.java
          model/
            Admin.java
            CreditRequest.java
            Customer.java
            Transaction.java
            User.java
          ui/
            AdminPanel.java
            CustomerPanel.java
            LoginPanel.java
            MainFrame.java
      resources/
        schema.sql


---

## 💾 Database Setup (MySQL)

1. Ensure MySQL 8.x is running.
2. Create the schema & sample data:

   Option A (CLI):
   sql
   SOURCE /absolute/path/to/src/main/resources/schema.sql;
   

   Option B (Workbench): Open [src/main/resources/schema.sql](src/main/resources/schema.sql) and execute all.

3. Verify:
   sql
   USE credit_bank;
   SELECT * FROM users;
   

Default seed users (from schema):
- Admin: admin / admin123
- Customers: hashtag_harsh / password123, artist_prakshi / password123

---

## ⚙️ Configuration

Current JDBC settings are hardcoded in [com.creditbank.db.DatabaseManager](src/main/java/com/creditbank/db/DatabaseManager.java):

java
private static final String DB_URL = "jdbc:mysql://localhost:3306/credit_bank";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "CHANGE_ME";


Recommendation (future): externalize via environment variables or a config.properties.

---

## ▶️ Running the Application

### 1. Build
bash
mvn -DskipTests clean package


### 2. Run via Maven Exec Plugin
bash
mvn exec:java


### 3. Run JAR (ensure MySQL driver on classpath if using plain java)
bash
java -cp "target/credit-banking-system-1.0.0.jar;~/.m2/repository/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar" com.creditbank.App

(Use : instead of ; on Linux/macOS.)

### 4. IDE (VS Code / IntelliJ)
- Open project root
- Let Maven sync
- Run main class: [com.creditbank.App](src/main/java/com/creditbank/App.java)

---

## 🔐 Login Flow

1. Application starts at [LoginPanel](src/main/java/com/creditbank/ui/LoginPanel.java)
2. Credentials validated via authenticateUser() in [DatabaseManager](src/main/java/com/creditbank/db/DatabaseManager.java)
3. Routes:
   - ADMIN → [AdminPanel](src/main/java/com/creditbank/ui/AdminPanel.java)
   - CUSTOMER → [CustomerPanel](src/main/java/com/creditbank/ui/CustomerPanel.java)

---

## 💳 Core Operations

| Action | Method (Database Layer) |
|--------|--------------------------|
| Deposit | performDeposit() in [DatabaseManager](src/main/java/com/creditbank/db/DatabaseManager.java) |
| Withdraw | performWithdrawal() |
| Penalty | imposePenalty() |
| Submit Credit Request | submitCreditRequest() |
| Approve Credit | approveCreditRequest() |
| Load Transactions | getCustomerTransactions() |

All multi-step financial updates use transactions (setAutoCommit(false) + rollback safety).

---

## 🛠 Troubleshooting

| Issue | Resolution |
|-------|------------|
| Cannot connect to DB | Confirm MySQL running, credentials, port 3306 |
| Tables missing | Re-run schema: [schema.sql](src/main/resources/schema.sql) |
| GUI freezes on launch | Ensure running on JDK 11+, avoid blocking EDT |
| Wrong credentials | Check users table |
| Permission errors | Run MySQL with proper grants |

---

## 🚀 Possible Enhancements

- Password hashing (BCrypt) instead of plaintext
- Externalized config file
- Reject credit request implementation (currently placeholder in [AdminPanel](src/main/java/com/creditbank/ui/AdminPanel.java))
- Transfer feature (CustomerPanel button placeholder)
- Statement export (PDF / CSV)
- Role-based session auditing

---

## ⚠ Security Notice

Current implementation stores raw passwords in the database and application memory.  
Do not use in production without hashing & secure credential management.

---

## 📜 License

Internal / Educational sample (no explicit license file present).

---

## ✅ Quick Start (Copy/Paste)

bash
git clone <repo-url>
cd credit-banking-system
mvn -DskipTests clean package
# Import schema
mysql -u root -p < src/main/resources/schema.sql
mvn exec:java


Login with:

admin / admin123
hashtag_harsh / password123


---

## 🏁 About

App version: 1.0.0 (via [com.creditbank.App.getVersion()](src/main/java/com/creditbank/App.java))  
Look & Feel: FlatLaf (flatlaf dependency in [pom.xml](pom.xml))  
Java: 11 (configured in [pom.xml](pom.xml))  

Enjoy building on it.
