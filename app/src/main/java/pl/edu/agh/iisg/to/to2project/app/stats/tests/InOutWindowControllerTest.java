package pl.edu.agh.iisg.to.to2project.app.stats.tests;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.testng.Assert;
import org.testng.annotations.Test;
import pl.edu.agh.iisg.to.to2project.app.stats.inout_graph_panel.controller.InOutWindowController;
import pl.edu.agh.iisg.to.to2project.app.stats.util.PropertiesUtil;
import pl.edu.agh.iisg.to.to2project.service.impl.InOutWindowMockImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Wojciech Dymek on 19.01.2016.
 */
public class InOutWindowControllerTest {
    private InOutWindowMock mock = InOutWindowMock.getInstance();
    private InOutWindowMockImpl mock2 = new InOutWindowMockImpl();
    private LocalDate from = LocalDate.of(2016, 1, 1);
    private LocalDate to = LocalDate.of(2016, 5, 30);

    @Test
    public void testCreateLineChartDataNotNull() throws Exception {
        ObservableList<XYChart.Series<String, BigDecimal>> result = InOutWindowController.createLineChartData(mock, from, to, mock.getAccounts(), mock.getCategories());

        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        Assert.assertTrue(result.size() == 2);
    }

    @Test
    public void testContent() throws Exception {
        ObservableList<XYChart.Series<String, BigDecimal>> result = InOutWindowController.createLineChartData(mock, from, to, mock.getAccounts(), mock.getCategories());

        Assert.assertEquals(PropertiesUtil.INCOMES, result.get(0).getName());
        Assert.assertEquals(PropertiesUtil.OUTCOMES, result.get(1).getName());
        Assert.assertEquals(result.get(0).getData().size(), 22);
        Assert.assertEquals(result.get(1).getData().size(), 22);
        Assert.assertEquals(result.get(0).getData().get(3).getXValue(), "2016-01 week 4");
        Assert.assertEquals(result.get(0).getData().get(3).getYValue(), BigDecimal.valueOf(232));


    }
}
