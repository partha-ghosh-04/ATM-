package atm;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Application entry point.
 *
 * Responsibilities:
 *   1. Initialise the Bank and seed it with demo accounts.
 *   2. Create a shared Scanner for the entire session.
 *   3. Hand control to the ATM class.
 *   4. Close the Scanner on exit.
 */
public class Main {

    public static void main(String[] args) {

        // ── 1. Set up the bank ─────────────────────────────────────────────
        Bank bank = new Bank("JavaBank ATM");

        // Demo accounts — credentials are documented in README.md
        bank.addAccount(new Account("1001", "1234", "Alice Johnson",
                new BigDecimal("10000.00")));

        bank.addAccount(new Account("1002", "5678", "Bob Smith",
                new BigDecimal("5000.00")));

        bank.addAccount(new Account("1003", "9999", "Carol Williams",
                new BigDecimal("7500.00")));

        // ── 2. Shared Scanner (one instance avoids resource leaks) ─────────
        Scanner scanner = new Scanner(System.in);

        // ── 3. Start the ATM session ───────────────────────────────────────
        ATM atm = new ATM(bank, scanner);
        atm.start();

        // ── 4. Release resources ───────────────────────────────────────────
        scanner.close();
    }
}

