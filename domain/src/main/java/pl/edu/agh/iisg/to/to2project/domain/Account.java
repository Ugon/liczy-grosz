package pl.edu.agh.iisg.to.to2project.domain;

import com.google.common.base.Preconditions;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

/**
 * @author Wojciech Pachuta.
 */
@Entity
@Table
public class Account extends AbstractEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String namePOJO;

    @Column(name = "initialBalance", nullable = false)
    private BigDecimal initialBalancePOJO;

    @OneToMany(mappedBy = "destinationAccountEntity")
    private Set<InternalTransaction> internalTransactionDestinationSetPOJO;

    @OneToMany(mappedBy = "destinationAccountEntity")
    private Set<ExternalTransaction> externalTransactionDestinationSetPOJO;

    @OneToMany(mappedBy = "destinationAccountEntity")
    private Set<InternalTransaction> internalTransactionSourceSetPOJO;

    @Transient
    private final StringProperty name;

    @Transient
    private final ObjectProperty<BigDecimal> initialBalance;

    @Transient
    private final ObservableSet<InternalTransaction> internalTransactionDestinationSet;

    @Transient
    private final ObservableSet<ExternalTransaction> externalTransactionDestinationSet;

    @Transient
    private final ObservableSet<InternalTransaction> internalTransactionSourceSet;

    Account() {
        super();
        this.name = new SimpleStringProperty();
        this.initialBalance = new SimpleObjectProperty<>();
        this.internalTransactionDestinationSet = FXCollections.observableSet();
        this.externalTransactionDestinationSet = FXCollections.observableSet();
        this.internalTransactionSourceSet = FXCollections.observableSet();
    }

    public Account(String name, BigDecimal balance) {
        this();
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        Preconditions.checkNotNull(balance);
        setName(name);
        setInitialBalance(balance);
    }

    @PostLoad
    private void initProperties(){
        name.set(namePOJO);
        initialBalance.setValue(initialBalancePOJO);
        internalTransactionDestinationSet.clear();
        internalTransactionDestinationSet.addAll(internalTransactionDestinationSetPOJO);
        externalTransactionDestinationSet.clear();
        externalTransactionDestinationSet.addAll(externalTransactionDestinationSetPOJO);
        internalTransactionSourceSet.clear();
        internalTransactionSourceSet.addAll(internalTransactionSourceSetPOJO);
    }

    @PrePersist
    @PreUpdate
    private void updatePOJOs(){
        namePOJO = name.get();
        initialBalancePOJO = initialBalance.get();
        if(internalTransactionDestinationSetPOJO == null) {
            internalTransactionDestinationSetPOJO = internalTransactionSourceSet;
        }
        else {
            internalTransactionDestinationSetPOJO.clear();
            internalTransactionDestinationSetPOJO.addAll(internalTransactionDestinationSet);
        }

        if(externalTransactionDestinationSetPOJO == null) {
            externalTransactionDestinationSetPOJO = externalTransactionDestinationSet;
        }
        else {
            externalTransactionDestinationSetPOJO.clear();
            externalTransactionDestinationSetPOJO.addAll(externalTransactionDestinationSet);
        }

        if(internalTransactionSourceSetPOJO == null) {
            internalTransactionSourceSetPOJO = internalTransactionSourceSet;
        }
        else {
            internalTransactionSourceSetPOJO.clear();
            internalTransactionSourceSetPOJO.addAll(internalTransactionSourceSet);
        }
    }


    public void setName(String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        this.name.set(name);
    }

    public ReadOnlyStringProperty nameProperty() {
        return name;
    }



    public void setInitialBalance(BigDecimal initialBalance){
        Preconditions.checkNotNull(initialBalance);
        Preconditions.checkArgument(initialBalance.compareTo(BigDecimal.ZERO) >= 0);
        this.initialBalance.set(initialBalance);
    }

    public ReadOnlyObjectProperty<BigDecimal> initialBalanceProperty() {
        return this.initialBalance;
    }



    void addAsInternalTransactionDestination(InternalTransaction transaction){
        internalTransactionDestinationSet.add(transaction);
    }

    void removeAsInternalTransactionDestination(InternalTransaction transaction){
        internalTransactionDestinationSet.remove(transaction);
    }

    void addAsExternalTransactionDestination(ExternalTransaction transaction){
        externalTransactionDestinationSet.add(transaction);
    }

    void removeAsExternalTransactionDestination(ExternalTransaction transaction){
        externalTransactionDestinationSet.remove(transaction);
    }

    void addAsInternalTransactionSource(InternalTransaction transaction){
        internalTransactionSourceSet.add(transaction);
    }

    void removeAsInternalTransactionSource(InternalTransaction transaction){
        internalTransactionSourceSet.remove(transaction);
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
