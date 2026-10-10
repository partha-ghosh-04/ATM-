# 🏧 JavaBank ATM – Console Application

A complete, fully functional **ATM Interface Console Application** built with **Core Java** and **Object-Oriented Programming**. Designed as an internship-quality project demonstrating encapsulation, abstraction, exception handling, and clean code practices.

---

## 📁 Project Structure

```
ATM/
├── src/
│   └── atm/
│       ├── Main.java         ← Application entry point
│       ├── ATM.java          ← Login flow, menu, and transaction operations
│       ├── Account.java      ← Account details, PIN, balance management
│       ├── Transaction.java  ← Transaction record (type, amount, date/time)
│       └── Bank.java         ← Account registry, authentication, and transfers
├── out/                      ← Compiled .class files (auto-generated)
└── README.md
```

---

## ✅ Features

| Feature | Status |
|---|---|
| User login with PIN validation | ✅ |
| Max 3 login attempts with lockout | ✅ |
| Transaction History (session-scoped) | ✅ |
| Withdraw with balance check | ✅ |
| Deposit with validation | ✅ |
| Fund Transfer (atomic, with validation) | ✅ |
| Transfer to self blocked | ✅ |
| Transfer to nonexistent account blocked | ✅ |
| `BigDecimal` for precise monetary math | ✅ |
| Full input validation (negative, zero, non-numeric) | ✅ |
| No crash on invalid input | ✅ |
| Clean session exit | ✅ |

---

## 🔑 Sample Test Accounts

> **⚠ Security Notice**: These credentials are for demonstration only. Real banking systems must use hashed passwords, secure storage, and multi-factor authentication. Never hardcode credentials in production software.

| User ID | PIN  | Account Holder  | Initial Balance |
|---------|------|-----------------|----------------|
| 1001    | 1234 | Alice Johnson   | ₹10,000.00     |
| 1002    | 5678 | Bob Smith       | ₹5,000.00      |
| 1003    | 9999 | Carol Williams  | ₹7,500.00      |

---

## 🛠 Prerequisites

