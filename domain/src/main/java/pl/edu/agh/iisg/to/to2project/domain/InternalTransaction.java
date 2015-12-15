package pl.edu.agh.iisg.to.to2project.domain;

import com.google.common.base.Preconditions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicObservableValue;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Wojciech Pachuta.
 * @author Bartłomiej Grochal.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"sourceAccount", "destinationAccount", "delta", "dateTime"}))
public class InternalTransaction extends AbstractTransaction {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sourceAccount")
    protected Account sourceAccountEntity;

    @Transient
    private final ObjectProperty<Account> sourceAccount;

    @Transient
    private final MonadicObservableValue<String> sourceMonadicString;

    InternalTransaction() {
        super();
        this.sourceAccount = new SimpleObjectProperty<>();
        this.sourceMonadicString = EasyBind.map(sourceAccount, elem -> elem.nameProperty().get());
    }

    public InternalTransaction(Account sourceAccount, Account destinationAccount, BigDecimal delta, DateTime dateTime) {
        super(destinationAccount, delta, dateTime);
        this.sourceAccount = new SimpleObjectProperty<>();
        this.sourceMonadicString = EasyBind.map(this.sourceAccount, elem -> elem.nameProperty().get());
        setSourceAccount(sourceAccount);
    }

    @Override
    @PostLoad
    protected void initProperties() {
        super.initProperties();
        sourceAccount.setValue(sourceAccountEntity);
    }

    @Override
    @PrePersist
    @PreUpdate
    protected void updatePOJOs() {
        super.updatePOJOs();
        sourceAccountEntity = sourceAccount.get();
    }

    @Override
    protected void updateDestinationAccounts(Account oldDestinationAccount, Account newDestinationAccount) {
        if(oldDestinationAccount != null) {
            oldDestinationAccount.removeAsInternalTransactionDestination(this);
        }
        newDestinationAccount.addAsInternalTransactionDestination(this);
    }


    public void setSourceAccount(Account sourceAccount) {
        Preconditions.checkNotNull(sourceAccount);

        Account oldSourceAccount = this.sourceAccount.get();
        this.sourceAccount.set(sourceAccount);

        if(oldSourceAccount != null){
            this.sourceAccount.get().removeAsInternalTransactionSource(this);
        }
        sourceAccount.addAsInternalTransactionSource(this);
    }

    public ReadOnlyObjectProperty<Account> sourceAccountProperty() {
        return sourceAccount;
    }


    @Override
    protected void updateCategorySet(Category oldCategory, Category newCategory) {
        if(oldCategory != null) {
            oldCategory.removeInternalTransaction(this);
        }
        newCategory.addInternalTransaction(this);
    }

    @Override
    protected void updateCategoryRemove(Category removedCategory) {
        if(removedCategory != null) {
            removedCategory.removeInternalTransaction(this);
        }
    }

    @Override
    public MonadicObservableValue<String> sourcePropertyAsMonadicString() {
        return this.sourceMonadicString;
    }

}
