package pl.edu.agh.iisg.to.to2project.domain;

import com.google.common.base.Preconditions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Wojciech Pachuta.
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
        category.addInternalTransaction(this);
        this.category.set(Optional.of(category));
    }

    @Override
    public void removeCategory() {
        if(this.category.get().isPresent()){
            Category category = this.category.get().get();
            category.removeInternalTransaction(this);
            this.category.set(Optional.empty());
        }
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
