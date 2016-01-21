package pl.edu.agh.iisg.to.to2project.budget;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;

import java.util.List;

/**
 * Created by root on 22/11/15.
 */
public class DisplayedItem extends TreeItem<DisplayedItem> {

    // change category to Category not ObjProperty
    private ObjectProperty<Category> category;
    private DoubleProperty m_pPlan;
    private DoubleProperty m_pTransactions;

    private DoubleBinding m_bPlanSum;
    private DoubleBinding m_bTransactionsSum;
    private DoubleBinding m_bBalance;
    private DoubleBinding m_bProgressBarState;

    private boolean m_isSpending;


    public DisplayedItem(String categoryName, double transactionValue, double planValue, boolean isSpending) {
        this(new Category(categoryName), transactionValue, planValue, isSpending, true);
    }

    public DisplayedItem(Category category,double transactionValue, double planValue) {
        this(category, transactionValue, planValue, transactionValue < 0, !category.subCategoriesObservableSet().isEmpty());
    }

    public DisplayedItem(Category category, double transactionValue, double planValue, boolean isSpending, boolean hasChildren) {
        m_isSpending = isSpending;
        m_pPlan = new SimpleDoubleProperty(planValue);
        m_pTransactions = new SimpleDoubleProperty(transactionValue);
        this.category = new SimpleObjectProperty<Category>(category);
        m_bPlanSum = Bindings.add(0.0, new SimpleDoubleProperty(0.0));
        m_bTransactionsSum = Bindings.add(0.0, new SimpleDoubleProperty(0.0));

        if (!hasChildren) {
            if (!isSpending())
                m_bBalance = m_pTransactions.subtract(m_pPlan);
            else
                m_bBalance =  m_pPlan.subtract(m_pTransactions);
            m_bProgressBarState = m_pTransactions.divide(m_pPlan);
        }
        else {
            if(!isSpending())
                m_bBalance = m_bTransactionsSum.subtract(m_bPlanSum);
            else
                m_bBalance = m_bPlanSum.subtract(m_bTransactionsSum);
            m_bProgressBarState = getTransactionsSumBinding().divide(getPlanSumBinding());
            m_bPlanSum = m_bPlanSum.add(this.getPlanProperty());
            m_bTransactionsSum = m_bTransactionsSum.add(m_pTransactions);
        }
    }

    public void addChild(DisplayedItem child)
    {
        if(child.isLeaf()) {
            m_bPlanSum = m_bPlanSum.add(child.getPlanProperty());
            m_bTransactionsSum = m_bTransactionsSum.add(child.getTransactionsValue());
        } else {
            m_bPlanSum = m_bPlanSum.add(child.getPlanSumBinding());
            m_bTransactionsSum = m_bTransactionsSum.add(child.getTransactionsSumBinding());
        }

        if(!isSpending())
            m_bBalance = m_bTransactionsSum.subtract(m_bPlanSum);
        else
            m_bBalance = m_bPlanSum.subtract(m_bTransactionsSum);

        this.m_bProgressBarState =  getTransactionsSumBinding().divide(getPlanSumBinding());
        super.getChildren().add(child);
    }

    public void addChildren(List<DisplayedItem> children)
    {
        for(DisplayedItem child: children)
            addChild(child);
    }



    public Category getCategory() {
        return category.get();
    }
    public String getCategoryName() {
        return category.get().nameProperty().get();
    }
    public ReadOnlyStringProperty getCategoryNameProperty() {
        return category.get().nameProperty();
    }
    public void setCategoryName(String name) {
        category.get().setName(name);
    }


    public double getPlanValue() {
        return m_pPlan.get();
    }
    public DoubleProperty getPlanProperty() {
        return m_pPlan;
    }
    public void setPlanValue(double planValue) {
        this.m_pPlan.setValue(planValue);
    }


    public double getTransactionsValue() {
        return m_pTransactions.get();
    }
    public DoubleProperty getTransactionsProperty() {
        return m_pTransactions;
    }
    public void setTransactionsValue(double transactionsValueProperty) {
        this.m_pTransactions.setValue(transactionsValueProperty);
    }


    public Double getPlanSumValue() {
        return m_bPlanSum.getValue();
    }
    public DoubleBinding getPlanSumBinding() {
        return m_bPlanSum;
    }


    public Double getTransactionsSumValue() {
        return m_bTransactionsSum.getValue();
    }
    public DoubleBinding getTransactionsSumBinding() {
        return m_bTransactionsSum;
    }


    public Double getBalanceValue() {
        return m_bBalance.getValue();
    }
    public DoubleBinding getBalanceBinding() {
        return m_bBalance;
    }


    public Double getProgressBarStateValue() {
        return m_bProgressBarState.get();
    }
    public DoubleBinding getProgressBarStateBinding() {
        return m_bProgressBarState;
    }


    public ObservableStringValue showPlanValueAsString()  {
        return m_pPlan.asString();
    }

    public ObservableValue showBalance()  {
        return (ObservableValue) this.m_bBalance;
    }

    public ObservableStringValue showPlanSumAsString() {
        if (isLeaf())
            return new SimpleStringProperty("-");
        else
            return m_bPlanSum.asString();
    }

    public ObservableStringValue showTransactionsSumAsString() {
        if (isLeaf())
            return new SimpleStringProperty("-");
        else
            return m_bTransactionsSum.asString();
    }


    public boolean isSpending() {
        return m_isSpending;
    }
    public void setIsSpending(boolean isSpending) {
        this.m_isSpending = isSpending;
    }
    public boolean isLeaf()  {
        return super.getChildren().isEmpty();
    }
    public boolean hasParent() {
        return super.getParent() != null;
    }
}
