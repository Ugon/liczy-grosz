package  pl.edu.agh.iisg.to.to2project.budget;

import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.VBox;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.agh.iisg.to.to2project.budget_persistence.BudgetPersistenceManager;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by mike on 20.01.16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BudgetTests.TestConfig.class})
public class BudgetTests {

    @Configuration
    public static class TestConfig {
        @Bean
        @Primary
        public BudgetPersistenceManager BudgetPersistenceManager() {
            return mock(BudgetPersistenceManager.class);
        }
    }

    @Autowired
    private ApplicationContext context;

    @Autowired
    private BudgetPersistenceManager budgetPersistenceManager;

    private DataGenerator dataGenerator;

    private Data instance;

    private LocalDate dateTime;

    @Before
    public void setUp() throws Exception {
        new JFXPanel();
        resetMocks();
        dateTime = LocalDate.now();
        dataGenerator = new DataGenerator();
        instance = Data.getInstance();
        instance.setVboxes(new VBox(), new VBox());
        instance.setBudgetPersistenceManager(budgetPersistenceManager);
    }

    private void resetMocks(){
        reset(budgetPersistenceManager);
    }

    private void checkItem(DisplayedItem item, String name, double planValue, double transactionValue, boolean hasChildren, double planSumValue, double transactionSumValue) {
        assertThat(item.getCategoryName()).isEqualTo(name);
        assertThat(item.getChildren().isEmpty()).isEqualTo(!hasChildren);
        assertThat(item.getPlanValue()).isEqualTo(planValue);
        assertThat(item.getTransactionsValue()).isEqualTo(transactionValue);
        assertThat(item.getPlanSumValue()).isEqualTo(planSumValue);
        assertThat(item.getTransactionsSumValue()).isEqualTo(transactionSumValue);
    }

    private void checkItem(DisplayedItem item, String name, double planValue, double transactionValue) {
        checkItem(item, name, planValue, transactionValue, false, 0.0, 0.0);
    }

