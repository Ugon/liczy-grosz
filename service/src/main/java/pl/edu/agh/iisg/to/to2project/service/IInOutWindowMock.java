package pl.edu.agh.iisg.to.to2project.service;

import javafx.collections.ObservableList;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by Nesbite on 2016-01-20.
 */
public interface IInOutWindowMock {
    Map<LocalDate, BigDecimal> getIncomePerDay(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories);
    Map<LocalDate, BigDecimal> getTransactionsPerDay(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories);
    Map<LocalDate, BigDecimal> getOutgoingsPerDay(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories);
    Category getCategoryByName(List<Category> categories, String categoryName);
    Map<LocalDate, BigDecimal> getPlannedIncomePerDay(LocalDate dateFrom, LocalDate dateTo, ObservableList<Category> categories);

}