- **Java JDK 17 or higher** — [Download here](https://www.oracle.com/java/technologies/downloads/)
- Verify your installation: `java -version` and `javac -version`

---

## 🚀 How to Run

### Option A — Command Line (Windows)

```powershell
# 1. Compile all sources
javac -d out src\atm\Transaction.java src\atm\Account.java src\atm\Bank.java src\atm\ATM.java src\atm\Main.java

# 2. Run the application
java -cp out atm.Main
```

### Option B — Eclipse IDE

1. Open Eclipse → **File → New → Java Project** → Name it `ATM`.
2. Right-click `src` → **New → Package** → enter `atm`.
3. Right-click the `atm` package → **New → Class** for each of the five files.
4. Paste the source code from each `.java` file.
5. Right-click `Main.java` → **Run As → Java Application**.

### Option C — IntelliJ IDEA

1. Open IntelliJ → **New Project → Java** (no build system needed).
2. Create the `src/atm` directory structure under the project root.
3. Copy all `.java` files into `src/atm/`.
4. Right-click `Main.java` → **Run 'Main.main()'**.

---

## 🖥 Sample Console Output

### Login
```
═══════════════════════════════════════════════════════════════
           Welcome to JavaBank ATM
              Automated Teller Machine
═══════════════════════════════════════════════════════════════

  Please log in. Attempts remaining: 3
  Enter User ID : 1001
  Enter PIN     : 1234

  ✔  Login successful! Welcome, Alice Johnson.
```

### Withdrawal
```
═══════════════════════════════════════════════════════════════
  Withdrawal
═══════════════════════════════════════════════════════════════
  Available Balance: ₹10,000.00
  Enter amount to withdraw: ₹2000

  ✔  Withdrawal successful!
     Withdrawn : ₹2000.00
     Remaining : ₹8,000.00
```

### Insufficient Funds
```
  Enter amount to withdraw: ₹999999

  [!] Insufficient Funds
```

### Deposit
```
═══════════════════════════════════════════════════════════════
  Deposit
═══════════════════════════════════════════════════════════════
  Enter amount to deposit: ₹5000

  ✔  Deposit successful!
     Deposited   : ₹5,000.00
     New Balance : ₹13,000.00
```

### Transfer
```
═══════════════════════════════════════════════════════════════
  Fund Transfer
═══════════════════════════════════════════════════════════════
  Available Balance: ₹13,000.00
  Enter recipient Account ID: 1002
  Enter amount to transfer: ₹3000

  ✔  Transfer successful!
     Transferred : ₹3,000.00  →  Account 1002
     Remaining   : ₹10,000.00
```

### Transaction History
```
  Type            | Date & Time          | Amount (₹)     | Balance (₹)  | Description
  ──────────────────────────────────────────────────────────────────────────────────────────
  WITHDRAWAL      | 10-Oct-2026 17:41:01 |       2,000.00 |     8,000.00 | ATM Withdrawal
  DEPOSIT         | 10-Oct-2026 17:41:05 |       5,000.00 |    13,000.00 | ATM Deposit
  TRANSFER OUT    | 10-Oct-2026 17:41:09 |       3,000.00 |    10,000.00 | Transfer to Account 1002
```

### Logout
```
═══════════════════════════════════════════════════════════════
  Thank you for using JavaBank ATM.
  Your session has ended. Have a great day!
═══════════════════════════════════════════════════════════════
```

---

## 🧪 Testing Checklist

| # | Test Case | Expected Result |
|---|-----------|-----------------|
| 1 | Login with valid credentials (1001 / 1234) | ✔ Login successful |
| 2 | Login with wrong PIN 3 times | ✘ Access denied, session ends |
| 3 | Withdraw ₹500 (balance ₹10,000) | ✔ Success, balance ₹9,500 |
| 4 | Withdraw ₹999999 (insufficient) | `[!] Insufficient Funds` |
| 5 | Deposit ₹2000 | ✔ Success, balance updated |
| 6 | Deposit -100 or 0 | `[!] Amount must be greater than zero` |
| 7 | Transfer ₹3000 to account 1002 | ✔ Both balances updated correctly |
| 8 | Transfer to account 9999 (nonexistent) | `[!] Recipient account '9999' not found` |
| 9 | Transfer to own account (1001 → 1001) | `[!] Cannot transfer to your own account` |
| 10 | View history after multiple operations | All transactions listed in order |
| 11 | Enter `abc` at menu prompt | `[!] Invalid choice` — no crash |
| 12 | Verify balance consistency after transfer | Sender decremented, recipient credited |
| 13 | Select option 5 (Quit) | Goodbye message, clean exit |

---

## 🏗 OOP Concepts Used

### `Transaction.java`
- **Encapsulation**: All fields are `private final` with read-only getters.
- Immutable record — once created, a transaction cannot be altered.

### `Account.java`
- **Encapsulation**: Balance can only change via `deposit()`, `withdraw()`, `creditTransfer()`, `debitTransfer()`.
- **Abstraction**: Callers do not manipulate the balance field directly.

### `Bank.java`
- **Abstraction**: The ATM does not iterate accounts directly; it asks the Bank.
- Coordinates atomic transfers between two Account instances.

### `ATM.java`
- **Abstraction**: `Main` simply calls `atm.start()` without knowing session details.
- Handles all user interaction and delegates business logic to `Bank` / `Account`.

### `Main.java`
- Acts as the **composition root** — wires up `Bank`, sample `Account` objects, and `ATM`.

---

## 📄 Resume / Internship Description

> **ATM Interface Console Application** — Core Java, OOP  
> Developed a fully functional ATM simulation demonstrating object-oriented design with five
> collaborating classes. Implemented secure authentication with lockout, monetary operations
> (withdraw, deposit, inter-account transfer), and in-session transaction history. Used
> `BigDecimal` for precision arithmetic, `ArrayList` for history storage, and comprehensive
> input validation to prevent crashes and invalid state. All code follows Java naming
> conventions with clear separation of responsibilities.

---

*Built for Java Development Internship Submission — Core Java, OOP, Collections, Exception Handling*

