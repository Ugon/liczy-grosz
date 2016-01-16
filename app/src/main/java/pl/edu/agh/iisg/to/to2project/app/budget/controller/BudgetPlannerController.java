package pl.edu.agh.iisg.to.to2project.app.budget.controller;

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
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.budget.BudgetPersistenceManager;
import pl.edu.agh.iisg.to.to2project.domain.entity.budget.Data;
import pl.edu.agh.iisg.to.to2project.domain.entity.budget.DisplayedItem;
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

    private Data data;

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
    public TableColumn<Data, Double> summaryAvaiableResourcesColumn;

    @FXML
    public TableColumn<Data, ObservableValue> summaryEarningsPlanColumn;

    @FXML
    public TableColumn<Data, ObservableValue> summaryEarininsRealColumn;

    @FXML
    public TableColumn<Data, ObservableValue> summarySpendingsPlanColumn;

    @FXML
    public TableColumn<Data, ObservableValue> summarySpendingsRealColumn;

    @FXML
    public TableColumn<Data, ObservableValue>  summarySpendingsBalanceColumn;

    @FXML
    public Button addCategoryButton;

    @FXML
    public Button editCategoryButton;

    @FXML
    public TableView<Data> summaryTableView;

    @FXML
    private void handleAddCategoryAction(ActionEvent event) {
        NewCategoryPopup popup = context.getBean(NewCategoryPopup.class);
        NewCategoryPopupController controller = popup.getController();

        controller.addCategory();
    }

    @FXML
    private void handleEditCategoryAction(ActionEvent event) {
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
    public void initialize()
    {
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
                DisplayedItem p = (DisplayedItem) event.getTreeTableView().getTreeItem(event.getTreeTablePosition().getRow());
                p.setCategoryName(event.getNewValue());

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
                DisplayedItem p = (DisplayedItem) event.getTreeTableView().getTreeItem(event.getTreeTablePosition().getRow());
                p.setCategoryName(event.getNewValue());
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
                    BudgetPersistenceManager.updatePlannedEarningForMonth(displayedItem.getCategoryName(), date.getYear(), date.getMonthValue() , Double.parseDouble(event.getNewValue()));
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
        summaryAvaiableResourcesColumn.setCellValueFactory(dataValue -> dataValue.getValue().getAvailableResourcesProperty());
        summaryEarningsPlanColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSummaryEarningsPlanColumn());
        summaryEarininsRealColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSummaryEarningsRealColumn());
        summarySpendingsPlanColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSummarySpendingPlanColumn());
        summarySpendingsRealColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSummarySpendingRealColumn());
        summarySpendingsBalanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSummarySpendingBalanceColumn());

        datePicker.isBudgetDatePicker(true);
        datePicker.getCalendarView().selectedDateProperty()
                .addListener(new InvalidationListener() {
                                 @Override
                                 public void invalidated(Observable observable) {
                                     int year = datePicker.getSelectedDate().getYear();
                                     int month = datePicker.getSelectedDate().getMonthValue();
                                     date = YearMonth.of(year, month);
                                     data.setYearAndMonth(year, month);
                                     refreshContent();
                                 }
                             }
                );
    }

    private void initializeBE() {
        data = Data.getInstance();
        data.setYearAndMonth(date.getYear(), date.getMonthValue());
        data.setVboxes(earningVbox, spendingVbox);
        refreshContent();
    }

    public void refreshContent() {
        if(!rootController.isBudgetTabActive())
            return;

        spendingVbox.getChildren().clear();
        earningVbox.getChildren().clear();

        data.build(categoryService.getList());

        //data.buildEarningTree(earningVbox);
        //data.buildSpendingTree(spendingVbox);

        final ObservableList<Data> dataList = FXCollections.observableArrayList(data);
        summaryTableView.getItems().clear();
        summaryTableView.setItems(dataList);

        spendingTreeTableView.setRoot(data.getSpendingDisplayedItemRoot());
        spendingTreeTableView.setShowRoot(false);
        earningTreeTableView.setRoot(data.getEarningDisplayedItemRoot());
        earningTreeTableView.setShowRoot(false);

        spendingProgressBarPane.getChildren().clear();
        earningProgressBarPane.getChildren().clear();
        spendingProgressBarPane.getChildren().addAll(spendingVbox);
        earningProgressBarPane.getChildren().addAll(earningVbox);
    }

}
