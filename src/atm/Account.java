package atm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a bank account belonging to one user.
 *
 * OOP Concepts:
 *   Encapsulation — private fields exposed only through controlled getters/setters.
 *   Abstraction  — callers work with deposit/withdraw methods, not raw field mutation.
 */
public class Account {

    private final String userId;
    private final String pin;          // stored in-memory only; never printed
    private final String accountHolder;
    private BigDecimal balance;

    // Full transaction history for this account
    private final ArrayList<Transaction> transactions;

    /**
     * Creates an Account with an initial balance.
     *
     * @param userId        unique identifier (e.g., "1001")
     * @param pin           numeric PIN (stored as String to allow leading zeros)
     * @param accountHolder display name of the account owner
     * @param initialBalance starting balance (must be >= 0)
     */
    public Account(String userId, String pin,
                   String accountHolder, BigDecimal initialBalance) {
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.userId         = userId;
        this.pin            = pin;
        this.accountHolder  = accountHolder;
        this.balance        = initialBalance.setScale(2, RoundingMode.HALF_UP);
        this.transactions   = new ArrayList<>();
    }

    // ── Authentication ─────────────────────────────────────────────────────────

    /**
     * Checks whether the supplied PIN matches this account's PIN.
     * The comparison is intentionally constant-time via equals().
     */
    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    // ── Balance operations ─────────────────────────────────────────────────────

    /**
     * Deposits a positive amount into this account and records the transaction.
     *
     * @param amount       amount to deposit (must be > 0)
     * @param description  human-readable note
     * @throws IllegalArgumentException if amount is zero or negative
     */
    public void deposit(BigDecimal amount, String description) {
        validateAmount(amount);
        balance = balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        transactions.add(new Transaction(
                Transaction.TYPE_DEPOSIT, amount, description, balance));
    }

    /**
     * Withdraws a positive amount from this account and records the transaction.
     *
     * @param amount       amount to withdraw (must be > 0 and <= balance)
     * @param description  human-readable note
     * @throws IllegalArgumentException if amount is invalid
     * @throws IllegalStateException    if balance is insufficient
     */
    public void withdraw(BigDecimal amount, String description) {
        validateAmount(amount);
        if (amount.compareTo(balance) > 0) {
            throw new IllegalStateException("Insufficient Funds");
        }
        balance = balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
        transactions.add(new Transaction(
                Transaction.TYPE_WITHDRAWAL, amount, description, balance));
    }

    /**
     * Records an incoming transfer credit without going through the normal
     * deposit path (avoids double-recording a DEPOSIT type entry).
     */
    public void creditTransfer(BigDecimal amount, String description) {
        validateAmount(amount);
        balance = balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        transactions.add(new Transaction(
                Transaction.TYPE_TRANSFER_IN, amount, description, balance));
    }

    /**
     * Records an outgoing transfer debit.
     * The caller (Bank) is responsible for the atomicity check.
     */
    public void debitTransfer(BigDecimal amount, String description) {
        validateAmount(amount);
        if (amount.compareTo(balance) > 0) {
            throw new IllegalStateException("Insufficient Funds");
        }
        balance = balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
        transactions.add(new Transaction(
                Transaction.TYPE_TRANSFER_OUT, amount, description, balance));
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero.");
        }
    }

    // ── Accessors ──────────────────────────────────────────────────────────────

    public String getUserId()          { return userId; }
    public String getAccountHolder()   { return accountHolder; }
    public BigDecimal getBalance()     { return balance; }

    /** Returns an unmodifiable view of the transaction list. */
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    /** Summary line (PIN is intentionally excluded). */
    @Override
    public String toString() {
        return String.format("Account[userId=%s, holder=%s, balance=%.2f]",
                userId, accountHolder, balance);
    }
}

