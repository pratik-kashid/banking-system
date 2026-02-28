# 🏦 SecureBank - Complete Banking System

A full-stack banking application built with **Spring Boot** (Backend) and **HTML/CSS/JavaScript** (Frontend) with **MySQL** database.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![JWT](https://img.shields.io/badge/JWT-Authentication-red)

---

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Database Setup](#-database-setup)
- [Running the Application](#-running-the-application)
- [API Endpoints](#-api-endpoints)
- [User Guide](#-user-guide)
- [Security Features](#-security-features)
- [Project Structure](#-project-structure)
- [Troubleshooting](#-troubleshooting)
- [Screenshots](#-screenshots)

---

## ✨ Features

### 🔐 Authentication & Security
- User registration with email verification
- JWT-based authentication
- Password encryption using BCrypt
- PIN-based transaction security
- Session management

### 💳 Account Management
- User-defined account numbers (10-16 digits)
- Multiple account types (Savings, Current, Fixed Deposit)
- Real-time balance updates
- Account status tracking
- Secure PIN system for transactions

### 💰 Transactions
- **Deposit Money** - Add funds to account
- **Withdraw Money** - Requires PIN verification
- **Transfer Money** - Transfer between accounts with PIN
- Complete transaction history
- Balance tracking after each transaction

### 📊 Dashboard
- Real-time account overview
- Transaction history
- User profile management
- Responsive design
- Professional UI/UX

### 🔍 Additional Features
- Real-time data from MySQL database
- RESTful API architecture
- CORS enabled for frontend integration
- Error handling and validation
- Professional banking interface

---

## 🛠️ Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.2**
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Database operations
- **Hibernate** - ORM framework
- **JWT (JSON Web Tokens)** - Secure authentication
- **MySQL 8.0** - Database
- **Lombok** - Reduce boilerplate code
- **Maven** - Dependency management

### Frontend
- **HTML5**
- **CSS3** - Modern styling with gradients
- **JavaScript (ES6+)** - Async/await, Fetch API
- **Google Fonts** - Playfair Display & Outfit

---

## 📦 Prerequisites

Before running this application, make sure you have:

1. **Java Development Kit (JDK) 17 or higher**
   - Download: https://www.oracle.com/java/technologies/downloads/
   - Verify: `java -version`

2. **MySQL 8.0 or higher**
   - Download: https://dev.mysql.com/downloads/installer/
   - MySQL Workbench (optional but recommended)

3. **Maven 3.6+** (Usually comes with IntelliJ IDEA)
   - Verify: `mvn -version`

4. **IntelliJ IDEA** (Recommended) or any Java IDE
   - Download: https://www.jetbrains.com/idea/download/

5. **Git** (for cloning the repository)
   - Download: https://git-scm.com/downloads

---

## 🚀 Installation

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/banking-system.git
cd banking-system
```

### Step 2: Configure Database

1. Open MySQL Workbench
2. Create a new connection (if not exists)
3. Run the following SQL commands:

```sql
-- Create database
CREATE DATABASE banking_system;

-- Verify database is created
SHOW DATABASES;
```

### Step 3: Update Database Configuration

Open `src/main/resources/application.properties` and update:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/banking_system
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

# Change YOUR_MYSQL_PASSWORD to your actual MySQL root password
```

### Step 4: Install Dependencies

```bash
# Using Maven
mvn clean install
```

Or in IntelliJ IDEA:
- Right-click on `pom.xml` → Maven → Reload Project

---

## 🗄️ Database Setup

### Automatic Setup (Recommended)

When you run the application for the first time, Hibernate will automatically create all tables:

```
users
accounts
transactions
```

### Manual Setup (Optional)

If you prefer to create tables manually, run this SQL:

```sql
USE banking_system;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15),
    verified BOOLEAN DEFAULT FALSE,
    enabled BOOLEAN DEFAULT TRUE,
    created_at DATETIME NOT NULL,
    role VARCHAR(20) DEFAULT 'USER'
);

CREATE TABLE accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(16) UNIQUE NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    pin VARCHAR(6) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_id VARCHAR(100) UNIQUE NOT NULL,
    type VARCHAR(20) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    balance_after DECIMAL(15,2) NOT NULL,
    timestamp DATETIME NOT NULL,
    description VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    related_account_number VARCHAR(16),
    account_id BIGINT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);
```

### Verify Tables

```sql
USE banking_system;
SHOW TABLES;
```

You should see:
```
+---------------------------+
| Tables_in_banking_system  |
+---------------------------+
| accounts                  |
| transactions              |
| users                     |
+---------------------------+
```

---

## ▶️ Running the Application

### Method 1: Using IntelliJ IDEA (Recommended)

1. Open the project in IntelliJ IDEA
2. Wait for Maven to download dependencies
3. Find `BankingApplication.java` in:
   ```
   src/main/java/com/banking/BankingApplication.java
   ```
4. Right-click → **Run 'BankingApplication'**
5. Or press **Shift + F10**

### Method 2: Using Maven Command Line

```bash
# In project root directory
mvn spring-boot:run
```

### Method 3: Using JAR file

```bash
# Build the JAR
mvn clean package

# Run the JAR
java -jar target/banking-system-0.0.1-SNAPSHOT.jar
```

### Verify Application is Running

Open browser and go to:
```
http://localhost:8080
```

You should see the SecureBank homepage! 🎉

Console output should show:
```
Started BankingApplication in X.XXX seconds
```

---

## 🌐 API Endpoints

### Authentication APIs

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/verify/{username}` | Verify user account | No |
| POST | `/api/auth/login` | Login user | No |

### User APIs

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/users/me` | Get current user info | Yes (JWT) |

### Account APIs

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/accounts` | Create new account | Yes (JWT) |
| GET | `/api/accounts` | Get user's accounts | Yes (JWT) |
| GET | `/api/accounts/{accountNumber}` | Get specific account | Yes (JWT) |
| DELETE | `/api/accounts/{accountNumber}` | Delete account | Yes (JWT) |

### Transaction APIs

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/transactions/deposit` | Deposit money | Yes (JWT) |
| POST | `/api/transactions/withdraw` | Withdraw money (requires PIN) | Yes (JWT) |
| POST | `/api/transactions/transfer` | Transfer money (requires PIN) | Yes (JWT) |
| GET | `/api/transactions/account/{accountNumber}` | Get transaction history | Yes (JWT) |

---

## 📖 User Guide

### 1. Registration

1. Go to `http://localhost:8080`
2. Click **"Get Started"** or **"Sign Up"**
3. Fill in the registration form:
   - Full Name
   - Email
   - Username
   - Phone Number
   - Password
4. Click **"Create Account"**
5. Account is **auto-verified** ✅

### 2. Login

1. Click **"Login"** tab
2. Enter username and password
3. Click **"Sign In"**
4. Redirected to dashboard

### 3. Create Banking Account

**First time login:**
1. You'll see a welcome screen
2. Click **"Create Account"**
3. Fill in account details:
   - **Account Number**: Choose 10-16 digits (e.g., `1234567890`)
   - **Account Type**: Savings/Current/Fixed Deposit
   - **PIN**: Choose 4-6 digit PIN (e.g., `1234`)
   - **Initial Deposit**: Optional amount (e.g., `10000`)
4. Click **"Create Account"**
5. Your account is ready! 🎉

### 4. Deposit Money

1. Click **"💰 Deposit"** button
2. Enter amount
3. Add description (optional)
4. Click **"Deposit"**
5. Balance updated instantly ✅

### 5. Withdraw Money

1. Click **"💵 Withdraw"** button
2. Enter amount
3. **Enter your PIN** 🔐
4. Add description (optional)
5. Click **"Withdraw"**
6. PIN verified → Money withdrawn ✅

### 6. Transfer Money

1. Click **"🔄 Transfer Money"** in sidebar
2. Enter recipient account number
3. Enter amount
4. **Enter your PIN** 🔐
5. Add description (optional)
6. Click **"Transfer Money"**
7. PIN verified → Transfer complete ✅

### 7. View Transactions

1. Click **"💸 Transactions"** in sidebar
2. See complete transaction history:
   - Transaction type
   - Date & time
   - Amount (+ for credit, - for debit)
   - Balance after transaction
   - Status

### 8. Profile & Settings

- **Profile**: View your personal information
- **Settings**: Manage preferences
- **Logout**: Click logout button at bottom of sidebar

---

## 🔒 Security Features

### 1. Authentication
- **JWT Tokens**: Secure stateless authentication
- **Token Expiration**: Configurable token lifetime
- **Password Encryption**: BCrypt hashing (strength: 12)

### 2. Authorization
- **Role-Based Access**: USER and ADMIN roles
- **Endpoint Protection**: Spring Security configuration
- **User Isolation**: Users can only access their own data

### 3. Transaction Security
- **PIN Verification**: Required for withdrawals and transfers
- **PIN Storage**: Stored securely in database
- **Account Validation**: Verify account ownership before operations

### 4. Data Validation
- **Input Validation**: Bean Validation (@Valid, @NotNull, etc.)
- **Account Number Uniqueness**: Database constraints
- **Balance Checks**: Prevent negative balances

### 5. CORS Configuration
- **Allowed Origins**: Configure in SecurityConfig
- **Allowed Methods**: GET, POST, PUT, DELETE
- **Credentials Support**: Enabled

---

## 📁 Project Structure

```
banking-system/
├── src/
│   ├── main/
│   │   ├── java/com/banking/
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── AccountController.java
│   │   │   │   ├── TransactionController.java
│   │   │   │   └── UserController.java
│   │   │   ├── dto/
│   │   │   │   ├── ApiResponse.java
│   │   │   │   ├── AuthRequest.java
│   │   │   │   ├── RegisterRequest.java
│   │   │   │   ├── AccountRequest.java
│   │   │   │   ├── TransactionRequest.java
│   │   │   │   └── TransferRequest.java
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── Account.java
│   │   │   │   └── Transaction.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── AccountRepository.java
│   │   │   │   └── TransactionRepository.java
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── AccountService.java
│   │   │   │   ├── TransactionService.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   ├── security/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtUtil.java
│   │   │   │   └── JwtAuthenticationFilter.java
│   │   │   └── BankingApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html
│   │       │   └── dashboard.html
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

---

## 🔧 Configuration

### application.properties

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/banking_system
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# JWT Configuration
jwt.secret=mySecretKeyForJWTTokenGenerationAndValidation12345
jwt.expiration=86400000

# Logging
logging.level.com.banking=DEBUG
logging.level.org.springframework.security=DEBUG
```

### Important Settings

| Property | Value | Description |
|----------|-------|-------------|
| `server.port` | 8080 | Application port |
| `spring.jpa.hibernate.ddl-auto` | update | Auto-create/update tables |
| `jwt.expiration` | 86400000 | Token expiry (24 hours in ms) |
| `spring.jpa.show-sql` | true | Show SQL queries in console |

---

## 🐛 Troubleshooting

### Problem 1: Cannot Connect to Database

**Error:** `Communications link failure`

**Solution:**
1. Check MySQL is running
2. Verify database name: `banking_system`
3. Check username/password in `application.properties`
4. Test connection in MySQL Workbench

```sql
-- Test query
SELECT 1;
```

### Problem 2: Port 8080 Already in Use

**Error:** `Port 8080 is already in use`

**Solution:**
Change port in `application.properties`:
```properties
server.port=8081
```

Or kill the process using port 8080:
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```

### Problem 3: 403 Forbidden Error

**Error:** `HTTP ERROR 403`

**Solution:**
1. Check `SecurityConfig.java` has correct permitAll() paths
2. Clear browser cache
3. Check JWT token is valid
4. Verify CORS configuration

### Problem 4: Tables Not Created

**Error:** Tables missing in database

**Solution:**
1. Check `spring.jpa.hibernate.ddl-auto=update` in properties
2. Stop application
3. Drop database and recreate:
```sql
DROP DATABASE banking_system;
CREATE DATABASE banking_system;
```
4. Restart application

### Problem 5: JWT Token Expired

**Error:** `401 Unauthorized`

**Solution:**
1. Login again to get new token
2. Increase token expiration in properties
3. Clear localStorage in browser

```javascript
// Clear in browser console
localStorage.clear();
```

### Problem 6: Invalid PIN Error

**Error:** `Invalid PIN. Please enter the correct PIN.`

**Solution:**
1. Check PIN was saved correctly during account creation
2. Verify PIN in database:
```sql
SELECT account_number, pin FROM accounts;
```
3. PIN must match exactly (case-sensitive if not numeric)

### Problem 7: Circular Reference Error

**Error:** `Could not write JSON: Infinite recursion`

**Solution:**
Verify these annotations are present:
- `@JsonIgnore` on `User.accounts`
- `@JsonIgnore` on `Account.user`
- `@JsonIgnore` on `Account.transactions`
- `@JsonIgnore` on `Transaction.account`

---

## 📸 Screenshots

### Homepage
![Homepage](screenshots/homepage.png)
*Modern, professional banking homepage with hero section*

### Dashboard
![Dashboard](screenshots/dashboard.png)
*Single card layout showing account details, balance, and recent transactions*

### Create Account
![Create Account](screenshots/create-account.png)
*User-defined account number and PIN setup*

### Withdraw with PIN
![Withdraw](screenshots/withdraw.png)
*PIN verification required for withdrawals*

### Transfer Money
![Transfer](screenshots/transfer.png)
*Transfer between accounts with PIN security*

### Transaction History
![Transactions](screenshots/transactions.png)
*Complete transaction history with balance tracking*

---

## 📊 Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15),
    verified BOOLEAN DEFAULT FALSE,
    enabled BOOLEAN DEFAULT TRUE,
    created_at DATETIME NOT NULL,
    role VARCHAR(20) DEFAULT 'USER'
);
```

### Accounts Table
```sql
CREATE TABLE accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(16) UNIQUE NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    pin VARCHAR(6) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Transactions Table
```sql
CREATE TABLE transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_id VARCHAR(100) UNIQUE NOT NULL,
    type VARCHAR(20) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    balance_after DECIMAL(15,2) NOT NULL,
    timestamp DATETIME NOT NULL,
    description VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    related_account_number VARCHAR(16),
    account_id BIGINT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);
```

---

## 🚀 Deployment

### Deploy to Heroku

1. Create `Procfile`:
```
web: java -jar target/banking-system-0.0.1-SNAPSHOT.jar
```

2. Add MySQL database (ClearDB or JawsDB)

3. Update `application.properties` with environment variables

4. Deploy:
```bash
heroku create securebank-app
git push heroku main
```

### Deploy to AWS

1. Create EC2 instance
2. Install Java and MySQL
3. Upload JAR file
4. Run application:
```bash
java -jar banking-system.jar
```

---

## 📝 API Request Examples

### Register User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Smith",
    "email": "john@example.com",
    "username": "johnsmith",
    "phoneNumber": "1234567890",
    "password": "password123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johnsmith",
    "password": "password123"
  }'
```

### Create Account
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "accountNumber": "1234567890",
    "accountType": "SAVINGS",
    "pin": "1234",
    "initialDeposit": 10000
  }'
```

### Deposit Money
```bash
curl -X POST http://localhost:8080/api/transactions/deposit \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "accountNumber": "1234567890",
    "amount": 5000,
    "description": "Salary deposit"
  }'
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- Email: your.email@example.com
- LinkedIn: [Your Name](https://linkedin.com/in/yourname)

---

## 🙏 Acknowledgments

- Spring Boot Documentation
- MySQL Documentation
- JWT.io for token insights
- Font Awesome for icons
- Google Fonts for typography

---

## 📞 Support

If you have any questions or need help, please:

1. Check the [Troubleshooting](#-troubleshooting) section
2. Open an issue on GitHub
3. Contact via email: support@securebank.com

---

## 🔮 Future Enhancements

- [ ] Email notifications for transactions
- [ ] Two-factor authentication (2FA)
- [ ] Mobile application (React Native)
- [ ] Statement generation (PDF)
- [ ] Loan management system
- [ ] Credit/Debit card integration
- [ ] Bill payment features
- [ ] Investment portfolio tracking
- [ ] Multi-currency support
- [ ] Admin dashboard

---

## 📈 Version History

### v1.0.0 (Current)
- ✅ User registration and authentication
- ✅ Account management with PIN
- ✅ Deposit, Withdraw, Transfer operations
- ✅ Transaction history
- ✅ JWT authentication
- ✅ MySQL database integration
- ✅ Responsive UI

---

## 💡 Tips & Best Practices

### For Developers

1. **Always use transactions** for money operations
2. **Hash PINs** in production (use BCrypt like passwords)
3. **Validate inputs** on both frontend and backend
4. **Use prepared statements** to prevent SQL injection
5. **Keep JWT secret** secure (use environment variables)
6. **Test thoroughly** before deployment
7. **Use HTTPS** in production
8. **Enable CORS** only for trusted origins
9. **Log important operations** for audit trail
10. **Backup database** regularly

### For Users

1. **Choose strong PINs** (not 1234!)
2. **Keep credentials** secure
3. **Logout** after each session
4. **Check transaction history** regularly
5. **Report suspicious activity** immediately

---

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [JWT Introduction](https://jwt.io/introduction)
- [REST API Best Practices](https://restfulapi.net/)

---

## ⭐ Star History

If you find this project helpful, please give it a ⭐!

---

**Built with ❤️ using Spring Boot, MySQL, and modern web technologies**

---

*Last Updated: February 2026*
