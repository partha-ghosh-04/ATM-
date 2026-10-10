package atm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Controls the full ATM session: login flow, main menu, and all banking operations.
 *
 * OOP Concepts:
 *   Abstraction   — hides Scanner and Bank complexity from Main.
 *   Encapsulation — session state (logged-in account) is private.
 */
public class ATM {

    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final String SEPARATOR =
            "═══════════════════════════════════════════════════════════════";

    private final Bank    bank;
    private final Scanner scanner;
    private Account currentAccount; // null when no user is logged in

    public ATM(Bank bank, Scanner scanner) {
        this.bank    = bank;
        this.scanner = scanner;
    }

    // ── Entry point ────────────────────────────────────────────────────────────

    /**
     * Starts the ATM application. Displays the welcome screen and manages
     * the login → menu → logout cycle.
     */
    public void start() {
        printWelcome();
        boolean loggedIn = handleLogin();
        if (loggedIn) {
            showMainMenu();
        }
        printGoodbye();
    }

    // ── Login ──────────────────────────────────────────────────────────────────

    private void printWelcome() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("           Welcome to " + bank.getBankName());
        System.out.println("              Automated Teller Machine");
        System.out.println(SEPARATOR);
    }

    /**
     * Handles up to MAX_LOGIN_ATTEMPTS login attempts.
     *
     * @return true if authentication succeeded; false after max failures
     */
    private boolean handleLogin() {
        int attempts = 0;
        while (attempts < MAX_LOGIN_ATTEMPTS) {
            int remaining = MAX_LOGIN_ATTEMPTS - attempts;
            System.out.println("\n  Please log in. Attempts remaining: " + remaining);

            System.out.print("  Enter User ID : ");
            String userId = scanner.nextLine().trim();

            System.out.print("  Enter PIN     : ");
            String pin = scanner.nextLine().trim();

            if (userId.isEmpty() || pin.isEmpty()) {
                System.out.println("  [!] User ID and PIN cannot be empty.");
                attempts++;
                continue;
            }

            Optional<Account> result = bank.authenticate(userId, pin);
            if (result.isPresent()) {
                currentAccount = result.get();
                System.out.println("\n  ✔  Login successful! Welcome, "
                        + currentAccount.getAccountHolder() + ".");
                System.out.println(SEPARATOR);
                return true;
            } else {
                attempts++;
                System.out.println("  [!] Invalid User ID or PIN.");
            }
        }

        System.out.println("\n  ✘  Maximum login attempts exceeded. Access denied.");
        System.out.println("     Please visit your nearest branch for assistance.");
        return false;
    }

    // ── Main Menu ──────────────────────────────────────────────────────────────

    private void showMainMenu() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readIntChoice();
            switch (choice) {
                case 1 -> showTransactionHistory();
                case 2 -> handleWithdraw();
                case 3 -> handleDeposit();
                case 4 -> handleTransfer();
                case 5 -> {
                    running = false;
                }
                default -> System.out.println(
                        "  [!] Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n" + SEPARATOR);
        System.out.printf("  Account Holder : %s%n", currentAccount.getAccountHolder());
        System.out.printf("  User ID        : %s%n", currentAccount.getUserId());
        System.out.printf("  Current Balance: ₹%.2f%n", currentAccount.getBalance());
        System.out.println(SEPARATOR);
        System.out.println("  ATM Main Menu");
        System.out.println("  1. Transaction History");
        System.out.println("  2. Withdraw");
        System.out.println("  3. Deposit");
        System.out.println("  4. Transfer");
        System.out.println("  5. Quit");
        System.out.println(SEPARATOR);
        System.out.print("  Enter your choice: ");
    }

    // ── 1. Transaction History ─────────────────────────────────────────────────

    private void showTransactionHistory() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  Transaction History — " + currentAccount.getAccountHolder());
        System.out.println(SEPARATOR);

        List<Transaction> history = currentAccount.getTransactions();
        if (history.isEmpty()) {
            System.out.println("  No transactions found for this session.");
        } else {
            System.out.printf("  %-15s | %-20s | %-14s | %-12s | %s%n",
                    "Type", "Date & Time", "Amount (₹)", "Balance (₹)", "Description");
            System.out.println("  " + "─".repeat(90));
            for (Transaction t : history) {
                System.out.printf("  %-15s | %-20s | %,14.2f | %,12.2f | %s%n",
                        t.getType(),
                        t.getDateTime().format(
                                java.time.format.DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")),
                        t.getAmount(),
                        t.getBalanceAfter(),
                        t.getDescription());
            }
        }
        System.out.println(SEPARATOR);
    }

    // ── 2. Withdraw ────────────────────────────────────────────────────────────

    private void handleWithdraw() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  Withdrawal");
        System.out.println(SEPARATOR);
        System.out.printf("  Available Balance: ₹%.2f%n", currentAccount.getBalance());
        System.out.print("  Enter amount to withdraw: ₹");

        BigDecimal amount = readPositiveAmount();
        if (amount == null) return; // invalid input already reported

        try {
            currentAccount.withdraw(amount, "ATM Withdrawal");
            System.out.printf("%n  ✔  Withdrawal successful!%n");
            System.out.printf("     Withdrawn : ₹%.2f%n", amount);
            System.out.printf("     Remaining : ₹%.2f%n", currentAccount.getBalance());
        } catch (IllegalStateException e) {
            // "Insufficient Funds" message as specified
            System.out.println("\n  [!] " + e.getMessage());
        }
        System.out.println(SEPARATOR);
    }

    // ── 3. Deposit ─────────────────────────────────────────────────────────────

    private void handleDeposit() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  Deposit");
        System.out.println(SEPARATOR);
        System.out.print("  Enter amount to deposit: ₹");

        BigDecimal amount = readPositiveAmount();
        if (amount == null) return;

        currentAccount.deposit(amount, "ATM Deposit");
        System.out.printf("%n  ✔  Deposit successful!%n");
        System.out.printf("     Deposited   : ₹%.2f%n", amount);
        System.out.printf("     New Balance : ₹%.2f%n", currentAccount.getBalance());
        System.out.println(SEPARATOR);
    }

    // ── 4. Transfer ────────────────────────────────────────────────────────────

    private void handleTransfer() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  Fund Transfer");
        System.out.println(SEPARATOR);
        System.out.printf("  Available Balance: ₹%.2f%n", currentAccount.getBalance());
        System.out.print("  Enter recipient Account ID: ");
        String recipientId = scanner.nextLine().trim();

        if (recipientId.isEmpty()) {
            System.out.println("  [!] Recipient Account ID cannot be empty.");
            return;
        }

        System.out.print("  Enter amount to transfer: ₹");
        BigDecimal amount = readPositiveAmount();
        if (amount == null) return;

        try {
            bank.transfer(currentAccount, recipientId, amount);
            System.out.printf("%n  ✔  Transfer successful!%n");
            System.out.printf("     Transferred : ₹%.2f  →  Account %s%n", amount, recipientId);
            System.out.printf("     Remaining   : ₹%.2f%n", currentAccount.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("\n  [!] " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("\n  [!] " + e.getMessage());
        }
        System.out.println(SEPARATOR);
    }

    // ── Goodbye ────────────────────────────────────────────────────────────────

    private void printGoodbye() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  Thank you for using " + bank.getBankName() + ".");
        System.out.println("  Your session has ended. Have a great day!");
        System.out.println(SEPARATOR + "\n");
    }

    // ── Input helpers ──────────────────────────────────────────────────────────

    /**
     * Reads a single integer choice from the console.
     * Returns -1 if the input is non-numeric (so the default switch branch handles it).
     */
    private int readIntChoice() {
        String line = scanner.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1; // will fall through to default case in switch
        }
    }

    /**
     * Reads a positive monetary amount from the console using BigDecimal for precision.
     *
     * @return a valid positive BigDecimal, or null if input was invalid
     */
    private BigDecimal readPositiveAmount() {
        String line = scanner.nextLine().trim();
        try {
            BigDecimal amount = new BigDecimal(line);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("  [!] Amount must be greater than zero.");
                return null;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid amount. Please enter a numeric value (e.g., 500 or 1500.50).");
            return null;
        }
    }
}

