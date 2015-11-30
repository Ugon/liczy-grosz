package pl.edu.agh.iisg.to.to2project.domain;

import com.google.common.base.Preconditions;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Wojciech Pachuta.
 */
@Entity
@Table
public class Transaction extends AbstractEntity {
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "accountId")
    private final Account account;

//    @Column(nullable = false)
    private final BigDecimal delta;

//    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    @Column(nullable = false)
    private final DateTime dateTime;

//    @OneToOne(optional = true, fetch = FetchType.EAGER)
    private Category category;

//    @Column(nullable = true)
    private String comment;

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

    @Transient
    public Account getAccount() {
        return account;
    }

    @Transient
    public BigDecimal getDelta() {
        return delta;
    }

    @Transient
    public DateTime getDateTime() {
        return dateTime;
    }

    @Transient
    public Optional<Category> getCategory() {
        return Optional.of(category);
    }

    public void setCategory(Category category) {
        Preconditions.checkNotNull(category);
        this.category = category;
    }

    public void removeCategoty(){
        this.category = null;
    }

    @Transient
    public Optional<String> getComment() {
        return Optional.ofNullable(comment);
    }

    public void setComment(String comment) {
        Preconditions.checkNotNull(comment);
        Preconditions.checkArgument(!comment.isEmpty());
        this.comment = comment;
    }

    public void removeComment(String comment) {
        this.comment = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Transaction that = (Transaction) o;

        if (!account.equals(that.account)) return false;
        if (!delta.equals(that.delta)) return false;
        return dateTime.equals(that.dateTime);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + account.hashCode();
        result = 31 * result + delta.hashCode();
        result = 31 * result + dateTime.hashCode();
        return result;
    }
}
