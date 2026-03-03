# Parasoft Demo Project

This is a multi-module Maven project containing Parasoft demo applications.

## Project Structure

This project consists of two modules:

- **bank**: Banking application module with sample bank operations
- **atm**: ATM (Automated Teller Machine) banking terminal that depends on bank

### Directory Layout

```
parasoft-demo-jbank/
├── pom.xml                 # Parent POM with shared configuration
├── README.md
├── bank/                   # Banking application module
│   ├── pom.xml
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── jdemo/
│   │               └── bank/
│   │                   ├── Bank.java
│   │                   ├── Account.java
│   │                   ├── Customer.java
│   │                   ├── CreditCard.java
│   │                   ├── AbstractTransaction.java
│   │                   ├── DepositTransaction.java
│   │                   ├── WithdrawalTransaction.java
│   │                   ├── ITransaction.java
│   │                   ├── ICreditAgency.java
│   │                   ├── BankState.java
│   │                   ├── ConnectionException.java
│   │                   └── LogAccountInfo.java
│   └── target/
└── atm/                    # ATM application module
    ├── pom.xml
    ├── src/
    │   ├── main/
    │   │   └── java/
    │   │       └── jdemo/
    │   │           └── atm/
    │   │               └── ATM.java
    │   └── test/
    │       └── java/
    └── target/
```

## Building the Project

To build all modules:

```bash
mvn clean install
```

To build a specific module:

```bash
cd bank
mvn clean install
```

To compile without running tests:

```bash
mvn clean compile
```

## Module Details

### bank
The bank module contains a sample bank application with various errors and bad practice examples which can be detected by Jtest analysis.

**Key Components:**
- `Bank.java` - Main bank class managing accounts
- `Account.java` - Bank account representation
- `Customer.java` - Customer information
- `CreditCard.java` - Credit card management
- Transaction classes for deposits and withdrawals
- Error handling and logging utilities

### atm
The atm module contains the ATM (Automated Teller Machine) banking terminal application. An ATM allows customers to perform financial transactions such as withdrawals, deposits, and balance inquiries without needing a human teller.

**Dependencies:**
- Depends on the `bank` module for banking operations

## Technology Stack

- **Java**: 11
- **Build Tool**: Maven 
- **Testing**: JUnit 5 (Jupiter), JUnit Vintage, Mockito
- **Quality Analysis**: Parasoft Jtest

## Maven Reactor

The project uses Maven's multi-module reactor build. The build order is:
1. Parent POM
2. bank module
3. atm module (depends on bank)

## Development

The project is configured with:
- UTF-8 source encoding
- Java 11 source and target compatibility
- Shared dependency management in parent POM
- Common plugin configuration for all modules