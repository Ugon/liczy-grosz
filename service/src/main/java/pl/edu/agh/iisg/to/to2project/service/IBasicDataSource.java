package pl.edu.agh.iisg.to.to2project.service;

import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Wojciech Dymek on 06.01.2016.
 */
public interface IBasicDataSource {
    /**
     *
     * @return all accounts from data base
     */
    List<Account> getAccounts();

    /**
     *
     * @return list of all main categories (categories which do not have any ancestor in hierarchy)
     */
    List<Category> getCategories();

    /**
     *
     * @param dateFrom
     * @param dateTo
     * @param accounts
     * @param categories    warning: if category has subcategories, result should also contains their transactions
     * @return list of all transactions given during time (defined by dateFrom and dateTo, both inclusive), accounts, and categories (and their subcategories)
     */
    List<ExternalTransaction> getTransactions(LocalDate dateFrom, LocalDate dateTo, List<Account> accounts, List<Category> categories);

    /**
     *
     * @param account
     * @return current balance of account specified as a parameter
     */
    BigDecimal getCurrencBalance(Account account);
}