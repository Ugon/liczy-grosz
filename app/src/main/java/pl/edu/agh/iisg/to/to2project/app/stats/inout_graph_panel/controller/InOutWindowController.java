package pl.edu.agh.iisg.to.to2project.app.stats.inout_graph_panel.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
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
import pl.edu.agh.iisg.to.to2project.service.impl.IBasicDataSourceImpl;
import pl.edu.agh.iisg.to.to2project.service.impl.InOutWindowMockImpl;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Wojciech Dymek on 24.11.2015.
 */
@Controller
public class InOutWindowController {
    @Autowired
    private IBasicDataSourceImpl mock;
    @Autowired
    private InOutWindowMockImpl mock2;

    @Autowired
    private AccountTreeProviderUtil accountTreeProviderUtil;

    @Autowired
    private CategoryTreeProviderUtil categoryTreeProviderUtil;

    @FXML
    private LineChart<String, BigDecimal> lineChart;

    @FXML
    private BorderedTitledPane borderedTitledPaneOpcje;


    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private CategoryAxis monthXAxis;

    @FXML
    private TableView<ExternalTransaction> transactionsTable;

    @FXML private TableColumn dateColumn;
    @FXML private TableColumn valueColumn;
    @FXML private TableColumn sourceAccountColumn;
    @FXML private TableColumn destinationAccountColumn;
    @FXML private TableColumn categoryColumn;
    @FXML private TableColumn descriptionColumn;
    @FXML
    private GridPane optionPanelGridPane;


    private List<Account> accounts;
    private List<Category> categories;
    private LocalDate dateFrom;
    private LocalDate dateTo;
//    private InOutWindowMock mock2 = InOutWindowMock.getInstance();
    private MenuButton accountsMenuButton;
    private MenuButton categoriesMenuButton;
    private ObservableList<Account> accountsList;
    private ObservableList<Category> categoriesList;
    private List<CategoryTreeProviderUtil.CategoryAndCheckMenuItem> map = new LinkedList<>();



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

        this.dateFrom = fromDatePicker.getSelectedDate();
        this.dateTo = toDatePicker.getSelectedDate();
        this.accounts = mock.getAccounts();
        this.categories = mock.getCategories();


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

        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
        borderedTitledPaneOpcje.setText(PropertiesUtil.OPTION_TITLE);

        initChart();
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
        });

        toDatePicker.selectedDateProperty().addListener(observable -> {
            if (toDatePicker.getSelectedDate().isBefore(fromDatePicker.getSelectedDate())) {
                dateTo = fromDatePicker.getSelectedDate().withDayOfMonth(fromDatePicker.getSelectedDate().lengthOfMonth());
                toDatePicker.setSelectedDate(dateTo);
            } else {
                dateTo = toDatePicker.getSelectedDate();
            }
            initChart();
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
                accounts = accountsList;
                initChart();
            }
        });
        categoriesList.addListener(new ListChangeListener<Category>() {
            @Override
            public void onChanged(Change<? extends Category> c) {
                categories = categoriesList;
                initChart();
            }
        });
    }
    private void initChart() {
        ObservableList<XYChart.Series<String, BigDecimal>> lineChartData = createLineChartData(mock2, dateFrom, dateTo, mock.getAccounts(), mock.getCategories());

        lineChart.setData(lineChartData);
        lineChart.createSymbolsProperty();

        initTable();
    }
    private void initTable() {
        transactionsTable.getItems().setAll(mock.getTransactions(dateFrom, dateTo, accounts, categories));
//        transactionsTable.getItems().setAll(mock.getTransactions(dateFrom, dateTo, optionPanel.getAccounts(), optionPanel.getCategories()));
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
//        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
//        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
//        accountColumn.setCellValueFactory(new PropertyValueFactory<>("accountName"));
//        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
//        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
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

    public static ObservableList<XYChart.Series<String, BigDecimal>> createLineChartData(InOutWindowMockImpl dataSource, LocalDate from, LocalDate to, List<Account> accounts, List<Category> categories) {
        int i = 0;
        int monthDiff = Period.between(from, to).getMonths();

        LineChart.Series<String, BigDecimal> series1 = new LineChart.Series<>();
        series1.setName(PropertiesUtil.INCOMES);
        LineChart.Series<String, BigDecimal> series2 = new LineChart.Series<>();
        series2.setName(PropertiesUtil.OUTCOMES);

        Map<LocalDate, BigDecimal> data = dataSource.getIncomePerDay(from, to, accounts, categories);
        Map<LocalDate, BigDecimal> data2 = dataSource.getOutgoingsPerDay(from, to, accounts, categories);

        if (data != null && data2 != null) {
            if (monthDiff < 1 && monthDiff > -1) {
                for (LocalDate iterDate = to; iterDate.isAfter(from.minusDays(1)); iterDate = iterDate.minusDays(1)) {
                    series1.getData().add(i, new XYChart.Data<>(iterDate.toString(), (data.get(iterDate) == null ? BigDecimal.ZERO : data.get(iterDate))));
                    series2.getData().add(i, new XYChart.Data<>(iterDate.toString(), (data2.get(iterDate) == null ? BigDecimal.ZERO : data2.get(iterDate))));
                }
            }
            else if (monthDiff > 0 && monthDiff < 6) {
                for (LocalDate iterDate = to; iterDate.isAfter(from.minusDays(1)); iterDate = iterDate.minusWeeks(1)) {
                    series1.getData().add(i, new XYChart.Data<>(iterDate.format(DateTimeFormatter.ofPattern("yyyy-MM' week 'W")),
                            (sumTransactions(iterDate.with(DayOfWeek.MONDAY), iterDate.with(DayOfWeek.SUNDAY), data))));
                    series2.getData().add(i, new XYChart.Data<>(iterDate.format(DateTimeFormatter.ofPattern("yyyy-MM' week 'W")),
                            (sumTransactions(iterDate.with(DayOfWeek.MONDAY), iterDate.with(DayOfWeek.SUNDAY), data2))));
                }

            } else if (monthDiff > 5) {
                for (LocalDate iterDate = to; iterDate.isAfter(from.minusDays(1)); iterDate = iterDate.minusMonths(1)) {
                    series1.getData().add(i, new XYChart.Data<>(iterDate.format(DateTimeFormatter.ofPattern("yyyy-MM")),
                            (sumTransactions(iterDate.withDayOfMonth(1), iterDate.withDayOfMonth(iterDate.lengthOfMonth()), data))));
                    series2.getData().add(i, new XYChart.Data<>(iterDate.format(DateTimeFormatter.ofPattern("yyyy-MM")),
                            (sumTransactions(iterDate.withDayOfMonth(1), iterDate.withDayOfMonth(iterDate.lengthOfMonth()), data2))));
                }
            }
        }

        ObservableList<XYChart.Series<String, BigDecimal>> lineChartData = FXCollections.observableArrayList();

        lineChartData.add(series1);
        lineChartData.add(series2);

        return lineChartData;
    }
    public void refreshContent(){
        mock.refreshCache();
        this.initialize();
    }
}
