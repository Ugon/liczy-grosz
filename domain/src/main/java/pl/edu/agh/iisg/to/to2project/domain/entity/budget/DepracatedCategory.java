package pl.edu.agh.iisg.to.to2project.domain.entity.budget;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Patryk Skalski on 21/12/15.
 */
public class DepracatedCategory {
    private StringProperty name;
    private boolean isSpending;
    private DoubleProperty planValue;
    private DoubleProperty transactionsValue;
    private List<DepracatedCategory> subcategories;

    public DepracatedCategory(String name,boolean isSpending,double planValue, double transactionValue){
        this.name = new SimpleStringProperty(name);
        this.planValue = new SimpleDoubleProperty(planValue);
        this.transactionsValue = new SimpleDoubleProperty(transactionValue);
        this.isSpending = isSpending;
        this.subcategories = new LinkedList<DepracatedCategory>();

    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean isSpending() {
        return isSpending;
    }

    public void setSpending(boolean isSpending) {
        this.isSpending = isSpending;
    }

    public double getPlanValue() {
        return planValue.get();
    }

    public DoubleProperty planValueProperty() {
        return planValue;
    }

    public void setPlanValue(double planValue) {
        this.planValue.set(planValue);
    }

    public double getTransactionsValue() {
        return transactionsValue.get();
    }

    public DoubleProperty transactionsValueProperty() {
        return transactionsValue;
    }

    public void setTransactionsValue(double transactionsValue) {
        this.transactionsValue.set(transactionsValue);
    }

    public List<DepracatedCategory> getSubcategories() {
        return subcategories;
    }

    public void addChildren(DepracatedCategory child) {
        this.subcategories.add(child);
    }

    public boolean hasChildren(){
        return  !this.subcategories.isEmpty();
    }



}
