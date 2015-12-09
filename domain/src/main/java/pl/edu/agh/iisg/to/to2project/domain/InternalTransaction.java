package pl.edu.agh.iisg.to.to2project.domain;

import com.google.common.base.Preconditions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Wojciech Pachuta.
 * @author Bartłomiej Grochal.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"sourceAccount_id", "destinationAccount_id", "delta", "dateTime"}))
@Access(AccessType.PROPERTY)
public class InternalTransaction extends AbstractTransaction {

    private final ObjectProperty<Account> sourceAccount;

    InternalTransaction() {
        super();
        this.sourceAccount = new SimpleObjectProperty<>();
    }

    public InternalTransaction(Account sourceAccount, Account destinationAccount, BigDecimal delta, DateTime dateTime) {
        super(destinationAccount, delta, dateTime);
        this.sourceAccount = new SimpleObjectProperty<>(sourceAccount);
    }



    @ManyToOne(fetch = FetchType.EAGER)
    public Account getSourceAccount() {
        return sourceAccount.get();
    }

    public void setSourceAccount(Account sourceAccount) {
        Preconditions.checkNotNull(sourceAccount);
        this.sourceAccount.set(sourceAccount);
    }

    public ObjectProperty<Account> sourceAccountProperty() {
        return sourceAccount;
    }




    @Override
    @Transient
    public void setCategory(Category category) {
        Preconditions.checkNotNull(category);
        Preconditions.checkState(categoryProperty().get() == null);
        category.addInternalTransaction(this);
        this.category.set(category);
    }

    @Override
    public void removeCategoryIfPresent() {
        Category category = this.category.get();
        if(category != null) {
            category.removeInternalTransaction(this);
            this.category.set(null);
        }
    }



    @Override
    public StringProperty sourcePropertyAsString() {
        return sourceAccount.get().nameProperty();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        InternalTransaction that = (InternalTransaction) o;

        return sourceAccount.equals(that.sourceAccount);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + sourceAccount.hashCode();
        return result;
    }
}
