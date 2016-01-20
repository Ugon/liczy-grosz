package pl.edu.agh.iisg.to.to2project.app.stats.category_share_graph_panel.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nesbite on 2015-11-26.
 */
@Controller
public class CategoryShareWindowController {

    @Autowired
    private IBasicDataSourceImpl mock;

    @Autowired
    private InOutWindowMockImpl mock2;

    @Autowired
    private AccountTreeProviderUtil accountTreeProviderUtil;

    @Autowired
    private CategoryTreeProviderUtil categoryTreeProviderUtil;

    @FXML
    private PieChart pieChart;

    @FXML
    private BorderedTitledPane borderedTitledPaneOpcje;

//    @FXML
//    private OptionsPanel2 optionPanel;
    @FXML
    private GridPane optionPanelGridPane;

    @FXML
    private TableView<ExternalTransaction> transactionsTable;

    @FXML private TableColumn dateColumn;
    @FXML private TableColumn valueColumn;
    @FXML private TableColumn destinationAccountColumn;
    @FXML private TableColumn categoryColumn;
    @FXML private TableColumn descriptionColumn;


    private List<Account> accounts;
    private List<Category> categories;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private List<ExternalTransaction> transactions;
    private boolean subcategories;

    private DatePicker fromDatePicker;
    private DatePicker toDatePicker;
    private MenuButton accountsMenuButton;
    private MenuButton categoriesMenuButton;
    private ObservableList<Account> accountsList;
    private ObservableList<Category> categoriesList;
    private List<CategoryTreeProviderUtil.CategoryAndCheckMenuItem> map = new LinkedList<>();



    @FXML
    private void initialize() {
//        setVgap(15);
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

        categoriesMenuButton.setText(PropertiesUtil.ALL);
        categoriesMenuButton.setDisable(true);
        this.accounts = mock.getAccounts();
        this.categories = mock.getCategories();
        this.dateFrom = fromDatePicker.getSelectedDate();
        this.dateTo = toDatePicker.getSelectedDate();
        this.subcategories = false;

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
        initPieChart();
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

        if((fromDatePicker.getSelectedDate() == null) || (toDatePicker.getSelectedDate() == null)) {
            fromDatePicker.setSelectedDate(dateFrom);
            toDatePicker.setSelectedDate(dateTo);
        }

        fromDatePicker.selectedDateProperty().addListener(observable -> {
            if (fromDatePicker.getSelectedDate().isAfter(toDatePicker.getSelectedDate())) {
                dateFrom = toDatePicker.getSelectedDate().withDayOfMonth(1);
                fromDatePicker.setSelectedDate(dateFrom);
            } else {
                dateFrom = fromDatePicker.getSelectedDate();
            }
            this.initPieChart();
        });

        toDatePicker.selectedDateProperty().addListener(observable -> {
            if (toDatePicker.getSelectedDate().isBefore(fromDatePicker.getSelectedDate())) {
                dateTo = fromDatePicker.getSelectedDate().withDayOfMonth(fromDatePicker.getSelectedDate().lengthOfMonth());
                toDatePicker.setSelectedDate(dateTo);
            } else {
                dateTo = toDatePicker.getSelectedDate();
            }
            this.initPieChart();
        });
    }
    private void initOptionPanel() {

        accountsList.addListener(new ListChangeListener<Account>() {
            @Override
            public void onChanged(Change<? extends Account> c) {
                accounts = accountsList;
                initPieChart();
            }
        });
        categoriesList.addListener(new ListChangeListener<Category>() {
            @Override
            public void onChanged(Change<? extends Category> c) {
                categories = categoriesList;
                initPieChart();
            }
        });
    }
    private void initPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        transactions = mock.getTransactions(dateFrom, dateTo, accounts, categories).stream()
                .filter(a -> a.deltaProperty().get().doubleValue() < 0)
                .collect(Collectors.toList());

        if(subcategories) {
            transactions.stream()
                    .collect(Collectors
                            .groupingBy(a -> a.categoryMonadicProperty().get().parentCategoryMonadicProperty().get() != null  ? a.categoryMonadicProperty().get().parentCategoryMonadicProperty().get() : a.categoryMonadicProperty().get(), Collectors
                                    .summingDouble(a -> a.deltaProperty().get().abs().doubleValue())))
                    .forEach((a, b) -> pieChartData.add(new PieChart.Data(a.nameProperty().get(), b)));
        }
        else {
            transactions.stream()
                    .collect(Collectors
                            .groupingBy(a -> a.categoryMonadicProperty().get(), Collectors
                                    .summingDouble(a -> a.deltaProperty().get().abs().doubleValue())))
                    .forEach((a, b) -> pieChartData.add(new PieChart.Data(a.nameProperty().get(), b)));
        }
        pieChart.setData(pieChartData);

        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    e -> {
                        if (e.getButton() == MouseButton.PRIMARY) {
                            List<Category> cats = new LinkedList<>(mock2.getCategoryByName(categories, data.getName()).subCategoriesObservableSet());
                            if (!cats.isEmpty()) {
                                categories = cats;
                                this.subcategories = false;
                                this.initPieChart();
                            }
                        } else if (e.getButton() == MouseButton.SECONDARY) {
                            if (subcategories) {
                                return;
                            }
                            categories = mock.getCategories();
                            this.subcategories = true;
                            this.initPieChart();
                        }
                    });
        }
        initTable();
    }
    private void initTable() {
        transactionsTable.getItems().setAll(transactions);

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
    public void refreshContent(){
        mock.refreshCache();
        this.initialize();
    }
}
