package pl.edu.agh.iisg.to.to2project.domain;

import javafx.beans.property.ReadOnlyObjectProperty;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;

/**
 * @author Wojciech Pachuta.
 */
public interface IInternalTransaction extends ITransaction {

    ReadOnlyObjectProperty<Account> sourceAccountProperty();

    IInternalTransaction getTransactionInverse();

}
