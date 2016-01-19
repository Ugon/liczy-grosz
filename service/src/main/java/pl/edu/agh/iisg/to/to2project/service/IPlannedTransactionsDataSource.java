package pl.edu.agh.iisg.to.to2project.service;

import javafx.collections.ObservableList;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.PlannedTransaction;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Wojciech Dymek on 07.01.2016.
 */
public interface IPlannedTransactionsDataSource {
    /**
     *
     * @param dateFrom
     * @param dateTo
     * @param categories    warning: if category has subcategories, result should also contains their transactions
     * @return list of all planned transactions given during time (defined by dateFrom and dateTo, both inclusive), accounts, and categories (and their subcategories)
     */
    List<PlannedTransaction> getPlannedTransactions(LocalDate dateFrom, LocalDate dateTo, ObservableList<Category> categories);
}
