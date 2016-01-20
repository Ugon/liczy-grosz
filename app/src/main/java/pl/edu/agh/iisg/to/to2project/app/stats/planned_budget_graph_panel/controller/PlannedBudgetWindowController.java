package pl.edu.agh.iisg.to.to2project.app.stats.planned_budget_graph_panel.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.stats.util.AccountTreeProviderUtil;
import pl.edu.agh.iisg.to.to2project.app.stats.util.CategoryTreeProviderUtil;
import pl.edu.agh.iisg.to.to2project.app.stats.util.PropertiesUtil;
import pl.edu.agh.iisg.to.to2project.app.stats.util.entity.BorderedTitledPane;
import pl.edu.agh.iisg.to.to2project.app.stats.util.entity.calendar.DatePicker;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;
import pl.edu.agh.iisg.to.to2project.domain.entity.PlannedTransaction;
import pl.edu.agh.iisg.to.to2project.service.impl.BudgetServiceImpl;
import pl.edu.agh.iisg.to.to2project.service.impl.IBasicDataSourceImpl;
import pl.edu.agh.iisg.to.to2project.service.impl.InOutWindowMockImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Nesbite on 2015-11-26.
 */
@Controller
public class PlannedBudgetWindowController {
    @Autowired
    private IBasicDataSourceImpl mock;

    @Autowired
    private InOutWindowMockImpl mock2;

    @Autowired
    private CategoryTreeProviderUtil categoryTreeProviderUtil;

    @Autowired
    private AccountTreeProviderUtil accountTreeProviderUtil;
    @Autowired
    private BudgetServiceImpl budgetService;
    @FXML
    private LineChart lineChart;
    @FXML
    private ToggleGroup radioGroup;
    @FXML
    private RadioButton plannedRadio;
    @FXML
    private RadioButton realRadio;
    @FXML
    private BorderedTitledPane borderedTitledPaneOpcje;

    @FXML
    private TableView transactionsTable;

    @FXML private TableColumn dateColumn;
    @FXML private TableColumn valueColumn;
    @FXML private TableColumn sourceAccountColumn;
    @FXML private TableColumn destinationAccountColumn;
    @FXML private TableColumn categoryColumn;
    @FXML private TableColumn descriptionColumn;

    @FXML
    private TableView plannedTransactionsTable;

    @FXML private TableColumn plannedDateColumn;
    @FXML private TableColumn plannedValueColumn;
    @FXML private TableColumn plannedCategoryColumn;
    @FXML
    private GridPane optionPanelGridPane;

    private DatePicker fromDatePicker;
    private DatePicker toDatePicker;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private MenuButton accountsMenuButton;
    private MenuButton categoriesMenuButton;
    private ObservableList<Account> accountsList;
    private ObservableList<Category> categoriesList;
    private List<CategoryTreeProviderUtil.CategoryAndCheckMenuItem> map = new LinkedList<>();

//    private InOutWindowMock mock2 = InOutWindowMock.getInstance();


