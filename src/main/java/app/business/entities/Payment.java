package app.business.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Payment {

    private Integer id = null;
    private String name = null;
    private BigDecimal amount = null;

    private List<Comment> comments = new ArrayList<Comment>();

    public Payment() {
        super();
    }

    public Payment(final Integer id, final String name, final BigDecimal amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public Integer getId() {
        return this.id;
    }
    public void setId(final Integer id) {
        this.id = id;
    }


    public String getName() {
        return this.name;
    }
    public void setName(final String name) {
        this.name = name;
    }


    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setPrice(final BigDecimal amount) {
        this.amount = amount;
    }

    public List<Comment> getComments() {
        return this.comments;
    }
}
