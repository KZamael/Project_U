package app.business.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Data-Transfer-Object reflcting a 'paygroup' JSON Document in the MongoDB Collection 'paygroups'.
 * Simple Bean with getters an setters of properties mapped to underlying DB Document
 * <p>
 * example of Documents mapped by this POJO (note that 'transactions' is mapped via Payment Object):
 *
 * <pre>{@code
 * {
 *     "_id" : ObjectId("5dbe1ab3b27d8f7e7e247340"),
 *     "title" : "Simpson",
 *     "transactions" : [
 *         {
 *             "amount" : NumberDecimal("15.35"),
 *             "date" : ISODate("2019-10-03T21:02:00.000Z"),
 *             "user" : "Marge"
 *         },
 *         {
 *             "amount" : NumberDecimal("5.2"),
 *             "date" : ISODate("2019-10-03T21:02:00.000Z"),
 *             "user" : "Lisa"
 *         }
 *         {
 *             "amount" : NumberDecimal("5"),
 *             "date" : ISODate("2019-12-17T12:47:10.848Z"),
 *             "user" : "Homer"
 *         }
 *     ],
 *     "users" : [
 *         "Homer",
 *         "Marge",
 *         "Maggie",
 *         "Bart",
 *         "Lisa"
 *     ]
 * }
 * }
 * </pre>
 *
 * @see <a href="https://mongodb.github.io/mongo-java-driver/3.12/bson/pojos/">POJO section in mongo java driver docs</a>
 */
public class Paymentgroup {

    @BsonId
    private ObjectId id;
    private String title;
    private List<String> users = new ArrayList<>();
    private List<Payment> transactions = new ArrayList<>();

    public Paymentgroup() {}

    public Paymentgroup(String title, List<String> users, List<Payment> transactions) {
        this.title = title;
        this.users = users;
        this.transactions = transactions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<Payment> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Payment> transactions) {
        this.transactions = transactions;
    }

    public ObjectId getId() {
        return id;
    }
}
