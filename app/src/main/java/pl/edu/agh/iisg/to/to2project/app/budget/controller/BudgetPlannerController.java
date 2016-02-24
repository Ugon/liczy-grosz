package pl.edu.agh.iisg.to.to2project.app.budget.controller;

import com.google.common.base.Preconditions;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.core.RootController;
import pl.edu.agh.iisg.to.to2project.app.expenses.categories.controller.EditCategoryPopupController;
import pl.edu.agh.iisg.to.to2project.app.expenses.categories.controller.NewCategoryPopupController;
import pl.edu.agh.iisg.to.to2project.app.expenses.categories.view.EditCategoryPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.categories.view.NewCategoryPopup;
import pl.edu.agh.iisg.to.to2project.app.stats.util.entity.calendar.DatePicker;
import pl.edu.agh.iisg.to.to2project.budget.BudgetContent;
import pl.edu.agh.iisg.to.to2project.budget.DisplayedItem;
import pl.edu.agh.iisg.to.to2project.budget_persistence.BudgetPersistenceManager;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;

import java.sql.SQLException;
import java.time.YearMonth;

@Controller
public class BudgetPlannerController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RootController rootController;

    private VBox spendingVbox = new VBox();

    private VBox earningVbox = new VBox();

    @Autowired
    private BudgetContent budgetContent;

    private YearMonth date = YearMonth.now();

    @FXML
    public DatePicker datePicker;

    @FXML
    public TreeTableView<DisplayedItem> earningTreeTableView;

    @FXML
    public TreeTableView<DisplayedItem> spendingTreeTableView;

    @FXML
    public TreeTableColumn<DisplayedItem, String> earningCategoryColumn;

    @FXML
    public TreeTableColumn<DisplayedItem, String> spendingCategoryColumn;

    @FXML
    public TreeTableColumn<DisplayedItem, String> earningPlanValueColumn;

    @FXML
    public TreeTableColumn<DisplayedItem, String> earningPlanSumColumn;

    @FXML
    public TreeTableColumn<DisplayedItem, Number> earningTransactionValueColumn;

    @FXML
    public TreeTableColumn<DisplayedItem, String> earningTransactionSumColumn;

    @FXML
    public TreeTableColumn<DisplayedItem, Number> earningBalanceColumn;

    @FXML
    public AnchorPane earningProgressBarPane;

    @FXML
    public TreeTableColumn<DisplayedItem, String> spendingPlanValueColumn;

    @FXML
    public TreeTableColumn<DisplayedItem, String> spendingPlanSumColumn;

    @FXML
    public TreeTableColumn<DisplayedItem, Number> spendingTransactionValueColumn;

    @FXML
    public TreeTableColumn<DisplayedItem, String> spendingTransactionSumColumn;

    @FXML
    public TreeTableColumn<DisplayedItem, Number> spendingBalanceColumn;

    @FXML
    public AnchorPane spendingProgressBarPane;

    @FXML
    public TableColumn<BudgetContent, Double> summaryAvailableResourcesColumn;

    @FXML
    public TableColumn<BudgetContent, ObservableValue> summaryEarningsPlanColumn;

    @FXML
    public TableColumn<BudgetContent, ObservableValue> summaryEarningsRealColumn;

    @FXML
    public TableColumn<BudgetContent, ObservableValue> summarySpendingPlanColumn;

    @FXML
    public TableColumn<BudgetContent, ObservableValue> summarySpendingRealColumn;

    @FXML
    public TableColumn<BudgetContent, ObservableValue>  summarySpendingsBalanceColumn;

    @FXML
    public Button addCategoryButton;

    @FXML
    public Button editCategoryButton;

    @FXML
    public TableView<BudgetContent> summaryTableView;

    @FXML
    private void handleAddCategoryAction(ActionEvent event) {
        NewCategoryPopup popup = context.getBean(NewCategoryPopup.class);
        NewCategoryPopupController controller = popup.getController();

        controller.addCategory();
    }

    @FXML
    private void handleEditCategoryAction(ActionEvent event) {
        //budgetService.getPlannedTransactions(LocalDate.of(2014, 1, 1), LocalDate.of(2017,12,1),categoryService.getList());
        EditCategoryPopup popup = context.getBean(EditCategoryPopup.class);
        EditCategoryPopupController controller = popup.getController();

        DisplayedItem displayedItem = (DisplayedItem) earningTreeTableView.getSelectionModel().getSelectedItem();
        if (displayedItem != null) {
            Category selectedCategory = displayedItem.getCategory();
            if (selectedCategory == null) {
                displayedItem = (DisplayedItem) spendingTreeTableView.getSelectionModel().getSelectedItem();
                selectedCategory = displayedItem.getCategory();
            }
            if (selectedCategory != null) {
                controller.editCategory(selectedCategory);
            }
        }
    }

    @FXML
    public void initialize() {
        initializeFE();
        initializeBE();
    }

    private void initializeFE() {
        earningTreeTableView.setEditable(true);
        spendingTreeTableView.setEditable(true);
        earningCategoryColumn.setCellFactory(TextFieldTreeTableCell.<DisplayedItem>forTreeTableColumn());
        earningCategoryColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<DisplayedItem, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<DisplayedItem, String> event) {
                String oldValue = event.getOldValue();
                String newValue = event.getNewValue();
                try {
                    if (!BudgetPersistenceManager.doesCategoryExist(newValue)) {
                        Preconditions.checkArgument(true);

                        DisplayedItem p = (DisplayedItem) event.getTreeTableView().getTreeItem(event.getTreeTablePosition().getRow());
                        Category category = p.getCategory();

                        p.setCategoryName(newValue);
                        category.setName(newValue);

                        try {
                            BudgetPersistenceManager.updateCategoryName(oldValue, newValue);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        categoryService.save(category);
                    } else
                    {
                        event.getTreeTableView().getColumns().get(0).setVisible(false);
                        event.getTreeTableView().getColumns().get(0).setVisible(true);
                    }
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        });
        earningCategoryColumn.setCellValueFactory(dataValue -> {
                    DisplayedItem p = (DisplayedItem) dataValue.getValue();
                    return p.getCategoryNameProperty();
                }
        );
        spendingCategoryColumn.setCellFactory(TextFieldTreeTableCell.<DisplayedItem>forTreeTableColumn());
        spendingCategoryColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<DisplayedItem, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<DisplayedItem, String> event) {
                String oldValue = event.getOldValue();
                String newValue = event.getNewValue();
                try {
                    if (!BudgetPersistenceManager.doesCategoryExist(newValue)) {
                        Preconditions.checkArgument(true);

                        DisplayedItem p = (DisplayedItem) event.getTreeTableView().getTreeItem(event.getTreeTablePosition().getRow());
                        Category category = p.getCategory();

                        p.setCategoryName(newValue);
                        category.setName(newValue);

                        try {
                            BudgetPersistenceManager.updateCategoryName(oldValue, newValue);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        categoryService.save(category);
                    } else
                    {
                        event.getTreeTableView().getColumns().get(0).setVisible(false);
                        event.getTreeTableView().getColumns().get(0).setVisible(true);
                    }
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        });
        spendingCategoryColumn.setCellValueFactory(dataValue -> {
                    DisplayedItem p = (DisplayedItem) dataValue.getValue();
                    return p.getCategoryNameProperty();
                }
        );
        earningPlanValueColumn.setCellFactory(TextFieldTreeTableCell.<DisplayedItem>forTreeTableColumn());
        earningPlanValueColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<DisplayedItem, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<DisplayedItem, String> event) {
                DisplayedItem displayedItem = (DisplayedItem) event.getTreeTableView().getTreeItem(event.getTreeTablePosition().getRow());
                displayedItem.setPlanValue(Double.parseDouble(event.getNewValue()));

                try {
                    BudgetPersistenceManager.updatePlannedEarningForMonth(displayedItem.getCategoryName(), date.getYear(), date.getMonthValue(), Double.parseDouble(event.getNewValue()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //updateEarningProgressBarValue(displayedItem);
            }
        });
        earningPlanValueColumn.setCellValueFactory(dataValue -> {
                    DisplayedItem p = (DisplayedItem) dataValue.getValue();
                    return p.showPlanValueAsString();
                }
        );
        spendingPlanValueColumn.setCellFactory(TextFieldTreeTableCell.<DisplayedItem>forTreeTableColumn());
        spendingPlanValueColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<DisplayedItem, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<DisplayedItem, String> event) {
                DisplayedItem displayedItem = (DisplayedItem) event.getTreeTableView().getTreeItem(event.getTreeTablePosition().getRow());
                displayedItem.setPlanValue(Double.parseDouble(event.getNewValue()));

                try {
                    BudgetPersistenceManager.updatePlannedSpendingForMonth(displayedItem.getCategoryName(), date.getYear(), date.getMonthValue() , Double.parseDouble(event.getNewValue()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //updateSpendingProgressBarValue(displayedItem);
            }
        });
        spendingPlanValueColumn.setCellValueFactory(dataValue -> {
                    DisplayedItem p = (DisplayedItem) dataValue.getValue();
                    return p.showPlanValueAsString();
                }
        );
        spendingBalanceColumn.setCellValueFactory(dataValue -> {
                    DisplayedItem p = (DisplayedItem) dataValue.getValue();
                    return p.showBalance();
                }
        );
        earningBalanceColumn.setCellValueFactory(dataValue -> {
                    DisplayedItem p = (DisplayedItem) dataValue.getValue();
                    return p.showBalance();
                }
        );
        earningPlanSumColumn.setCellValueFactory(dataValue -> {
                    DisplayedItem p = (DisplayedItem) dataValue.getValue();
                    return p.showPlanSumAsString();
                }
        );
        spendingPlanSumColumn.setCellValueFactory(dataValue -> {
                    DisplayedItem p = (DisplayedItem) dataValue.getValue();
                    return p.showPlanSumAsString();
                }
        );
        earningTransactionValueColumn.setCellValueFactory(dataValue -> {
                    DisplayedItem p = (DisplayedItem) dataValue.getValue();
                    return p.getTransactionsProperty();
                }
        );
        spendingTransactionValueColumn.setCellValueFactory(dataValue -> {
                    DisplayedItem p = (DisplayedItem) dataValue.getValue();
                    return p.getTransactionsProperty();
                }
        );

        earningTransactionSumColumn.setCellValueFactory(dataValue -> {
                    DisplayedItem p = (DisplayedItem) dataValue.getValue();
                    return p.showTransactionsSumAsString();
                }
        );

        spendingTransactionSumColumn.setCellValueFactory(dataValue -> {
                    DisplayedItem p = (DisplayedItem) dataValue.getValue();
                    return p.showTransactionsSumAsString();
                }
        );
        summaryAvailableResourcesColumn.setCellValueFactory(dataValue -> dataValue.getValue().getAvailableResourcesProperty());
        summaryEarningsPlanColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSummaryEarningsPlanColumn());
        summaryEarningsRealColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSummaryEarningsRealColumn());
        summarySpendingPlanColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSummarySpendingPlanColumn());
        summarySpendingRealColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSummarySpendingRealColumn());
        summarySpendingsBalanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSummarySpendingBalanceColumn());

        summaryTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        datePicker.isBudgetDatePicker(true);
        datePicker.getCalendarView().selectedDateProperty()
                .addListener(new InvalidationListener() {
                                 @Override
                                 public void invalidated(Observable observable) {
                                     int year = datePicker.getSelectedDate().getYear();
                                     int month = datePicker.getSelectedDate().getMonthValue();
                                     date = YearMonth.of(year, month);
                                     budgetContent.setYearAndMonth(year, month);
                                     refreshContent();
                                 }
                             }
                );
    }

    private void initializeBE() {
        budgetContent.setYearAndMonth(date.getYear(), date.getMonthValue());
        budgetContent.setVboxes(earningVbox, spendingVbox);
        refreshContent();
    }

    public void refreshContent() {
        if(!rootController.isBudgetTabActive())
            return;

        spendingVbox.getChildren().clear();
        earningVbox.getChildren().clear();

        budgetContent.build(categoryService.getList());

        final ObservableList<BudgetContent> dataList = FXCollections.observableArrayList(budgetContent);
        summaryTableView.getItems().clear();
        summaryTableView.setItems(dataList);

        spendingTreeTableView.setRoot(budgetContent.getSpendingDisplayedItemRoot());
        spendingTreeTableView.setShowRoot(false);
        earningTreeTableView.setRoot(budgetContent.getEarningDisplayedItemRoot());
        earningTreeTableView.setShowRoot(false);

        spendingProgressBarPane.getChildren().clear();
        earningProgressBarPane.getChildren().clear();
        spendingProgressBarPane.getChildren().addAll(spendingVbox);
        earningProgressBarPane.getChildren().addAll(earningVbox);
    }

}
