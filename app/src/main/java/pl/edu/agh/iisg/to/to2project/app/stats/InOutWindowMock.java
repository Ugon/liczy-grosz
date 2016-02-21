package pl.edu.agh.iisg.to.to2project.app.stats;


import javafx.collections.FXCollections;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;
import pl.edu.agh.iisg.to.to2project.domain.entity.PlannedTransaction;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Nesbite on 2015-11-25.
 */
public class InOutWindowMock {
    private List<PlannedTransaction> plannedTransactions;
    private List<ExternalTransaction> transactions;
    private List<Category> cat;
    private List<Account> accounts;

    private InOutWindowMock() {
        this.plannedTransactions = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.cat = new ArrayList<>();
        makeAccounts(6);
        makeCategories(8);
        makeTransactions(150);
        makePlannedTransactions(100);
    }

    private static class InOutWindowMockHolder {
        private final static InOutWindowMock instance = new InOutWindowMock();
    }

    public static InOutWindowMock getInstance() {
        return InOutWindowMockHolder.instance;
    }

    public List<Category> getCategories() {
        return this.cat;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }



    public void makeAccounts(int n) {
        for (int i = 1; i < n; i++) {
            accounts.add(new Account("Konto nr " + i, BigDecimal.ONE));
        }
    }


    public void makeCategories(int n) {
        for (int i = 1; i < n; i++) {
            Category category = new Category("Kategoria nr " + i);
            for (int k = 1; k < 7; k++) {
                Category cat = new Category("[cat" + i + "] Podkategoria nr " + k);

                category.addSubCategory(cat);
            }

            cat.add(category);
        }


    }

    public Category getParentCategory(Category category) {
        List<Category> categories = getCategories().stream()
                .filter(a -> (a.subCategoriesObservableSet().isEmpty() ? a.equals(category) : a.subCategoriesObservableSet().contains(category)))
                .collect(Collectors.toList());
        return getCategories().contains(category) ? category : (categories.isEmpty() ? null : categories.get(0));
    }



    public void makeTransactions(int n) {
        for (int i = 0; i < n; i++) {
            int categoryNo = i%cat.size();
            String date = "2016-" + new DecimalFormat("00").format(i%12 + 1) + "-" + new DecimalFormat("00").format(i%28 + 1);
            transactions.add(new ExternalTransaction("asdasda", accounts.get(i%accounts.size()),
                    (categoryNo < (cat.size() / 2 + 1)) ? (new BigDecimal(i%200 + 50).negate())
                            : (new BigDecimal(i%200 + 50)), org.joda.time.LocalDate.now()));
        }
    }
    public void makePlannedTransactions(int n) {
        for (int i = 0; i < n; i++) {
            int categoryNo = i%cat.size();
            String date = "2015-" + new DecimalFormat("00").format(i%12 + 1) + "-" + new DecimalFormat("00").format(i%28 + 1);
            plannedTransactions.add(new PlannedTransaction(makeDate(date, "yyyy-MM-dd"),
                    (categoryNo < (cat.size() / 2 + 1)) ? (new BigDecimal(i%200 + 50).negate())
                            : (new BigDecimal(i%200 + 50)),
                    (cat.get(categoryNo).subCategoriesObservableSet().isEmpty() ? cat.get(categoryNo) : (new LinkedList<>(cat.get(categoryNo).subCategoriesObservableSet())
                            .get(i%(cat.get(categoryNo)
                                    .subCategoriesObservableSet().size()))))));
        }
    }


    public LocalDate makeDate(String dateInString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date = LocalDate.parse(dateInString, formatter);
        return date;
    }

    public List<ExternalTransaction> getTransactions(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories) {

        List<ExternalTransaction> selectedTransactions = new ArrayList<>();
        for (Account account : accounts) {
            for (Category category : categories) {
                selectedTransactions.addAll(transactions.stream()
                        .filter(a -> a.toString().equals(account.toString()))
                        .filter(a -> (category.subCategoriesObservableSet().isEmpty() ? (a.categoryMonadicProperty().get().equals(category.nameProperty().get())) : (category.subCategoriesObservableSet().stream().map(b -> b.nameProperty().get()).collect(Collectors.toList()).contains(a.categoryMonadicProperty().get()))))
                        .filter(a -> compareDates(a.dateProperty().get(), dateFrom.minusDays(1)) ==1)
                        .filter(a -> compareDates(a.dateProperty().get(), dateFrom.plusDays(1)) == -1)
                        .collect(Collectors.toList()));
            }
        }
        return selectedTransactions;
    }



