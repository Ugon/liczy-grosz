package pl.edu.agh.iisg.to.to2project.app.stats.tests;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.testng.Assert;
import org.testng.annotations.Test;

import pl.edu.agh.iisg.to.to2project.app.stats.planned_budget_graph_panel.controller.PlannedBudgetWindowController;
import pl.edu.agh.iisg.to.to2project.app.stats.util.PropertiesUtil;
import pl.edu.agh.iisg.to.to2project.service.IInOutWindowMock;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Wojciech Dymek on 17.01.2016.
 */
public class PlannedBudgetWindowControllerTest {
    private IInOutWindowMock mock = InOutWindowMock.getInstance();
    private InOutWindowMock mock2 = InOutWindowMock.getInstance();
    private LocalDate from = LocalDate.of(2016, 1, 1);
    private LocalDate to = LocalDate.of(2016, 6, 1);

    @Test
    public void testCreateLineChartDataNotNull() throws Exception {
        ObservableList<XYChart.Series<String, BigDecimal>> result = PlannedBudgetWindowController.createLineChartData(mock, from, to, mock2.getAccounts(), FXCollections.observableList(mock2.getCategories()));

        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        Assert.assertTrue(result.size() == 2);
    }

    @Test
    public void testContent() throws Exception {
        ObservableList<XYChart.Series<String, BigDecimal>>result = PlannedBudgetWindowController.createLineChartData(mock, from, to, mock2.getAccounts(), FXCollections.observableList(mock2.getCategories()));

        Assert.assertEquals(PropertiesUtil.REAL, result.get(0).getName());
        Assert.assertEquals(PropertiesUtil.PLANNED, result.get(1).getName());
        Assert.assertEquals(result.get(0).getData().size(), 6);
        Assert.assertEquals(result.get(1).getData().size(), 6);

        Assert.assertEquals(result.get(1).getData().get(3).getXValue(), "2016-04");
        Assert.assertEquals(result.get(1).getData().get(3).getYValue(), BigDecimal.valueOf(1000));
    }
}