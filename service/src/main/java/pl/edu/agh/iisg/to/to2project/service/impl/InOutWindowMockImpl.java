package pl.edu.agh.iisg.to.to2project.service.impl;

import javafx.collections.ObservableList;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;
import pl.edu.agh.iisg.to.to2project.domain.entity.PlannedTransaction;
import pl.edu.agh.iisg.to.to2project.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Nesbite on 2015-11-25.
 */
@Service
public class InOutWindowMockImpl implements IInOutWindowMock {
    @Autowired
    private IBasicDataSource dataSource;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ExternalTransactionService transactionService;

    @Autowired
    private BudgetServiceImpl budgetService;


    public LocalDate makeLocalDate(String dateInString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date = LocalDate.parse(dateInString, formatter);
        return date;
    }

    public DateTime makeDateTimeFromLocalDate(LocalDate localDate) {
        DateTime dateTime = DateTime.parse(localDate.toString());
        return dateTime;
    }


    public LocalDate toLocalDate(DateTime dateTime){
        return LocalDate.of(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
    }


    public double getIncome(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories) {
        return dataSource.getTransactions(dateFrom, dateTo, accounts, categories).stream().filter(a -> a.deltaProperty().get().doubleValue() > 0).mapToDouble(a -> a.deltaProperty().get().doubleValue()).sum();
    }


    public double getOutgoings(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories) {
        return dataSource.getTransactions(dateFrom, dateTo, accounts, categories).stream().filter(a -> a.deltaProperty().get().doubleValue() < 0).mapToDouble(a -> a.deltaProperty().get().doubleValue()).sum();
    }

    private int compareDates(org.joda.time.LocalDate checkedDate, LocalDate rangeDate) {
        LocalDate localDateUnified = LocalDate.of(checkedDate.getYear(),checkedDate.getMonthOfYear(),checkedDate.getDayOfMonth());

        if(localDateUnified.isEqual(rangeDate)) {
            return 0;
        }

        return localDateUnified.isAfter(rangeDate) ? 1 : -1;
    }

    public Map<LocalDate, BigDecimal> getTransactionsPerDay(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories) {
        Map<LocalDate, BigDecimal> income = new TreeMap<>(Collections.reverseOrder());
        List<ExternalTransaction> transactions = dataSource.getTransactions(dateFrom,dateTo,accounts,categories);
        for (LocalDate iterDate = dateTo; iterDate.isAfter(dateFrom.minusDays(1)); iterDate = iterDate.minusDays(1)) {
            List<ExternalTransaction> transactionList = new LinkedList<>();
            for(ExternalTransaction transaction : transactions){
                if (compareDates(transaction.dateProperty().get(), iterDate) == 0){
                    transactionList.add(transaction);
                }
            }
            BigDecimal value = transactionList.stream()
                    .map(a -> a.deltaProperty().get())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);


                income.put(iterDate, value);
        }

        return income;
    }

    public Map<LocalDate, BigDecimal> getIncomePerDay(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories) {
        Map<LocalDate, BigDecimal> income = new TreeMap<>(Collections.reverseOrder());
        List<ExternalTransaction> transactions = dataSource.getTransactions(dateFrom,dateTo,accounts,categories);
        for (LocalDate iterDate = dateTo; iterDate.isAfter(dateFrom.minusDays(1)); iterDate = iterDate.minusDays(1)) {
            List<ExternalTransaction> transactionList = new LinkedList<>();
            for(ExternalTransaction transaction : transactions){
                if (compareDates(transaction.dateProperty().get(), iterDate) == 0){
                    transactionList.add(transaction);
                }
            }
            BigDecimal value = transactionList.stream()
                    .filter(a -> a.deltaProperty().get()
                            .compareTo(BigDecimal.ZERO) == 1)
                    .map(a -> a.deltaProperty().get().abs())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (value.compareTo(BigDecimal.ZERO) > 0)
                income.put(iterDate, value);
        }

        return income;
    }




    public Map<LocalDate, BigDecimal> getOutgoingsPerDay(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories) {
        Map<LocalDate, BigDecimal> outgoings = new TreeMap<>(Collections.reverseOrder());
        List<ExternalTransaction> transactions = dataSource.getTransactions(dateFrom,dateTo,accounts,categories);
        for (LocalDate iterDate = dateTo; iterDate.isAfter(dateFrom.minusDays(1)); iterDate = iterDate.minusDays(1)) {
            List<ExternalTransaction> transactionList = new LinkedList<>();
            for(ExternalTransaction transaction : transactions){
                if (compareDates(transaction.dateProperty().get(), iterDate) == 0){
                    transactionList.add(transaction);
                }
            }
            BigDecimal value = transactionList.stream()
                    .filter(a -> a.deltaProperty().get()
                            .compareTo(BigDecimal.ZERO) == -1)
                    .map(a -> a.deltaProperty().get().abs())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if ((value.compareTo(BigDecimal.ZERO) > 0) || iterDate.isEqual(dateFrom) || iterDate.isEqual(dateTo))
                outgoings.put(iterDate, value);
        }
        return outgoings;
    }


    public Category getCategoryByName(List<Category> categories, String categoryName) {
        for (Category category : categories) {
            if (category.nameProperty().get().equalsIgnoreCase(categoryName)) return category;
            if (!category.subCategoriesObservableSet().isEmpty()) {
                Category result = getCategoryByName(new ArrayList<>(category.subCategoriesObservableSet()), categoryName);
                if (result!=null) {
                    return result;
                }
            }
        }
        return null;
    }

    public Account getAccountByName(List<Account> accounts, String accountName) {
        for (Account account : accounts) {
            if (account.toString().equalsIgnoreCase(accountName)) return account;
        }
        return null;
    }

    public Map<LocalDate, BigDecimal> getPlannedIncomePerDay(LocalDate dateFrom, LocalDate dateTo, ObservableList<Category> categories) {
        Map<LocalDate, BigDecimal> income = new TreeMap<>(Collections.reverseOrder());
        List<PlannedTransaction> plannedTransactions = budgetService.getPlannedTransactions(dateFrom, dateTo, categories);
        for (LocalDate iterDate = dateTo; iterDate.isAfter(dateFrom.minusDays(1)); iterDate = iterDate.minusDays(1)) {
            List<PlannedTransaction> transactionList = new LinkedList<>();
            for(PlannedTransaction transaction : plannedTransactions){
                if (transaction.getDate().isEqual(iterDate)){
                    transactionList.add(transaction);
                }
            }
            BigDecimal value = transactionList.stream()
                    .map(a -> a.getValue().abs())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);


                income.put(iterDate, value);
        }

        return income;
    }
    
//    public Map<LocalDate, BigDecimal> getPlannedOutgoingsPerDay(LocalDate dateFrom, LocalDate dateTo, List<Category> categories) {
//        Map<LocalDate, BigDecimal> outgoings = new TreeMap<>(Collections.reverseOrder());
//        for (LocalDate iterDate = dateTo; iterDate.isAfter(dateFrom.minusDays(1)); iterDate = iterDate.minusDays(1)) {
//            BigDecimal value = budgetService.getPlannedTransactions(iterDate, iterDate, FXCollections.observableList(categories)).stream()
//                    .filter(a -> a.getValue()
//                            .compareTo(BigDecimal.ZERO) == 1)
//                    .map(a -> a.getValue().abs())
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//            if ((value.compareTo(BigDecimal.ZERO) > 0) || iterDate.isEqual(dateFrom) || iterDate.isEqual(dateTo))
//                outgoings.put(iterDate, value);
//        }
//        return outgoings;
//    }
}