package pl.edu.agh.iisg.to.to2project.app.stats.main_panel.controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.stats.util.AccountTreeProviderUtil;
import pl.edu.agh.iisg.to.to2project.app.stats.util.CategoryTreeProviderUtil;
import pl.edu.agh.iisg.to.to2project.app.stats.util.PropertiesUtil;
import pl.edu.agh.iisg.to.to2project.app.stats.util.entity.BorderedTitledPane;
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
 * Created by Wojciech Dymek on 21.11.2015.
 */
@Controller
public class MainWindowController {
    @Autowired
    private IBasicDataSource mock;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private CategoryTreeProviderUtil categoryTreeProviderUtil;

    @Autowired
    private AccountTreeProviderUtil accountTreeProviderUtil;

    @FXML
    private TextField wplywyTextField;

    @FXML
    private TextField wydatkiTextField;

    @FXML
    private Button wplywyVsWydatki;

    @FXML
    private Button udzialKategoriiWydatkach;

    @FXML
    private Button planowanyBudzet;

    @FXML
    private TextField saldoTextField;


    @FXML
    private BorderedTitledPane borderedTitledPaneStatystyki;

    @FXML
    private BorderedTitledPane borderedTitledPaneOpcje;

    @FXML
    private BorderedTitledPane borderedTitledPaneDatePicker;

    @FXML
    private BorderedTitledPane optionPanelBorder;


    @FXML
    private ImageView clipArt;
    private DatePicker fromDatePicker;
    private DatePicker toDatePicker;
    private MenuButton accountsMenuButton;
    private MenuButton categoriesMenuButton;
    @FXML
    private GridPane optionPanelGridPane;
    private ObservableList<Account> accountsList;
    private ObservableList<Category> categoriesList;
    private List<CategoryTreeProviderUtil.CategoryAndCheckMenuItem> map = new LinkedList<>();

//    private InOutWindowMock mock2 = InOutWindowMock.getInstance();

    @Autowired
    private InOutWindowMockImpl mock2;

    private Image veryPoorImg = new Image(PropertiesUtil.VERY_POOR_IMG);
    private Image poorImg = new Image(PropertiesUtil.POOR_IMG);
    private Image richImg = new Image(PropertiesUtil.RICH_IMG);


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
        initMiddleComponents();

        optionPanelBorder.setText(PropertiesUtil.OPTION_TITLE);
//        borderedTitledPaneStatystyki.setText(PropertiesUtil.GENERATE_STATS_TITLE);

        setFields();
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
            if (fromDatePicker.getSelectedDate().isAfter(toDatePicker.getSelectedDate()))
                fromDatePicker.setSelectedDate(toDatePicker.getSelectedDate().withDayOfMonth(1));
            setFields();
        });
        toDatePicker.selectedDateProperty().addListener(observable -> {
            if (toDatePicker.getSelectedDate().isBefore(fromDatePicker.getSelectedDate()))
                toDatePicker.setSelectedDate(fromDatePicker.getSelectedDate().withDayOfMonth(fromDatePicker.getSelectedDate().lengthOfMonth()));
            setFields();
        });
    }
    private void initOptionPanel() {
        accountsList.addListener(new ListChangeListener<Account>() {
            @Override
            public void onChanged(Change<? extends Account> c) {
                setFields();
            }
        });
        categoriesList.addListener(new ListChangeListener<Category>() {
            @Override
            public void onChanged(Change<? extends Category> c) {
                setFields();
            }
        });
    }
    private void initMiddleComponents() {
        clipArt.setFitHeight(PropertiesUtil.CLIPART_HEIGHT);
        clipArt.setFitWidth(PropertiesUtil.CLIPART_WIDTH);


        saldoTextField.getStyleClass().add("balance");

        wplywyTextField.setEditable(false);
        wydatkiTextField.setEditable(false);
        saldoTextField.setEditable(false);
    }


    private void setFields(){
        double income = mock2.getIncome(fromDatePicker.getSelectedDate(), toDatePicker.getSelectedDate(), accountsList, categoriesList);
        wplywyTextField.setText(income + " zl");
        double outgoings = mock2.getOutgoings(fromDatePicker.getSelectedDate(), toDatePicker.getSelectedDate(), accountsList, categoriesList);
        wydatkiTextField.setText(outgoings + " zl");
        saldoTextField.setText(income - outgoings + " zl");

        setImage(income-outgoings);
    }

    private void setImage(double balance) {
        if (balance < -500) {
            clipArt.setImage(veryPoorImg);
        } else if (balance > 1000) {
            clipArt.setImage(richImg);
        } else {
            clipArt.setImage(poorImg);
        }
    }

    private LocalDate selectedDateFrom(){
        return (fromDatePicker.getSelectedDate() == null || toDatePicker.getSelectedDate() == null) ? LocalDate.now().minusMonths(6) : fromDatePicker.getSelectedDate();
    }

    private LocalDate selectedDateTo(){
        return (fromDatePicker.getSelectedDate() == null || toDatePicker.getSelectedDate() == null) ? LocalDate.now() : toDatePicker.getSelectedDate();
    }

    private List<Account> selectedAccounts(){
        return accountsList;
    }

}