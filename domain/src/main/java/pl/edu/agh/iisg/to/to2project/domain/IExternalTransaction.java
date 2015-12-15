package pl.edu.agh.iisg.to.to2project.domain;

import javafx.beans.property.ReadOnlyStringProperty;

/**
 * @author Wojciech Pachuta.
 */
public interface IExternalTransaction extends ITransaction {

    ReadOnlyStringProperty sourcePayeeProperty();

}
