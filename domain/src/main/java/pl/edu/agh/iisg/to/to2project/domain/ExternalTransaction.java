package pl.edu.agh.iisg.to.to2project.domain;

import com.google.common.base.Preconditions;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Wojciech Pachuta.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"source", "destinationAccount_id", "delta", "dateTime"}))
@Access(AccessType.PROPERTY)
public class ExternalTransaction extends AbstractTransaction {

    private final StringProperty source;

    ExternalTransaction() {
        super();
        this.source = new SimpleStringProperty();
    }

    public ExternalTransaction(String source, Account destinationAccount, BigDecimal delta, DateTime dateTime) {
        super(destinationAccount, delta, dateTime);
        this.source = new SimpleStringProperty(source);
    }



    @Column(nullable = false)
    public String getSource() {
        return source.get();
    }

    public void setSource(String source) {
        Preconditions.checkNotNull(source);
        this.source.set(source);
    }

    public StringProperty sourceProperty() {
        return source;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ExternalTransaction that = (ExternalTransaction) o;

        return source.equals(that.source);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + source.hashCode();
        return result;
    }
}
