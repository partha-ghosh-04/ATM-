package atm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Acts as the central bank registry: stores all accounts, handles authentication,
 * and coordinates inter-account transfers atomically.
 *
 * OOP Concepts:
 *   Abstraction   — ATM delegates account look-up and transfer logic here.
 *   Encapsulation — the account list is private; external code cannot iterate it freely.
 */
public class Bank {

    private final String bankName;
    private final ArrayList<Account> accounts;

    public Bank(String bankName) {
        this.bankName = bankName;
        this.accounts = new ArrayList<>();
    }

    // ── Account management ─────────────────────────────────────────────────────

    /**
     * Registers a new account with the bank.
     *
     * @throws IllegalArgumentException if a duplicate userId already exists
     */
    public void addAccount(Account account) {
        if (findAccountById(account.getUserId()).isPresent()) {
            throw new IllegalArgumentException(
                    "An account with userId '" + account.getUserId() + "' already exists.");
        }
        accounts.add(account);
    }

    /**
     * Returns the Account with the given userId, or empty if not found.
     */
    public Optional<Account> findAccountById(String userId) {
        return accounts.stream()
                .filter(a -> a.getUserId().equals(userId))
                .findFirst();
    }

    // ── Authentication ─────────────────────────────────────────────────────────

    /**
     * Authenticates a user by userId + PIN.
     *
     * @return the matching Account, or empty if credentials are invalid
     */
    public Optional<Account> authenticate(String userId, String pin) {
        return findAccountById(userId)
                .filter(a -> a.validatePin(pin));
    }

    // ── Transfer ───────────────────────────────────────────────────────────────

    /**
     * Atomically transfers {@code amount} from {@code sender} to the account
     * identified by {@code recipientId}.
     *
     * Either both sides update or neither does (via exception propagation).
     *
     * @throws IllegalArgumentException if recipient does not exist or transfer is to self
     * @throws IllegalStateException    if sender has insufficient funds
     */
    public void transfer(Account sender, String recipientId, BigDecimal amount) {
        // Guard: recipient must exist
        Account recipient = findAccountById(recipientId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Recipient account '" + recipientId + "' not found."));

        // Guard: cannot transfer to self
        if (sender.getUserId().equals(recipientId)) {
            throw new IllegalArgumentException(
                    "Cannot transfer to your own account.");
        }

        // Guard: amount validity is checked inside debitTransfer (throws if invalid/insufficient)
        // Perform the atomic transfer
        String outDesc = "Transfer to Account " + recipientId;
        String inDesc  = "Transfer from Account " + sender.getUserId();

        // Step 1 — debit sender (throws IllegalStateException on insufficient funds)
        sender.debitTransfer(amount, outDesc);

        // Step 2 — credit recipient (only reached if Step 1 succeeded)
        recipient.creditTransfer(amount, inDesc);
    }

    // ── Accessors ──────────────────────────────────────────────────────────────

    public String getBankName() { return bankName; }

    /** Returns how many accounts are currently registered. */
    public int getAccountCount() { return accounts.size(); }
}

