package pl.edu.agh.iisg.to.to2project.domain.entity;

import com.google.common.base.Preconditions;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicObservableValue;
import org.joda.time.DateTime;
import pl.edu.agh.iisg.to.to2project.domain.IExternalTransaction;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Wojciech Pachuta.
 * @author Bartłomiej Grochal.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"source", "destinationAccount", "delta", "dateTime"}))
public class ExternalTransaction extends AbstractTransaction implements IExternalTransaction {

    @Column(name = "source", nullable = false)
    protected String sourcePOJO;

    @Transient
    private final StringProperty source;

    ExternalTransaction() {
        super();
        this.source = new SimpleStringProperty();
    }

    public ExternalTransaction(String source, Account destinationAccount, BigDecimal delta, DateTime dateTime) {
        super(destinationAccount, delta, dateTime);
        this.source = new SimpleStringProperty();
        setSource(source);
    }

    @Override
    @PostLoad
    protected void initProperties(){
        super.initProperties();
        source.setValue(sourcePOJO);
    }

    @Override
    @PrePersist
    @PreUpdate
    protected void updatePOJOs() {
        super.updatePOJOs();
        sourcePOJO = source.get();
    }

    @Override
    protected void updateDestinationAccounts(Account oldDestinationAccount, Account newDestinationAccount) {
        if(oldDestinationAccount != null) {
            oldDestinationAccount.removeAsExternalTransactionDestination(this);
        }
        newDestinationAccount.addAsExternalTransactionDestination(this);
    }


    public void setSource(String source) {
        Preconditions.checkNotNull(source);
        this.source.set(source);
    }

    @Override
    public ReadOnlyStringProperty sourcePayeeProperty() {
        return source;
    }


    @Override
    protected void updateCategorySet(Category oldCategory, Category newCategory) {
        if(oldCategory != null) {
            oldCategory.removeExternalTransaction(this);
        }
        newCategory.addExternalTransaction(this);
    }

    @Override
    protected void updateCategoryRemove(Category removedCategory) {
        if(removedCategory != null) {
            removedCategory.removeExternalTransaction(this);
        }
    }

    @Override
    public MonadicObservableValue<String> sourcePropertyAsMonadicString() {
        return EasyBind.monadic(source);
    }

}
