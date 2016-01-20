package pl.edu.agh.iisg.to.to2project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;
import pl.edu.agh.iisg.to.to2project.service.AccountService;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;
import pl.edu.agh.iisg.to.to2project.service.ExternalTransactionService;
import pl.edu.agh.iisg.to.to2project.service.IBasicDataSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Bartłomiej Grochal
 */
@Service
public class IBasicDataSourceImpl implements IBasicDataSource {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ExternalTransactionService transactionService;


    @Override
    public List<Account> getAccounts() {
        List<Account> returnList = new LinkedList<>();

        for(Account account : accountService.getList()) {
            returnList.add(account);
        }

        return returnList;
    }

    @Override
    public List<Category> getCategories() {
        List<Category> returnList = new LinkedList<>();

        for(Category category : categoryService.getList()) {
            if(category.parentCategoryMonadicProperty().isEmpty()) {
                returnList.add(category);
            }
        }

        return returnList;
    }

    @Override
    public List<ExternalTransaction> getTransactions(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories) {
        List<ExternalTransaction> returnList = new LinkedList<>();

        for(ExternalTransaction transaction : transactionService.getList()) {
            if(
                compareDates(transaction.dateProperty().get().toDate(), dateFrom) >= 0 &&
                compareDates(transaction.dateProperty().get().toDate(), dateTo) <= 0 &&
                accounts.contains(transaction.destinationAccountProperty().get()) &&
                isAllowedCategory(transaction.categoryMonadicProperty().get(), categories)
            ) {

                returnList.add(transaction);
            }
        }

        return returnList;
    }

    @Override
    public BigDecimal getCurrencBalance(Account account) {
        return account.currentBalanceProperty().get();
    }


    private int compareDates(Date checkedDate, LocalDate rangeDate) {
        Date rangeDateUnified = Date.from(rangeDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        if(checkedDate.equals(rangeDateUnified)) {
            return 0;
        }

        return checkedDate.after(rangeDateUnified) ? 1 : -1;
    }

    private boolean isAllowedCategory(Category category, List<Category> categories) {
        for(Category categoryItem : categories) {
            if(categoryItem.deepSubCategoriesSet().contains(category)) {
                return true;
            }
        }

        return false;
    }

}
