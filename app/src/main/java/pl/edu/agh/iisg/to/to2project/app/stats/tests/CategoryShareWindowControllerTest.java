//package pl.edu.agh.iisg.to.to2project.app.stats.tests;
//
//import javafx.collections.ObservableList;
//import javafx.scene.chart.PieChart;
//import org.testng.Assert;
//import org.testng.annotations.Test;
//import pl.edu.agh.iisg.to.to2project.app.stats.category_share_graph_panel.controller.CategoryShareWindowController;
//import pl.edu.agh.iisg.to.to2project.app.stats.tests.InOutWindowMock;
//import pl.edu.agh.iisg.to.to2project.service.IInOutWindowMock;
//import pl.edu.agh.iisg.to.to2project.service.impl.InOutWindowMockImpl;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * Created by Wojciech Dymek on 19.01.2016.
// */
//public class CategoryShareWindowControllerTest {
//    private InOutWindowMock mock = InOutWindowMock.getInstance();
//    private LocalDate from = LocalDate.of(2016, 1, 1);
//    private LocalDate to = LocalDate.of(2016, 6, 1);
//
//    @Test
//    public void testCreatePieChartDataNotNull() throws Exception {
//        List transactions = mock.getTransactions(from, to, mock.getAccounts(), mock.getCategories()).stream()
//                .filter(a -> a.deltaProperty().get().doubleValue() < 0)
//                .collect(Collectors.toList());
//        ObservableList<PieChart.Data> result = CategoryShareWindowController.createPieChartData(mock, from, to, mock.getAccounts(), mock.getCategories(), transactions, true);
//
//        Assert.assertNotNull(result);
//        Assert.assertFalse(result.isEmpty());
//        Assert.assertTrue(result.size() == 4);
//    }
//
//    @Test
//    public void testContent() throws Exception {
//        List transactions = mock.getTransactions(from, to, mock.getAccounts(), mock.getCategories()).stream()
//                .filter(a -> a.deltaProperty().get().doubleValue() < 0)
//                .collect(Collectors.toList());
//        ObservableList<PieChart.Data> result = CategoryShareWindowController.createPieChartData(mock, from, to, mock.getAccounts(), mock.getCategories(), transactions, true);
//
//        PieChart.Data cat1 = null;
//        PieChart.Data cat2 = null;
//        for (int i=0; i<result.size(); i++) {
//            if (result.get(i).getName().equalsIgnoreCase("Kategoria nr 1"))
//                cat1 = result.get(i);
//            else if (result.get(i).getName().equalsIgnoreCase("Kategoria nr 2"))
//                cat2 = result.get(i);
//        }
//        Assert.assertEquals("Kategoria nr 1", cat1.getName());
//        Assert.assertEquals("Kategoria nr 2", cat2.getName());
//        Assert.assertEquals(cat1.getPieValue(), 1228.0);
//        Assert.assertEquals(cat2.getPieValue(), 1252.0);
//    }
//}
