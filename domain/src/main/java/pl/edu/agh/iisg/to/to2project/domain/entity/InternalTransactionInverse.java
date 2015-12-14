package pl.edu.agh.iisg.to.to2project.domain.entity;

import javafx.beans.property.ReadOnlyObjectProperty;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicObservableValue;
import org.joda.time.DateTime;
import pl.edu.agh.iisg.to.to2project.domain.IInternalTransaction;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;

import java.math.BigDecimal;

/**
 * @author Wojciech Pachuta.
 */
public class InternalTransactionInverse implements IInternalTransaction {
    private final InternalTransaction internalTransaction;

    InternalTransactionInverse(InternalTransaction internalTransaction) {
        this.internalTransaction = internalTransaction;
    }

    public ReadOnlyObjectProperty<Account> destinationAccountProperty() {
        return internalTransaction.sourceAccountProperty();
    }

    public MonadicObservableValue<BigDecimal> deltaProperty() {
        return EasyBind.monadic(internalTransaction.deltaProperty()).map(BigDecimal::negate);
    }

    public ReadOnlyObjectProperty<DateTime> dateTimeProperty() {
        return internalTransaction.dateTimeProperty();
    }

    public MonadicObservableValue<Category> categoryMonadicProperty() {
        return internalTransaction.categoryMonadicProperty();
    }

    public MonadicObservableValue<String> commentMonadicProperty() {
        return internalTransaction.commentMonadicProperty();
    }

    public ReadOnlyObjectProperty<Account> sourceAccountProperty() {
        return internalTransaction.destinationAccountProperty();
    }

    public IInternalTransaction getTransactionInverse() {
        return internalTransaction;
    }

    public MonadicObservableValue<String> sourcePropertyAsMonadicString(){
        return EasyBind.monadic(internalTransaction.destinationAccountProperty()).flatMap(Account::nameProperty);
    }

}