    @Test
    public void addOneDisplayItem_BothPresent() throws Exception {
        //given
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();
        instance.setYearAndMonth(year, month);

        dataGenerator.generateOneItemBothPresent(year, month);
        Map<String, Double[]> planMap = dataGenerator.getPlanMap();
        List<Category> categoryList = dataGenerator.getCategoryList();

        //when
        for(String categoryName : dataGenerator.getPlanMap().keySet()) {
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month , false)).thenReturn(planMap.get(categoryName)[0]);
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, true)).thenReturn(planMap.get(categoryName)[1]);
        }

        instance.build(categoryList);

        //then
        assertThat(instance.getEarningDisplayedItemRoot().getChildren().size()).isEqualTo(1);
        DisplayedItem item = (DisplayedItem) instance.getEarningDisplayedItemRoot().getChildren().get(0);
        checkItem(item, "catA", 100.0, 130.0);

        assertThat(instance.getSpendingDisplayedItemRoot().getChildren().size()).isEqualTo(1);
        item = (DisplayedItem) instance.getSpendingDisplayedItemRoot().getChildren().get(0);
        checkItem(item, "catA", 40.0, 70.0);
    }

    @Test
    public void addOneDisplayItem_BothPast() throws Exception {
        //given
        int year = dateTime.getYear() - 1;
        int month = dateTime.getMonthValue();
        instance.setYearAndMonth(year, month);

        dataGenerator.generateOneItemBothPast(year, month);
        Map<String, Double[]> planMap = dataGenerator.getPlanMap();
        List<Category> categoryList = dataGenerator.getCategoryList();

        //when
        for(String categoryName : dataGenerator.getPlanMap().keySet()) {
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, false)).thenReturn(planMap.get(categoryName)[0]);
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, true)).thenReturn(planMap.get(categoryName)[1]);
        }

        instance.build(categoryList);

        //then
        assertThat(instance.getEarningDisplayedItemRoot().getChildren().size()).isEqualTo(0);
        assertThat(instance.getSpendingDisplayedItemRoot().getChildren().size()).isEqualTo(0);
    }

    @Test
    public void addOneDisplayItem_OnePast_Trans() throws Exception {
        //given
        int year = dateTime.getYear() - 1;
        int month = dateTime.getMonthValue();
        instance.setYearAndMonth(year, month);

        dataGenerator.generateOneItemOnePastTrans(year, month);
        Map<String, Double[]> planMap = dataGenerator.getPlanMap();
        List<Category> categoryList = dataGenerator.getCategoryList();

        //when
        for(String categoryName : dataGenerator.getPlanMap().keySet()) {
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, false)).thenReturn(planMap.get(categoryName)[0]);
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, true)).thenReturn(planMap.get(categoryName)[1]);
        }

        instance.build(categoryList);

        //then
        assertThat(instance.getEarningDisplayedItemRoot().getChildren().size()).isEqualTo(0);

        assertThat(instance.getSpendingDisplayedItemRoot().getChildren().size()).isEqualTo(1);
        DisplayedItem item = (DisplayedItem) instance.getSpendingDisplayedItemRoot().getChildren().get(0);
        checkItem(item, "catA", 0.0, 90.0);
    }

    @Test
    public void addOneDisplayItem_OnePast_Plan() throws Exception {
        //given
        int year = dateTime.getYear() - 1;
        int month = dateTime.getMonthValue();
        instance.setYearAndMonth(year, month);

        dataGenerator.generateOneItemOnePastPlan(year, month);
        Map<String, Double[]> planMap = dataGenerator.getPlanMap();
        List<Category> categoryList = dataGenerator.getCategoryList();

        //when
        for(String categoryName : dataGenerator.getPlanMap().keySet()) {
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, false)).thenReturn(planMap.get(categoryName)[0]);
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, true)).thenReturn(planMap.get(categoryName)[1]);
        }

        instance.build(categoryList);

        //then
        assertThat(instance.getEarningDisplayedItemRoot().getChildren().size()).isEqualTo(1);
        DisplayedItem item = (DisplayedItem) instance.getEarningDisplayedItemRoot().getChildren().get(0);
        checkItem(item, "catA", 20.0, 0.0);

        assertThat(instance.getSpendingDisplayedItemRoot().getChildren().size()).isEqualTo(0);
    }

    @Test
    public void addTwoDisplayItems_EqualLevel() throws Exception {
        //given
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();
        instance.setYearAndMonth(year, month);

        dataGenerator.generateTwoItemsEqualLevel(year, month);
        Map<String, Double[]> planMap = dataGenerator.getPlanMap();
        List<Category> categoryList = dataGenerator.getCategoryList();

        //when
        for(String categoryName : dataGenerator.getPlanMap().keySet()) {
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, false)).thenReturn(planMap.get(categoryName)[0]);
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, true)).thenReturn(planMap.get(categoryName)[1]);
        }

        instance.build(categoryList);

        //then
        assertThat(instance.getEarningDisplayedItemRoot().getChildren().size()).isEqualTo(2);
        DisplayedItem item = (DisplayedItem) instance.getEarningDisplayedItemRoot().getChildren().get(0);
        checkItem(item, "catA", 7000.0, 10.0);
        item = (DisplayedItem) instance.getEarningDisplayedItemRoot().getChildren().get(1);
        checkItem(item, "catB", 0.0, 1250.0);

        assertThat(instance.getSpendingDisplayedItemRoot().getChildren().size()).isEqualTo(2);
        item = (DisplayedItem) instance.getSpendingDisplayedItemRoot().getChildren().get(0);
        checkItem(item, "catA", 0.0, 200.0);
        item = (DisplayedItem) instance.getSpendingDisplayedItemRoot().getChildren().get(1);
        checkItem(item, "catB", 200.0, 100.0);
    }

    @Test
    public void addTwoDisplayItems_ParentAndChild() throws Exception {
        //given
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();
        instance.setYearAndMonth(year, month);

        dataGenerator.generateTwoItemsParentAndChild(year, month);
        Map<String, Double[]> planMap = dataGenerator.getPlanMap();
        List<Category> categoryList = dataGenerator.getCategoryList();

        //when
        for(String categoryName : dataGenerator.getPlanMap().keySet()) {
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, false)).thenReturn(planMap.get(categoryName)[0]);
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, true)).thenReturn(planMap.get(categoryName)[1]);
        }

        instance.build(categoryList);

        //then
        assertThat(instance.getEarningDisplayedItemRoot().getChildren().size()).isEqualTo(1);
        DisplayedItem item = (DisplayedItem) instance.getEarningDisplayedItemRoot().getChildren().get(0);
        checkItem(item, "catA", 7000.0, 10.0, true, 7000.0, 1260.0);
        item = (DisplayedItem) item.getChildren().get(0);
        checkItem(item, "catB", 0.0, 1250.0);

        assertThat(instance.getSpendingDisplayedItemRoot().getChildren().size()).isEqualTo(1);
        item = (DisplayedItem) instance.getSpendingDisplayedItemRoot().getChildren().get(0);
        checkItem(item, "catA", 0.0, 200.0, true, 200.0, 300.0);
        item = (DisplayedItem) item.getChildren().get(0);
        checkItem(item, "catB", 200.0, 100.0);
    }

    @Test
    public void buildWholeTree() throws Exception {
        //given
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();
        instance.setYearAndMonth(year, month);

        dataGenerator.generateWholeTree(year, month);
        Map<String, Double[]> planMap = dataGenerator.getPlanMap();
        List<Category> categoryList = dataGenerator.getCategoryList();

        //when
        for(String categoryName : dataGenerator.getPlanMap().keySet()) {
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, false)).thenReturn(planMap.get(categoryName)[0]);
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, true)).thenReturn(planMap.get(categoryName)[1]);
        }

        instance.build(categoryList);

        //then
        assertThat(instance.getEarningDisplayedItemRoot().getChildren().size()).isEqualTo(4);
        DisplayedItem item1 = (DisplayedItem) instance.getEarningDisplayedItemRoot().getChildren().get(0);
        checkItem(item1, "inA", 0.0, 130.0, true, 0.0, 195.0);
        assertThat(item1.getChildren().size()).isEqualTo(2);
            DisplayedItem item2 = (DisplayedItem) item1.getChildren().get(0);
            checkItem(item2, "inB", 0.0, 0.0, true, 0.0, 10.0);
            assertThat(item2.getChildren().size()).isEqualTo(1);
                DisplayedItem item3 = (DisplayedItem) item2.getChildren().get(0);
                checkItem(item3, "inD", 0.0, 10.0);
            item2 = (DisplayedItem) item1.getChildren().get(1);
            checkItem(item2, "inC", 0.0, 55.0);
        item1 = (DisplayedItem) instance.getEarningDisplayedItemRoot().getChildren().get(1);
        checkItem(item1, "outA", 0.0, 0.0, true, 0.0, 0.0);
        assertThat(item1.getChildren().size()).isEqualTo(2);
            item2 = (DisplayedItem) item1.getChildren().get(0);
            checkItem(item2, "outB", 0.0, 0.0);
            item2 = (DisplayedItem) item1.getChildren().get(1);
            checkItem(item2, "outC", 0.0, 0.0, true, 0.0, 0.0);
            assertThat(item2.getChildren().size()).isEqualTo(2);
                item3 = (DisplayedItem) item2.getChildren().get(0);
                checkItem(item3, "outD", 0.0, 0.0);
                item3 = (DisplayedItem) item2.getChildren().get(1);
                checkItem(item3, "outE", 0.0, 0.0);
        item1 = (DisplayedItem) instance.getEarningDisplayedItemRoot().getChildren().get(2);
        checkItem(item1, "bothA", 0.0, 0.0, true, 0.0, 110.0);
        assertThat(item1.getChildren().size()).isEqualTo(2);
            item2 = (DisplayedItem) item1.getChildren().get(0);
            checkItem(item2, "bothB", 0.0, 110.0);
            item2 = (DisplayedItem) item1.getChildren().get(1);
            checkItem(item2, "bothC", 0.0, 0.0);
        item1 = (DisplayedItem) instance.getEarningDisplayedItemRoot().getChildren().get(3);
        checkItem(item1, "both2A", 0.0, 0.0, true, 0.0, 140.0);
        assertThat(item1.getChildren().size()).isEqualTo(3);
            item2 = (DisplayedItem) item1.getChildren().get(0);
            checkItem(item2, "both2B", 0.0, 10.0);
            item2 = (DisplayedItem) item1.getChildren().get(1);
            checkItem(item2, "both2C", 0.0, 90.0);
            item2 = (DisplayedItem) item1.getChildren().get(2);
            checkItem(item2, "both2D", 0.0, 0.0, true, 0.0, 40.0);
            assertThat(item2.getChildren().size()).isEqualTo(1);
                item3 = (DisplayedItem) item2.getChildren().get(0);
                checkItem(item3, "both2E", 0.0, 40.0);


        assertThat(instance.getSpendingDisplayedItemRoot().getChildren().size()).isEqualTo(4);
        item1 = (DisplayedItem) instance.getSpendingDisplayedItemRoot().getChildren().get(0);
        checkItem(item1, "inA", 0.0, 0.0, true, 0.0, 0.0);
        assertThat(item1.getChildren().size()).isEqualTo(2);
            item2 = (DisplayedItem) item1.getChildren().get(0);
            checkItem(item2, "inB", 0.0, 0.0, true, 0.0, 0.0);
            assertThat(item2.getChildren().size()).isEqualTo(1);
                item3 = (DisplayedItem) item2.getChildren().get(0);
                checkItem(item3, "inD", 0.0, 0.0);
            item2 = (DisplayedItem) item1.getChildren().get(1);
            checkItem(item2, "inC", 0.0, 0.0);
        item1 = (DisplayedItem) instance.getSpendingDisplayedItemRoot().getChildren().get(1);
        checkItem(item1, "outA", 0.0, 0.0, true, 0.0, 395.0);
        assertThat(item1.getChildren().size()).isEqualTo(2);
            item2 = (DisplayedItem) item1.getChildren().get(0);
            checkItem(item2, "outB", 0.0, 135.0);
            item2 = (DisplayedItem) item1.getChildren().get(1);
            checkItem(item2, "outC", 0.0, 0.0, true, 0.0, 260.0);
            assertThat(item2.getChildren().size()).isEqualTo(2);
                item3 = (DisplayedItem) item2.getChildren().get(0);
                checkItem(item3, "outD", 0.0, 250.0);
                item3 = (DisplayedItem) item2.getChildren().get(1);
                checkItem(item3, "outE", 0.0, 10.0);
        item1 = (DisplayedItem) instance.getSpendingDisplayedItemRoot().getChildren().get(2);
        checkItem(item1, "bothA", 0.0, 0.0, true, 0.0, 50.0);
        assertThat(item1.getChildren().size()).isEqualTo(2);
            item2 = (DisplayedItem) item1.getChildren().get(0);
            checkItem(item2, "bothB", 0.0, 0.0);
            item2 = (DisplayedItem) item1.getChildren().get(1);
            checkItem(item2, "bothC", 0.0, 50.0);
        item1 = (DisplayedItem) instance.getSpendingDisplayedItemRoot().getChildren().get(3);
        checkItem(item1, "both2A", 0.0, 0.0, true, 0.0, 150.0);
        assertThat(item1.getChildren().size()).isEqualTo(3);
            item2 = (DisplayedItem) item1.getChildren().get(0);
            checkItem(item2, "both2B", 0.0, 0.0);
            item2 = (DisplayedItem) item1.getChildren().get(1);
            checkItem(item2, "both2C", 0.0, 50.0);
            item2 = (DisplayedItem) item1.getChildren().get(2);
            checkItem(item2, "both2D", 0.0, 100.0, true, 0.0, 100.0);
            assertThat(item2.getChildren().size()).isEqualTo(1);
                item3 = (DisplayedItem) item2.getChildren().get(0);
                checkItem(item3, "both2E", 0.0, 00.0);
    }

    @Test
    public void buildWholeTree_Past() throws Exception {
        //given
        int year = dateTime.getYear() - 1;
        int month = dateTime.getMonthValue();
        instance.setYearAndMonth(year, month);

        dataGenerator.generateWholeTree(year, month);
        Map<String, Double[]> planMap = dataGenerator.getPlanMap();
        List<Category> categoryList = dataGenerator.getCategoryList();

        //when
        for(String categoryName : dataGenerator.getPlanMap().keySet()) {
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, false)).thenReturn(planMap.get(categoryName)[0]);
            when(budgetPersistenceManager.getPlannedValueForMonth(categoryName, year, month, true)).thenReturn(planMap.get(categoryName)[1]);
        }

        instance.build(categoryList);

        //then
        assertThat(instance.getEarningDisplayedItemRoot().getChildren().size()).isEqualTo(3);
        DisplayedItem item1 = (DisplayedItem) instance.getEarningDisplayedItemRoot().getChildren().get(0);
        checkItem(item1, "inA", 0.0, 130.0, true, 0.0, 195.0);
        assertThat(item1.getChildren().size()).isEqualTo(2);
            DisplayedItem item2 = (DisplayedItem) item1.getChildren().get(0);
            checkItem(item2, "inB", 0.0, 0.0, true, 0.0, 10.0);
            assertThat(item2.getChildren().size()).isEqualTo(1);
                DisplayedItem item3 = (DisplayedItem) item2.getChildren().get(0);
                checkItem(item3, "inD", 0.0, 10.0);
            item2 = (DisplayedItem) item1.getChildren().get(1);
            checkItem(item2, "inC", 0.0, 55.0);
        item1 = (DisplayedItem) instance.getEarningDisplayedItemRoot().getChildren().get(1);
        checkItem(item1, "bothA", 0.0, 0.0, true, 0.0, 110.0);
        assertThat(item1.getChildren().size()).isEqualTo(1);
            item2 = (DisplayedItem) item1.getChildren().get(0);
            checkItem(item2, "bothB", 0.0, 110.0);
        item1 = (DisplayedItem) instance.getEarningDisplayedItemRoot().getChildren().get(2);
        checkItem(item1, "both2A", 0.0, 0.0, true, 0.0, 140.0);
        assertThat(item1.getChildren().size()).isEqualTo(3);
            item2 = (DisplayedItem) item1.getChildren().get(0);
            checkItem(item2, "both2B", 0.0, 10.0);
            item2 = (DisplayedItem) item1.getChildren().get(1);
            checkItem(item2, "both2C", 0.0, 90.0);
            item2 = (DisplayedItem) item1.getChildren().get(2);
            checkItem(item2, "both2D", 0.0, 0.0, true, 0.0, 40.0);
            assertThat(item2.getChildren().size()).isEqualTo(1);
                item3 = (DisplayedItem) item2.getChildren().get(0);
                checkItem(item3, "both2E", 0.0, 40.0);


        assertThat(instance.getSpendingDisplayedItemRoot().getChildren().size()).isEqualTo(3);
        item1 = (DisplayedItem) instance.getSpendingDisplayedItemRoot().getChildren().get(0);
        checkItem(item1, "outA", 0.0, 0.0, true, 0.0, 395.0);
        assertThat(item1.getChildren().size()).isEqualTo(2);
            item2 = (DisplayedItem) item1.getChildren().get(0);
            checkItem(item2, "outB", 0.0, 135.0);
            item2 = (DisplayedItem) item1.getChildren().get(1);
            checkItem(item2, "outC", 0.0, 0.0, true, 0.0, 260.0);
            assertThat(item2.getChildren().size()).isEqualTo(2);
                item3 = (DisplayedItem) item2.getChildren().get(0);
                checkItem(item3, "outD", 0.0, 250.0);
                item3 = (DisplayedItem) item2.getChildren().get(1);
                checkItem(item3, "outE", 0.0, 10.0);
        item1 = (DisplayedItem) instance.getSpendingDisplayedItemRoot().getChildren().get(1);
        checkItem(item1, "bothA", 0.0, 0.0, true, 0.0, 50.0);
        assertThat(item1.getChildren().size()).isEqualTo(1);
            item2 = (DisplayedItem) item1.getChildren().get(0);
            checkItem(item2, "bothC", 0.0, 50.0);
        item1 = (DisplayedItem) instance.getSpendingDisplayedItemRoot().getChildren().get(2);
        checkItem(item1, "both2A", 0.0, 0.0, true, 0.0, 150.0);
        assertThat(item1.getChildren().size()).isEqualTo(2);
            item2 = (DisplayedItem) item1.getChildren().get(0);
            checkItem(item2, "both2C", 0.0, 50.0);
            item2 = (DisplayedItem) item1.getChildren().get(1);
            checkItem(item2, "both2D", 0.0, 100.0);
    }
}
