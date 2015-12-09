package pl.edu.agh.iisg.to.to2project.domain;

import com.google.common.base.Preconditions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Wojciech Pachuta.
 * @author Bartłomiej Grochal.
 */
@MappedSuperclass
@Access(AccessType.PROPERTY)
public abstract class AbstractTransaction extends AbstractEntity {

    private final ObjectProperty<Account> destinationAccount;

    private final ObjectProperty<BigDecimal> delta;

    private final ObjectProperty<DateTime> dateTime;

    final ObjectProperty<Category> category;

    private final ObjectProperty<String> comment;

    AbstractTransaction() {
        this(null, null, null);
    }

    public AbstractTransaction(Account account, BigDecimal delta, DateTime dateTime) {
        this.destinationAccount = new SimpleObjectProperty<>(account);
        this.delta = new SimpleObjectProperty<>(delta);
        this.dateTime = new SimpleObjectProperty<>(dateTime);
        this.category = new SimpleObjectProperty<>();
        this.comment = new SimpleObjectProperty<>();
    }



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destinationAccount_id")
    private Account getDestinationAccount() {
        return destinationAccount.get();
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount.set(destinationAccount);
    }

    public ObjectProperty<Account> destinationAccountProperty() {
        return destinationAccount;
    }



    @Column(nullable = false)
    private BigDecimal getDelta() {
        return delta.get();
    }

    public void setDelta(BigDecimal delta) {
        this.delta.set(delta);
    }

    public ObjectProperty<BigDecimal> deltaProperty() {
        return delta;
    }



    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(nullable = false)
    private DateTime getDateTime() {
        return dateTime.get();
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime.set(dateTime);
    }

    public ObjectProperty<DateTime> dateTimeProperty() {
        return dateTime;
    }



    @ManyToOne(fetch = FetchType.EAGER)
    private Category getCategoryColumn() {
        return category.get();
    }

    private void setCategoryColumn(Category category) {
        this.category.set(category);
    }

    @Transient
    public abstract void setCategory(Category category);

    public abstract void removeCategoryIfPresent();

    public ObjectProperty<Category> categoryProperty() {
        return category;
    }



    @Column(name = "comment", nullable = true)
    private String getCommentHibernate() {
        return comment.get();
    }

    private void setCommentHibernate(String comment) {
        this.comment.set(comment);
    }

    @Transient
    public void setComment(String comment) {
        Preconditions.checkNotNull(comment);
        Preconditions.checkArgument(!comment.isEmpty());
        this.comment.set(comment);
    }

    public void removeCommentIfPresent() {
        this.comment.set(null);
    }

    public ObjectProperty<String> commentProperty() {
        return comment;
    }


    public abstract StringProperty sourcePropertyAsString();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AbstractTransaction that = (AbstractTransaction) o;

        if (!destinationAccount.equals(that.destinationAccount)) return false;
        if (!delta.equals(that.delta)) return false;
        return dateTime.equals(that.dateTime);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + destinationAccount.hashCode();
        result = 31 * result + delta.hashCode();
        result = 31 * result + dateTime.hashCode();
        return result;
    }
}
