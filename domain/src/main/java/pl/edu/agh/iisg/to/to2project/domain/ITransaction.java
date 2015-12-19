package pl.edu.agh.iisg.to.to2project.domain;

import javafx.beans.property.ReadOnlyObjectProperty;
import org.fxmisc.easybind.monadic.MonadicObservableValue;
import org.joda.time.LocalDate;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;

import java.math.BigDecimal;

/**
 * @author Wojciech Pachuta.
 */
public interface ITransaction {

    ReadOnlyObjectProperty<Account> destinationAccountProperty();

    MonadicObservableValue<BigDecimal> deltaProperty();

    ReadOnlyObjectProperty<LocalDate> dateProperty();

    MonadicObservableValue<Category> categoryMonadicProperty();

    MonadicObservableValue<String> commentMonadicProperty();

    MonadicObservableValue<String> sourcePropertyAsMonadicString();

    MonadicObservableValue<BigDecimal> accountBalanceAfterThisTransaction();

}