    public List<PlannedTransaction> getPlannedTransactions(LocalDate dateFrom, LocalDate dateTo, List<Category> categories) {

        List<PlannedTransaction> selectedTransactions = new ArrayList<>();
        for (Category category : categories) {
            selectedTransactions.addAll(plannedTransactions.stream()
                    .filter(a -> (category.subCategoriesObservableSet().isEmpty() ? (a.getCategory().nameProperty().get().equals(category.nameProperty().get())) : (category.subCategoriesObservableSet().stream().map(b -> b.nameProperty().get()).collect(Collectors.toList()).contains(a.getCategory().nameProperty().get()))))
                    .filter(a -> a.getDate().isAfter(dateFrom.minusDays(1)))
                    .filter(a -> a.getDate().isBefore(dateTo.plusDays(1)))
                    .collect(Collectors.toList()));
        }

        return selectedTransactions;
    }

    private int compareDates(org.joda.time.LocalDate checkedDate, LocalDate rangeDate) {
        LocalDate localDateUnified = LocalDate.of(checkedDate.getYear(),checkedDate.getMonthOfYear(),checkedDate.getDayOfMonth());

        if(localDateUnified.isEqual(rangeDate)) {
            return 0;
        }

        return localDateUnified.isAfter(rangeDate) ? 1 : -1;
    }


    public double getIncome(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories) {
        return getTransactions(dateFrom, dateTo, accounts, categories).stream().filter(a -> a.deltaProperty().get().doubleValue() > 0).mapToDouble(a -> a.deltaProperty().get().doubleValue()).sum();
    }


    public double getOutgoings(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories) {
        return getTransactions(dateFrom, dateTo, accounts, categories).stream().filter(a -> a.deltaProperty().get().doubleValue() < 0).mapToDouble(a -> a.deltaProperty().get().abs().doubleValue()).sum();
    }



    public Map<LocalDate, BigDecimal> getIncomePerDay(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories) {
        Map<LocalDate, BigDecimal> income = new TreeMap<>(Collections.reverseOrder());
        for (LocalDate iterDate = dateTo; iterDate.isAfter(dateFrom.minusDays(1)); iterDate = iterDate.minusDays(1)) {
            BigDecimal value = getTransactions(iterDate, iterDate, accounts, categories).stream()
                    .filter(a -> a.deltaProperty().get()
                            .compareTo(BigDecimal.ZERO) == -1)
                    .map(a -> a.deltaProperty().get().abs())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (value.compareTo(BigDecimal.ZERO) > 0)
                income.put(iterDate, value);
        }

        return income;
    }


    public Map<LocalDate, BigDecimal> getOutgoingsPerDay(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories) {
        Map<LocalDate, BigDecimal> outgoings = new TreeMap<>(Collections.reverseOrder());
        for (LocalDate iterDate = dateTo; iterDate.isAfter(dateFrom.minusDays(1)); iterDate = iterDate.minusDays(1)) {
            BigDecimal value = getTransactions(iterDate, iterDate, accounts, categories).stream()
                    .filter(a -> a.deltaProperty().get()
                            .compareTo(BigDecimal.ZERO) == 1)
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
                Category result = getCategoryByName(new LinkedList<>(category.subCategoriesObservableSet()), categoryName);
                if (result!=null) {
                    return result;
                }
            }
        }
        return null;
    }

    public Account getAccountByName(List<Account> accounts, String accountName) {
        for (Account account : accounts) {
            if (account.nameProperty().get().equalsIgnoreCase(accountName)) return account;
        }
        return null;
    }


    public List<Category> getIncomeCategories() {
        return getCategories();
    }


    public List<Category> getOutgoingsCategories() {
        return getCategories();
    }


    public Map<LocalDate, BigDecimal> getPlannedIncomePerDay(LocalDate dateFrom, LocalDate dateTo, List<Category> categories) {
        Map<LocalDate, BigDecimal> income = new TreeMap<>(Collections.reverseOrder());
        for (LocalDate iterDate = dateTo; iterDate.isAfter(dateFrom.minusDays(1)); iterDate = iterDate.minusDays(1)) {
            BigDecimal value = getPlannedTransactions(iterDate, iterDate, categories).stream()
                    .filter(a -> a.getValue()
                            .compareTo(BigDecimal.ZERO) == -1)
                    .map(a -> a.getValue().abs())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (value.compareTo(BigDecimal.ZERO) > 0)
                income.put(iterDate, value);
        }

        return income;
    }

    public Map<LocalDate, BigDecimal> getPlannedOutgoingsPerDay(LocalDate dateFrom, LocalDate dateTo, List<Category> categories) {
        Map<LocalDate, BigDecimal> outgoings = new TreeMap<>(Collections.reverseOrder());
        for (LocalDate iterDate = dateTo; iterDate.isAfter(dateFrom.minusDays(1)); iterDate = iterDate.minusDays(1)) {
            BigDecimal value = getPlannedTransactions(iterDate, iterDate, categories).stream()
                    .filter(a -> a.getValue()
                            .compareTo(BigDecimal.ZERO) == 1)
                    .map(a -> a.getValue().abs())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if ((value.compareTo(BigDecimal.ZERO) > 0) || iterDate.isEqual(dateFrom) || iterDate.isEqual(dateTo))
                outgoings.put(iterDate, value);
        }
        return outgoings;
    }
}