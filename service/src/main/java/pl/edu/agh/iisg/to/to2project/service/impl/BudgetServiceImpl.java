package pl.edu.agh.iisg.to.to2project.service.impl;

import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.iisg.to.to2project.budget_persistence.BudgetPersistenceManager;
import pl.edu.agh.iisg.to.to2project.budget_persistence.Plan;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.PlannedTransaction;
import pl.edu.agh.iisg.to.to2project.service.BudgetService;
import pl.edu.agh.iisg.to.to2project.service.IPlannedTransactionsDataSource;
import pl.edu.agh.iisg.to.to2project.service.generic.CRUDServiceGeneric;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Patryk Skalski on 18/01/16.
 */
@Service
public class BudgetServiceImpl extends CRUDServiceGeneric<Category, Long> implements BudgetService,IPlannedTransactionsDataSource {


    @Override
    public List<PlannedTransaction> getPlannedTransactions(LocalDate dateFrom, LocalDate dateTo, ObservableList<Category> categories) {
        List<PlannedTransaction> plannedTransactions = new ArrayList<>();
        List<Plan> plansForTimeInterval = null;
        try {
            plansForTimeInterval = BudgetPersistenceManager.getPlansForTimeInterval(dateFrom.getYear(), dateFrom.getMonthValue(), dateTo.getYear(), dateTo.getMonthValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<String> searchedCategorNames = plansForTimeInterval.stream().map(e -> e.getCategoryName()).collect(Collectors.toList());

        if (searchedCategorNames == null) return plannedTransactions;

        plansForTimeInterval.removeIf(p -> !searchedCategorNames.contains(p.getCategoryName()));

        for (Plan plan : plansForTimeInterval)
            {
                Category category = categories.filtered(c -> c.nameProperty().get().equals(plan.getCategoryName())).get(0);
                PlannedTransaction earningTransaction = new PlannedTransaction(plan.getDate(), plan.getEarningPlanValue(),category);
                PlannedTransaction spendingTransaction = new PlannedTransaction(plan.getDate(), plan.getSpendingPlanValue(), category);
                plannedTransactions.add(earningTransaction);
                plannedTransactions.add(spendingTransaction);
            }
        for (Plan plan : plansForTimeInterval)
        {
            System.out.println("PrintPlan" + plan.getCategoryName() + " " + plan.getDate() + " " + plan.getEarningPlanValue() + " " + plan.getSpendingPlanValue());
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
