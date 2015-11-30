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

    private final ObservableSet<Transaction> transactionHistory;

    Account() {
        super();
        this.name = new SimpleStringProperty(this, "name");
        this.initialBalance = new SimpleObjectProperty<>(this, "initialBalance");
        this.transactionHistory = new SimpleSetProperty<>(this, "transactionHistory");
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
    public String getName() {
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
    public BigDecimal getInitialBalance() {
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


//    @OneToMany(mappedBy = "account")
    @Transient
    public Set<Transaction> getTransactionHistory() {
        return Collections.unmodifiableSet(transactionHistory);
    }

    public boolean addTransaction(Transaction transaction){
        Preconditions.checkNotNull(transaction);
        return transactionHistory.add(transaction);
    }

    public boolean removeTransaction(Transaction transaction){
        Preconditions.checkNotNull(transaction);
        return transactionHistory.remove(transaction);
    }

    public ObservableSet<Transaction> transactionHistoryObservableSet() {
        return transactionHistory;
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
}
