package pl.edu.agh.iisg.to.to2project.service;

import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.service.generic.CRUDService;

/**
 * Created by Patryk Skalski on 19/01/16.
 */
public interface BudgetService extends CRUDService<Category, Long>, IPlannedTransactionsDataSource {

    boolean canDelete(Category category);
}