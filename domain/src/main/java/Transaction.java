import com.google.common.base.Preconditions;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Wojciech Pachuta.
 */
public class Transaction {
    private final Account account;
    private final BigDecimal delta;
    private final DateTime dateTime;
    private Optional<Category> category;
    private Optional<String> comment;

    Transaction(){
        this.account = null;
        this.delta = null;
        this.dateTime = null;
    }

    public Transaction(Account account, BigDecimal delta, DateTime dateTime) {
        this.account = account;
        this.delta = delta;
        this.dateTime = dateTime;
    }

    public Account getAccount() {
        return account;
    }

    public BigDecimal getDelta() {
        return delta;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public Optional<Category> getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        Preconditions.checkNotNull(category);
        this.category = Optional.of(category);
    }

    public void removeCategoty(){
        this.category = Optional.empty();
    }

    public Optional<String> getComment() {
        return comment;
    }

    public void setComment(String comment) {
        Preconditions.checkNotNull(comment);
        Preconditions.checkArgument(!comment.isEmpty());
        this.comment = Optional.of(comment);
    }

}