    private static BigDecimal sumTransactions(LocalDate dateFrom, LocalDate dateTo, Map<LocalDate, BigDecimal> transactions){
        return transactions.entrySet().stream()
                .filter(a -> a.getKey().isAfter(dateFrom.minusDays(1)))
                .filter(a -> a.getKey().isBefore(dateTo.plusDays(1)))
                .map(a -> a.getValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @FXML
    private void initialize() {
        fromDatePicker = new DatePicker();
        toDatePicker = new DatePicker();
        accountsMenuButton = new MenuButton();
        categoriesMenuButton = new MenuButton();
        accountsList = FXCollections.observableList(mock.getAccounts()); //expensesDataSource.getAccounts()
        categoriesList = FXCollections.observableList(mock.getCategories()); //expensesDataSource.getCategories()



        fromDatePicker.setIsFromDatePicker(true);
        toDatePicker.setIsFromDatePicker(false);
        fromDatePicker.setSelectedDate(LocalDate.of(2016, 1, 1));
        toDatePicker.setSelectedDate(LocalDate.of(2016, 6, 30));

        accountsMenuButton.setMinWidth(180);
        accountsMenuButton.setText(PropertiesUtil.ALL);
        categoriesMenuButton.setMinWidth(180);
        categoriesMenuButton.setText(PropertiesUtil.ALL);

        accountsMenuButton.setText(PropertiesUtil.ALL);
        accountsMenuButton.setDisable(true);
        this.dateFrom = fromDatePicker.getSelectedDate();
        this.dateTo = toDatePicker.getSelectedDate();

        accountsList.add(0, new Account(PropertiesUtil.ALL, BigDecimal.ZERO));

        accountsMenuButton.getItems().addAll(accountsList.stream().map(account -> {
            CheckMenuItem it = new CheckMenuItem(account.toString());
            addAccountsHandler(it, accountsMenuButton);
            return it;
        }).collect(Collectors.toList()));
        CheckMenuItem it2 = new CheckMenuItem(new Account(PropertiesUtil.ALL, BigDecimal.ZERO).toString());
        it2.setSelected(true);
        accountTreeProviderUtil.selectAccountsInMenu(accountsMenuButton, it2);

//        accountTreeProviderUtil.selectAccountsInMenu(accountsMenuButton, new CheckMenuItem(PropertiesUtil.ALL).setSelected(true));

        CheckMenuItem it = new CheckMenuItem(PropertiesUtil.ALL);
        it.setSelected(true);
        addCategoriesHandler(it, categoriesMenuButton);
        categoriesMenuButton.getItems().add(it);
        setCheckMenuItems(categoriesList, 0);

        optionPanelGridPane.add(providePane(PropertiesUtil.ACCOUNTS, accountsMenuButton), 0, 0);
        optionPanelGridPane.add(providePane(PropertiesUtil.CATEGORIES, categoriesMenuButton), 0, 1);
        optionPanelGridPane.add(providePane(PropertiesUtil.FROM, fromDatePicker), 0, 2);
        optionPanelGridPane.add(providePane(PropertiesUtil.TO, toDatePicker), 0, 3);

//        accounts.clear();
//        categories.clear();

        map.addAll(categoryTreeProviderUtil.init(categoriesMenuButton));

        categoryTreeProviderUtil.selectCategoriesInMenu(categoriesMenuButton, it, new ArrayList<CategoryTreeProviderUtil.CategoryAndCheckMenuItem>());


        initDatePickers();
        initOptionPanel();
        initRadioButtons();
        initTransactionsTable();
        initChart();

        borderedTitledPaneOpcje.setText(PropertiesUtil.OPTION_TITLE);
    }
    private Node providePane(String label, Node child) {
        GridPane result = new GridPane();
        result.setVgap(5);
        result.add(new Label(label), 0, 0);
        result.add(child, 0, 1);
        return result;
    }
    private List<CheckMenuItem> setCheckMenuItems(List<Category> categories, int level) {
        List<CheckMenuItem> result = new ArrayList<>();

        for (Category category : categories) {
            CheckMenuItem it = new CheckMenuItem(category.nameProperty().get());
            addCategoriesHandler(it, categoriesMenuButton);
            if (level == 0) {
                categoriesMenuButton.getItems().add(new SeparatorMenuItem());
            }
            categoriesMenuButton.getItems().add(it);

            if (!category.subCategoriesObservableSet().isEmpty()) {
                List<CheckMenuItem> subcategoryItems = setCheckMenuItems(new LinkedList<>(category.subCategoriesObservableSet()), level + 1);
                result.addAll(subcategoryItems);
            }
        }

        return result;
    }

    private void addAccountsHandler(MenuItem item, MenuButton button){
        item.addEventHandler(ActionEvent.ACTION, event -> {
            accountTreeProviderUtil.selectAccountsInMenu(button, (CheckMenuItem) event.getSource());
            button.setText(accountTreeProviderUtil.getButtonText(button));
            accountsList.clear();
            accountsList.addAll(accountTreeProviderUtil.getSelectedAccounts(button));
        });
    }

    private void addCategoriesHandler(MenuItem item, MenuButton button){
        item.addEventHandler(ActionEvent.ACTION, event -> {
            categoryTreeProviderUtil.selectCategoriesInMenu(button, (CheckMenuItem) event.getSource(), map);
            button.setText(categoryTreeProviderUtil.getButtonText(map));
            categoriesList.clear();
            categoriesList.addAll(categoryTreeProviderUtil.getSelectedCategories(map));
        });
    }
    private void initDatePickers() {

        fromDatePicker.selectedDateProperty().addListener(observable -> {
            if (fromDatePicker.getSelectedDate().isAfter(toDatePicker.getSelectedDate())) {
                dateFrom = toDatePicker.getSelectedDate().withDayOfMonth(1);
                fromDatePicker.setSelectedDate(dateFrom);
            } else {
                dateFrom = fromDatePicker.getSelectedDate();
            }
            initChart();
            initRadioButtons();
        });

        toDatePicker.selectedDateProperty().addListener(observable -> {
            if (toDatePicker.getSelectedDate().isBefore(fromDatePicker.getSelectedDate())) {
                dateTo = fromDatePicker.getSelectedDate().withDayOfMonth(fromDatePicker.getSelectedDate().lengthOfMonth());
                toDatePicker.setSelectedDate(dateTo);
            } else {
                dateTo = toDatePicker.getSelectedDate();
            }
            initChart();
            initRadioButtons();
        });

        if((fromDatePicker.getSelectedDate() == null) || (toDatePicker.getSelectedDate() == null)) {
            fromDatePicker.setSelectedDate(dateFrom);
            toDatePicker.setSelectedDate(dateTo);
        }
    }
    private void initOptionPanel() {
        accountsList.addListener(new ListChangeListener<Account>() {
            @Override
            public void onChanged(Change<? extends Account> c) {
                initChart();
                initRadioButtons();

            }
        });
        categoriesList.addListener(new ListChangeListener<Category>() {
            @Override
            public void onChanged(Change<? extends Category> c) {
                initChart();
                initRadioButtons();

            }
        });
    }
    private void initRadioButtons() {
        realRadio.selectedProperty().addListener(observable -> {
            initTransactionsTable();
//            initChart();
        });
        plannedRadio.selectedProperty().addListener(observable -> {
            initPlannedTransactionsTable();
//            initChart();
        });
        if(realRadio.isSelected()){
            initTransactionsTable();
        } else if(plannedRadio.isSelected()){
            initPlannedTransactionsTable();
        }

    }
    private void initTransactionsTable(){
//        transactionsTable.getItems().setAll(mock.getTransactions(dateFrom, dateTo, mock.getAccounts(), categoriesList));
        plannedTransactionsTable.setVisible(false);
        transactionsTable.setVisible(true);
        transactionsTable.getItems().setAll(mock.getTransactions(dateFrom, dateTo, accountsList, categoriesList));
        dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ExternalTransaction, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ExternalTransaction, String> p) {
                if (p.getValue() != null) {
                    return new SimpleStringProperty(p.getValue().dateProperty().get().toString());
                } else {
                    return new SimpleStringProperty("<empty>");
                }
            }
        });
        valueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ExternalTransaction, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ExternalTransaction, String> p) {
                if (p.getValue() != null) {
                    return new SimpleStringProperty(p.getValue().deltaProperty().get().toString());
                } else {
                    return new SimpleStringProperty("<empty>");
                }
            }
        });
        categoryColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ExternalTransaction, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ExternalTransaction, String> p) {
                if (p.getValue() != null) {
                    return new SimpleStringProperty(p.getValue().categoryMonadicProperty().get().nameProperty().get());
                } else {
                    return new SimpleStringProperty("<empty>");
                }
            }
        });
        destinationAccountColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ExternalTransaction, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ExternalTransaction, String> p) {
                if (p.getValue() != null) {
                    return new SimpleStringProperty(p.getValue().destinationAccountProperty().get().toString());
                } else {
                    return new SimpleStringProperty("<empty>");
                }
            }
        });
        descriptionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ExternalTransaction, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ExternalTransaction, String> p) {
                if (p.getValue() != null) {
                    return new SimpleStringProperty(p.getValue().commentMonadicProperty().get());
                } else {
                    return new SimpleStringProperty("<empty>");
                }
            }
        });

    }
    private void initPlannedTransactionsTable(){
        plannedTransactionsTable.getItems().setAll(budgetService.getPlannedTransactions(dateFrom, dateTo, categoriesList));
        transactionsTable.setVisible(false);
        plannedTransactionsTable.setVisible(true);
//        plannedTransactionsTable.getItems().setAll(budgetService.getPlannedTransactions(dateFrom, dateTo, categoriesList));
        plannedDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlannedTransaction, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PlannedTransaction, String> p) {
                if (p.getValue() != null) {
                    return new SimpleStringProperty(p.getValue().getDate().toString());
                } else {
                    return new SimpleStringProperty("<empty>");
                }
            }
        });
        plannedValueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlannedTransaction, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PlannedTransaction, String> p) {
                if (p.getValue() != null) {
                    return new SimpleStringProperty(p.getValue().getValue().toString());
                } else {
                    return new SimpleStringProperty("<empty>");
                }
            }
        });
        plannedCategoryColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlannedTransaction, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PlannedTransaction, String> p) {
                if (p.getValue() != null) {
                    return new SimpleStringProperty(p.getValue().getCategory().nameProperty().get());
                } else {
                    return new SimpleStringProperty("<empty>");
                }
            }
        });
    }
    private void initChart() {
        ObservableList<XYChart.Series<String, BigDecimal>> lineChartData = createLineChartData(mock2,dateFrom, dateTo, accountsList, categoriesList);

        lineChart.setData(lineChartData);
        lineChart.createSymbolsProperty();
    }

    public static ObservableList<XYChart.Series<String, BigDecimal>> createLineChartData(InOutWindowMockImpl dataSource, LocalDate from, LocalDate to, List<Account> accounts, ObservableList<Category> categories) {
        ObservableList<XYChart.Series<String, BigDecimal>> result = FXCollections.observableArrayList();

        LineChart.Series<String, BigDecimal> series1 = new LineChart.Series<>();
        LineChart.Series<String, BigDecimal> series2 = new LineChart.Series<>();

        Map<LocalDate, BigDecimal> data = dataSource.getIncomePerDay(from, to, accounts, categories);
        Map<LocalDate, BigDecimal> data2 = dataSource.getPlannedIncomePerDay(from, to, categories);

        if (data != null && data2 != null) {
            for (LocalDate iterDate = to; iterDate.isAfter(from.minusDays(1)); iterDate = iterDate.minusMonths(1)) {
                series1.getData().add(0, new XYChart.Data<>(iterDate.format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        (sumTransactions(iterDate.withDayOfMonth(1), iterDate.withDayOfMonth(iterDate.lengthOfMonth()), data))));
                series2.getData().add(0, new XYChart.Data<>(iterDate.format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        (sumTransactions(iterDate.withDayOfMonth(1), iterDate.withDayOfMonth(iterDate.lengthOfMonth()), data2))));
            }
        }


        series1.setName(PropertiesUtil.REAL);
        series2.setName(PropertiesUtil.PLANNED);

        result.add(series1);
        result.add(series2);

        return result;
    }
    public void refreshContent(){
        mock.refreshCache();
        this.initialize();
    }
}
