package app.business.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Data-Transfer-Object reflcting a single payment within the list of 'transactions' in a
 * 'paygroup' JSON Document in the MongoDB Collection 'paygroups'.
 *
 * @see Paymentgroup
 */
public class Payment {
    private String user;
    private BigDecimal amount;
    private Date date;

    public Payment() {}

    public Payment(String user, BigDecimal amount, Date date) {
        this.user = user;
        this.amount = amount;
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
