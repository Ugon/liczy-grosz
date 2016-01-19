package pl.edu.agh.iisg.to.to2project.app.stats.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.agh.iisg.to.to2project.app.stats.util.entity.calendar.DatePicker;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.service.IBasicDataSource;
import pl.edu.agh.iisg.to.to2project.service.impl.InOutWindowMockImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nesbite on 2015-11-29.
 */


public class OptionsPanel extends GridPane{
    @Autowired
    private IBasicDataSource expensesDataSource;

    @Autowired
    private InOutWindowMockImpl dataSource;

    @Autowired
    private CategoryTreeProviderUtil categoryTreeProviderUtil;

    @Autowired
    private AccountTreeProviderUtil accountTreeProviderUtil;

    private ObservableList<Account> accounts;
    private ObservableList<Category> categories;
    private List<CategoryTreeProviderUtil.CategoryAndCheckMenuItem> map = new LinkedList<>();

    private DatePicker fromDatePicker;
    private DatePicker toDatePicker;
//    private InOutWindowMock expensesDataSource = InOutWindowMock.getInstance();
    private MenuButton accountsMenuButton;
    private MenuButton categoriesMenuButton;
    private BooleanProperty withAccounts;
    private BooleanProperty withCategories;

    public OptionsPanel() {
        fromDatePicker = new DatePicker();
        toDatePicker = new DatePicker();

//        accounts = FXCollections.observableList(new ArrayList<>(expensesDataSource.getAccounts()));
//        categories = FXCollections.observableList(new ArrayList<>(expensesDataSource.getCategories()));

        accountsMenuButton = new MenuButton();
        categoriesMenuButton = new MenuButton();
//        categoryTreeProviderUtil = new CategoryTreeProviderUtil();
//        accountTreeProviderUtil = new AccountTreeProviderUtil();
        accounts = FXCollections.observableList(new ArrayList<Account>()); //expensesDataSource.getAccounts()
        categories = FXCollections.observableList(new ArrayList<Category>()); //expensesDataSource.getCategories()


    }

    public void init() {
        setVgap(15);

        fromDatePicker.setIsFromDatePicker(true);
        toDatePicker.setIsFromDatePicker(false);
        fromDatePicker.setSelectedDate(LocalDate.of(2016, 1, 1));
        toDatePicker.setSelectedDate(LocalDate.of(2016, 6, 30));
        accountsMenuButton.setMinWidth(180);
        accountsMenuButton.setText(PropertiesUtil.ALL);
        categoriesMenuButton.setMinWidth(180);
        categoriesMenuButton.setText(PropertiesUtil.ALL);

        accounts.add(0, new Account(PropertiesUtil.ALL, BigDecimal.ZERO));

        accountsMenuButton.getItems().addAll(accounts.stream().map(account -> {
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
        setCheckMenuItems(categories, 0);

        add(providePane(PropertiesUtil.ACCOUNTS, accountsMenuButton), 0, 0);
        add(providePane(PropertiesUtil.CATEGORIES, categoriesMenuButton), 0, 1);
        add(providePane(PropertiesUtil.FROM, fromDatePicker), 0, 2);
        add(providePane(PropertiesUtil.TO, toDatePicker), 0, 3);

//        accounts.clear();
//        categories.clear();

        map.addAll(categoryTreeProviderUtil.init(categoriesMenuButton));

        categoryTreeProviderUtil.selectCategoriesInMenu(categoriesMenuButton, it, new ArrayList<CategoryTreeProviderUtil.CategoryAndCheckMenuItem>());
    }

    public void setWithAccounts(String value) {
        withAccounts = new SimpleBooleanProperty(Boolean.parseBoolean(value));

        if (!withAccounts.getValue()) {
            accountsMenuButton.setText(PropertiesUtil.ALL);
            accountsMenuButton.setDisable(true);
        }
    }
    public String getWithAccounts(){
        return withAccounts.toString();
    }
    public void setWithCategories(String value) {
        withCategories = new SimpleBooleanProperty(Boolean.parseBoolean(value));

        if (!withCategories.getValue()) {
            categoriesMenuButton.setText(PropertiesUtil.ALL);
            categoriesMenuButton.setDisable(true);
        }
    }
    public String getWithCategories(){
        return withCategories.toString();
    }

    public DatePicker getFromDatePicker() {
        return fromDatePicker;
    }

    public DatePicker getToDatePicker() {
        return toDatePicker;
    }

    public ObservableList<Account> getAccounts(){
        return accounts;
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }

    private Node providePane(String label, Node child) {
        GridPane result = new GridPane();
        result.setVgap(5);
        result.add(new Label(label), 0, 0);
        result.add(child, 0, 1);
        return result;
    }

    private void addAccountsHandler(MenuItem item, MenuButton button){
        item.addEventHandler(ActionEvent.ACTION, event -> {
            accountTreeProviderUtil.selectAccountsInMenu(button, (CheckMenuItem) event.getSource());
            button.setText(accountTreeProviderUtil.getButtonText(button));
            accounts.clear();
            accounts.addAll(accountTreeProviderUtil.getSelectedAccounts(button));
        });
    }

    private void addCategoriesHandler(MenuItem item, MenuButton button){
        item.addEventHandler(ActionEvent.ACTION, event -> {
            categoryTreeProviderUtil.selectCategoriesInMenu(button, (CheckMenuItem) event.getSource(), map);
            button.setText(categoryTreeProviderUtil.getButtonText(map));
            categories.clear();
            categories.addAll(categoryTreeProviderUtil.getSelectedCategories(map));
        });
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
}
