package pl.edu.agh.iisg.to.to2project.service.impl;

import javafx.collections.ObservableList;
import pl.edu.agh.iisg.to.to2project.budget_persistence.BudgetPersistenceManager;
import pl.edu.agh.iisg.to.to2project.budget_persistence.Plan;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.PlannedTransaction;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;
import pl.edu.agh.iisg.to.to2project.service.IPlannedTransactionsDataSource;
import pl.edu.agh.iisg.to.to2project.service.generic.CRUDServiceGeneric;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 18/01/16.
 */
public class BudgetServiceImpl extends CRUDServiceGeneric<Category, Long> implements CategoryService,IPlannedTransactionsDataSource {


    @Override
    public List<PlannedTransaction> getPlannedTransactions(LocalDate dateFrom, LocalDate dateTo, ObservableList<Category> categories) {
        List<PlannedTransaction> plannedTransactions = new ArrayList<PlannedTransaction>();
        List<Plan> plansForTimeInterval = null;
        try {
            plansForTimeInterval = BudgetPersistenceManager.getPlansForTimeInterval(dateFrom.getYear(), dateFrom.getMonthValue(), dateTo.getYear(), dateTo.getMonthValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Plan plan : plansForTimeInterval)
            {
                PlannedTransaction earningTransaction = new PlannedTransaction(plan.getDate(), plan.getEarningPlanValue(),  categories.filtered(c -> c.nameProperty().get().equals(plan.getCategoryName())).get(0) );
                PlannedTransaction spendingTransaction = new PlannedTransaction(plan.getDate(), plan.getSpendingPlanValue(),  categories.filtered(c -> c.nameProperty().get().equals(plan.getCategoryName())).get(0) );
                plannedTransactions.add(earningTransaction);
                plannedTransactions.add(spendingTransaction);
            }
        return plannedTransactions;
    }

    @Override
    public boolean canDelete(Category category) {
        boolean canDelete = true;
        String categoryName = category.nameProperty().get();
        try {
            canDelete =  !BudgetPersistenceManager.doesPlanForCategoryExist(categoryName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return canDelete;
    }
}
