package pl.edu.agh.iisg.to.to2project.domain.entity;

import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectProperty;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicObservableValue;
import org.joda.time.LocalDate;
import pl.edu.agh.iisg.to.to2project.domain.ExtractableEntity;
import pl.edu.agh.iisg.to.to2project.domain.IInternalTransaction;

import java.math.BigDecimal;

/**
 * @author Wojciech Pachuta.
 */
public class InternalTransactionInverse implements IInternalTransaction, ExtractableEntity {
    private final InternalTransaction internalTransaction;

    private final MonadicObservableValue<BigDecimal> destinationAccountBalanceAfterThisTransaction;

    InternalTransactionInverse(InternalTransaction internalTransaction) {
        this.internalTransaction = internalTransaction;
        this.destinationAccountBalanceAfterThisTransaction = EasyBind.monadic(destinationAccountProperty())
                .flatMap(acc -> acc.calculateBalanceAtInclusive(dateProperty()));
    }

    @Override
    public Observable[] extractObservables() {
        return new Observable[] {destinationAccountProperty(), deltaProperty(), dateProperty(),
                categoryMonadicProperty(), commentMonadicProperty(), sourcePropertyAsMonadicString(),
                destinationAccountBalanceAfterThisTransaction};
    }

    @Override
    public ReadOnlyObjectProperty<Account> destinationAccountProperty() {
        return internalTransaction.sourceAccountProperty();
    }

    @Override
    public MonadicObservableValue<BigDecimal> deltaProperty() {
        return EasyBind.monadic(internalTransaction.deltaProperty()).map(BigDecimal::negate);
    }

    public ReadOnlyObjectProperty<LocalDate> dateProperty() {
        return internalTransaction.dateProperty();
    }

    @Override
    public MonadicObservableValue<Category> categoryMonadicProperty() {
        return internalTransaction.categoryMonadicProperty();
    }

    @Override
    public MonadicObservableValue<String> commentMonadicProperty() {
        return internalTransaction.commentMonadicProperty();
    }

    @Override
    public ReadOnlyObjectProperty<Account> sourceAccountProperty() {
        return internalTransaction.destinationAccountProperty();
    }

    @Override
    public IInternalTransaction getTransactionInverse() {
        return internalTransaction;
    }

    @Override
    public MonadicObservableValue<String> sourcePropertyAsMonadicString(){
        return EasyBind.monadic(internalTransaction.destinationAccountProperty()).flatMap(Account::nameProperty);
    }

    @Override
    public MonadicObservableValue<BigDecimal> accountBalanceAfterThisTransaction() {
        return destinationAccountBalanceAfterThisTransaction;
    }

}
