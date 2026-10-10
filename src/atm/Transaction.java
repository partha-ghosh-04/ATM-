package atm;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single financial transaction.
 * Stores the type, amount, timestamp, description, and balance after the transaction.
 *
 * OOP Concept: Encapsulation — all fields are private with public read-only accessors.
 */
public class Transaction {

    // Predefined transaction types for consistency
    public static final String TYPE_DEPOSIT    = "DEPOSIT";
    public static final String TYPE_WITHDRAWAL = "WITHDRAWAL";
    public static final String TYPE_TRANSFER_OUT = "TRANSFER OUT";
    public static final String TYPE_TRANSFER_IN  = "TRANSFER IN";

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

    private final String        type;
    private final BigDecimal    amount;
    private final LocalDateTime dateTime;
    private final String        description;
    private final BigDecimal    balanceAfter; // balance of the account after this transaction

    /**
     * Constructs a Transaction record.
     *
     * @param type         one of the TYPE_* constants defined in this class
     * @param amount       positive monetary amount involved
     * @param description  human-readable description
     * @param balanceAfter account balance after the transaction completes
     */
    public Transaction(String type, BigDecimal amount,
                       String description, BigDecimal balanceAfter) {
        this.type         = type;
        this.amount       = amount;
        this.dateTime     = LocalDateTime.now();
        this.description  = description;
        this.balanceAfter = balanceAfter;
    }

    // ── Accessors ──────────────────────────────────────────────────────────────

    public String getType()         { return type; }
    public BigDecimal getAmount()   { return amount; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getDescription()  { return description; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }

    /**
     * Returns a formatted, single-line summary of this transaction.
     */
    @Override
    public String toString() {
        return String.format("  %-15s | %-20s | Amount: %10.2f | Balance: %10.2f | %s",
                type,
                dateTime.format(FORMATTER),
                amount,
                balanceAfter,
                description);
    }
}

