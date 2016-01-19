package pl.edu.agh.iisg.to.to2project.app.stats.tests;//package pl.edu.agh.iisg.to.to2project.app.stats.tests;
//
//import javafx.collections.ObservableList;
//import javafx.scene.chart.XYChart;
//import junit.framework.Assert;
////import org.junit.Test;
////import org.testng.annotations.Test;
//import org.junit.Test;
//
//import pl.edu.agh.iisg.to.to2project.app.stats.entity.IDataSource;
//import pl.edu.agh.iisg.to.to2project.app.stats.mocks.InOutWindowMock;
//import pl.edu.agh.iisg.to.to2project.app.stats.planned_budget_graph_panel.controller.PlannedBudgetWindowController;
//import pl.edu.agh.iisg.to.to2project.app.stats.util.PropertiesUtil;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
///**
// * Created by Wojciech Dymek on 17.01.2016.
// */
//public class PlannedBudgetWindowControllerTest {
//    private InOutWindowMock mock = InOutWindowMock.getInstance();
//    private LocalDate from = LocalDate.of(2016, 1, 1);
//    private LocalDate to = LocalDate.of(2016, 6, 1);
//
//    @Test
//    public void testCreateLineChartDataNotNull() throws Exception {
//        ObservableList<XYChart.Series<String, BigDecimal>> result = PlannedBudgetWindowController.createLineChartData(mock, from, to, mock.getAccounts(), mock.getCategories(), mock.getTransactions(from,to,mock.getAccounts(), mock.getCategories()));
//
//        Assert.assertNotNull(result);
//        Assert.assertFalse(result.isEmpty());
//        Assert.assertTrue(result.size() == 2);
//    }
//
//    @Test
//    public void testContent() throws Exception {
//        ObservableList<XYChart.Series<String, BigDecimal>>result = PlannedBudgetWindowController.createLineChartData(mock, from, to, mock.getAccounts(), mock.getCategories(), mock.getTransactions(from,to,mock.getAccounts(), mock.getCategories()));
//
//        Assert.assertEquals(PropertiesUtil.REAL, result.get(0).getName());
//        Assert.assertEquals(PropertiesUtil.PLANNED, result.get(1).getName());
//        Assert.assertEquals(result.get(0).getData().size(), 6);
//        Assert.assertEquals(result.get(1).getData().size(), 6);
//
//        Assert.assertEquals(result.get(0).getData().get(3).getXValue(), "2016-04");
//        Assert.assertEquals(result.get(0).getData().get(3).getYValue(), BigDecimal.valueOf(1000));
//    }
//}