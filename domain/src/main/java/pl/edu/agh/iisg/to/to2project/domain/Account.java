package pl.edu.agh.iisg.to.to2project.domain;

import com.google.common.base.Preconditions;
import javafx.beans.property.*;
import javafx.collections.ObservableSet;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

/**
 * @author Wojciech Pachuta.
 */
@Entity
@Table
@Access(AccessType.PROPERTY)
public class Account extends AbstractEntity{

    private final StringProperty name;

    private final ObjectProperty<BigDecimal> initialBalance;

    private final ObservableSet<InternalTransaction> internalTransactionHistory;

    private final ObservableSet<ExternalTransaction> externalTransactionHistory;

    Account() {
        super();
        this.name = new SimpleStringProperty();
        this.initialBalance = new SimpleObjectProperty<>();
        this.internalTransactionHistory = new SimpleSetProperty<>();
        this.externalTransactionHistory = new SimpleSetProperty<>();
    }

    public Account(String name, BigDecimal balance) {
        this();
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        Preconditions.checkNotNull(balance);
        setName(name);
        this.initialBalance.set(balance);
    }



    @Column(nullable = false, unique = true)
    private String getName() {
        return name.get();
    }

    public void setName(String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }



    @Column(nullable = false)
    private BigDecimal getInitialBalance() {
        return initialBalance.get();
    }

    public void setInitialBalance(BigDecimal initialBalance){
        Preconditions.checkNotNull(initialBalance);
        Preconditions.checkArgument(initialBalance.compareTo(BigDecimal.ZERO) >= 0);
        this.initialBalance.set(initialBalance);
    }

    public ObjectProperty<BigDecimal> initialBalanceProperty() {
        return this.initialBalance;
    }



    @OneToMany(mappedBy = "destinationAccount")
    private Set<InternalTransaction> getInternalTransactionHistory() {
        return Collections.unmodifiableSet(internalTransactionHistory);
    }

    private void setInternalTransactionHistory(Set<InternalTransaction> internalTransactionHistory){
        this.internalTransactionHistory.addAll(internalTransactionHistory);
    }

    public boolean addInternalTransaction(InternalTransaction internalTransaction){
        Preconditions.checkNotNull(internalTransaction);
        return internalTransactionHistory.add(internalTransaction);
    }

    public boolean removeInternalTransaction(InternalTransaction internalTransaction){
        Preconditions.checkNotNull(internalTransaction);
        return internalTransactionHistory.remove(internalTransaction);
    }

    public ObservableSet<InternalTransaction> internalTransactionHistoryObservableSet() {
        return internalTransactionHistory;
    }



    @OneToMany(mappedBy = "destinationAccount")
    private Set<ExternalTransaction> getExternalTransactionHistory() {
        return Collections.unmodifiableSet(externalTransactionHistory);
    }

    private void setExternalTransactionHistory(Set<ExternalTransaction> externalTransactionHistory){
        this.externalTransactionHistory.addAll(externalTransactionHistory);
    }

    public boolean addExternalTransaction(ExternalTransaction externalTransaction){
        Preconditions.checkNotNull(externalTransaction);
        return externalTransactionHistory.add(externalTransaction);
    }

    public boolean removeExternalTransaction(ExternalTransaction internalTransaction){
        Preconditions.checkNotNull(internalTransaction);
        return externalTransactionHistory.remove(internalTransaction);
    }

    public ObservableSet<ExternalTransaction> externalTransactionHistoryObservableSet() {
        return externalTransactionHistory;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Account account = (Account) o;

        return name.equals(account.name);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name.getValue();
    }
}
