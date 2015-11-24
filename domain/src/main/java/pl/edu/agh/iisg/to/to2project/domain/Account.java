package pl.edu.agh.iisg.to.to2project.domain;

import com.google.common.base.Preconditions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Wojciech Pachuta.
 */
@Entity
@Table
public class Account extends AbstractEntity{
    @Column
    private String name;

    @Column
    private BigDecimal balance;

//    private Set<Transaction> transactionHistory;

    Account() {
    }

    public Account(String name, BigDecimal balance) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        Preconditions.checkNotNull(balance);
        this.name = name;
        this.balance = balance;
//        this.transactionHistory = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void addMoney(BigDecimal delta){
        Preconditions.checkNotNull(delta);
        Preconditions.checkArgument(delta.compareTo(BigDecimal.ZERO) >= 0);
        balance = balance.add(delta);
    }

    public void subtractMoney(BigDecimal delta){
        Preconditions.checkNotNull(delta);
        Preconditions.checkArgument(delta.compareTo(BigDecimal.ZERO) >= 0);
        balance = balance.subtract(delta);
    }

//    public Set<Transaction> getTransactionHistory() {
//        return Collections.unmodifiableSet(transactionHistory);
//    }
//
//    public boolean addTransaction(Transaction transaction){
//        Preconditions.checkNotNull(transaction);
//        return transactionHistory.add(transaction);
//    }
//
//    public boolean removeTransaction(Transaction transaction){
//        Preconditions.checkNotNull(transaction);
//        return transactionHistory.remove(transaction);
//    }
}
