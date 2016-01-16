package pl.edu.agh.iisg.to.to2project.domain.entity.budget;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.*;

import static java.util.stream.Collectors.toCollection;

/**
 * Created by root on 9/12/15.
 */
public class Data {
    private static Data instance = null;

    private VBox earningVbox;
    private VBox spendingVbox;
    private DisplayedItem spendingDisplayedItemRoot;
    private DisplayedItem earningDisplayedItemRoot;
    private SimpleDoubleProperty availableResources;
    private int month;
    private int year;

    private Data() {
        this.availableResources = new SimpleDoubleProperty(DataGenerator.generateAvailableResources());
    }

    public static Data getInstance() {
        if(instance == null)
            instance = new Data();
        return instance;
    }

    public void setYearAndMonth(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public  void setVboxes(VBox earningVbox, VBox spendingVbox) {
        this.earningVbox = earningVbox;
        this.spendingVbox = spendingVbox;
    }


    public TreeItem<DisplayedItem> getSpendingDisplayedItemRoot() { return spendingDisplayedItemRoot; }
    public void setSpendingDisplayedItemRoot(DisplayedItem spendingDisplayedItemRoot) {
        this.spendingDisplayedItemRoot = spendingDisplayedItemRoot;
    }

    public TreeItem<DisplayedItem> getEarningDisplayedItemRoot() {
        return earningDisplayedItemRoot;
    }
    public void setEarningDisplayedItemRoot(DisplayedItem earningDisplayedItemRoot) {
        this.earningDisplayedItemRoot = earningDisplayedItemRoot;
    }

    public double getAvailaibleResources() { return availableResources.get(); }
    public ObservableValue getAvailableResourcesProperty() { return availableResources; }
    public void setAvailaibleResources(int availableResources) {
        this.availableResources.set(availableResources);
    }

    public ObservableValue getSummaryEarningsPlanColumn() { return earningDisplayedItemRoot.getPlanSumBinding(); }
    public ObservableValue getSummaryEarningsRealColumn() { return earningDisplayedItemRoot.getTransactionsSumBinding();}
    public ObservableValue getSummarySpendingPlanColumn() { return spendingDisplayedItemRoot.getPlanSumBinding(); }
    public ObservableValue getSummarySpendingRealColumn() { return spendingDisplayedItemRoot.getTransactionsSumBinding(); }
    public ObservableValue getSummarySpendingBalanceColumn() {
        return earningDisplayedItemRoot.getTransactionsSumBinding().subtract(spendingDisplayedItemRoot.getTransactionsSumBinding());
    }


   /** public DisplayedItem buildSpendingTree(VBox vbox)
    {
        DisplayedItem root = new DisplayedItem("Spending",0.0, 0.0, true);
        spendingDisplayedItemRoot = root;
        addTextField(root, vbox);
        ProgressBar progressBar = addProgressBar(vbox);
        root.addChild(buildTree(DataGenerator.generateSpendings(), vbox));
        setProgressBarProgress(root, progressBar);
        return root;
    }

    public DisplayedItem buildEarningTree(VBox vbox)
    {
        DisplayedItem root = new DisplayedItem("Earning",0.0, 0.0, false);
        earningDisplayedItemRoot = root;
        addTextField(root, vbox);
        ProgressBar progressBar = addProgressBar(vbox);
        root.addChild(buildTree(DataGenerator.generateEarnings(),vbox));
        setProgressBarProgress(root, progressBar);
        return root;
    }

    public DisplayedItem buildTree(Category category,VBox vbox){
        DisplayedItem root = new DisplayedItem(category,DataGenerator.getTransactionValueForCategory(category.nameProperty().get(),month,year),DataGenerator.getPlanValueForCategory(category.nameProperty().get(),month,year));
        addTextField(root, vbox);
        ProgressBar progressBar = addProgressBar(vbox);
        if (category.hasChildren()) {

            for (Category child : category.subCategoriesObservableSet())
            {
                DisplayedItem newChild = buildTree(child, vbox);
                root.addChild(newChild);
            }
        }
        setProgressBarProgress(root, progressBar);
        return root;
    }*/

    private boolean isOld() {
        YearMonth currentDate = YearMonth.now();
        return 12 * currentDate.getYear() + currentDate.getMonthValue() > 12 * year + month + 1;
    }

    private Map<String,List<DisplayedItem>> buildTrees(List<Category> list) {
        List<DisplayedItem> earningList = new ArrayList<>();
        List<DisplayedItem> spendingList = new ArrayList<>();

        Map<String,List<DisplayedItem>> map = new HashMap<>();
        map.put("earningList", earningList);
        map.put("spendingList", spendingList);

        for (Category category: list) {
            Set<ExternalTransaction> transactions = category.externalTransactionsObservableSet();
            List<ExternalTransaction> yearMonthTransactions = transactions.stream()
                    .filter(elem -> elem.dateProperty().get().getYear() == year &&
                            elem.dateProperty().get().getMonthOfYear() == month).collect(toCollection(ArrayList::new));

            BigDecimal earningTransactionsValue = yearMonthTransactions.stream().map(elem -> elem.deltaProperty().get())
                    .filter(elem -> elem.signum() > 0).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal spendingTransactionsValue = yearMonthTransactions.stream().map(elem -> elem.deltaProperty().get())
                    .filter(elem -> elem.signum() < 0).reduce(BigDecimal.ZERO, BigDecimal::add).negate();

            BigDecimal earningPlanValue = BigDecimal.ZERO;
            BigDecimal spendingPlanValue = BigDecimal.ZERO;
            try {
                Double d_earningplanValue = BudgetPersistenceManager.getPlannedEarningValueForMonth(category.nameProperty().get(), year, month);
                if (d_earningplanValue == null)
                {
                    earningPlanValue = BigDecimal.ZERO;
                } else
                {
                    earningPlanValue = BigDecimal.valueOf(d_earningplanValue);
                }
                Double d_spendingplanValue = BudgetPersistenceManager.getPlannedSpendingValueForMonth(category.nameProperty().get(), year, month);
                if (d_earningplanValue == null)
                {
                    spendingPlanValue = BigDecimal.ZERO;
                } else
                {
                    spendingPlanValue = BigDecimal.valueOf(d_spendingplanValue);
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

            LabeledProgressBar earningLabeledProgressBar = new LabeledProgressBar(earningVbox);
            LabeledProgressBar spendingLabeledProgressBar = new LabeledProgressBar(spendingVbox);

            Map<String,List<DisplayedItem>> childrenMap = null;
            if(!category.subCategoriesObservableSet().isEmpty()) {
                childrenMap = buildTrees(new ArrayList<>(category.subCategoriesObservableSet()));
            }
            boolean hasEarningChildren = childrenMap != null && !childrenMap.get("earningList").isEmpty();
            boolean hasSpendingChildren = childrenMap != null && !childrenMap.get("spendingList").isEmpty();

            if(!isOld() || !earningTransactionsValue.equals(BigDecimal.ZERO) || !earningPlanValue.equals(BigDecimal.ZERO) || hasEarningChildren) {
                DisplayedItem earningItem = new DisplayedItem(category, earningTransactionsValue.doubleValue(),
                        earningPlanValue.doubleValue(), false, hasEarningChildren);
                if(hasEarningChildren) {
                    earningItem.addChildren(childrenMap.get("earningList"));
                }
                earningList.add(earningItem);
                earningLabeledProgressBar.update(earningItem);
            }
            else {
                earningLabeledProgressBar.remove();
            }

            if(!isOld() || !spendingTransactionsValue.equals(BigDecimal.ZERO) || !spendingPlanValue.equals(BigDecimal.ZERO) || hasSpendingChildren) {
                DisplayedItem spendingItem = new DisplayedItem(category, spendingTransactionsValue.doubleValue(),
                        spendingPlanValue.doubleValue(), true, hasSpendingChildren);
                if (hasSpendingChildren) {
                    spendingItem.addChildren(childrenMap.get("spendingList"));
                }
                spendingList.add(spendingItem);
                spendingLabeledProgressBar.update(spendingItem);
            }
            else {
                spendingLabeledProgressBar.remove();
            }
        }
        return map;
    }


    public void build(List<Category> list) {
        LabeledProgressBar earningLabeledProgressBar = new LabeledProgressBar(earningVbox);
        LabeledProgressBar spendingLabeledProgressBar = new LabeledProgressBar(spendingVbox);

        list = list.stream().filter(elem -> elem.parentCategoryMonadicProperty().get() == null).collect(toCollection(ArrayList::new));

        Map<String, List<DisplayedItem>> childrenMap = buildTrees(list);

        earningDisplayedItemRoot = new DisplayedItem("Earning",0.0, 0.0, false);
        earningDisplayedItemRoot.addChildren(childrenMap.get("earningList"));
        earningLabeledProgressBar.update(earningDisplayedItemRoot);

        spendingDisplayedItemRoot = new DisplayedItem("Spending",0.0, 0.0, true);
        spendingDisplayedItemRoot.addChildren(childrenMap.get("spendingList"));
        spendingLabeledProgressBar.update(spendingDisplayedItemRoot);
    }
}
