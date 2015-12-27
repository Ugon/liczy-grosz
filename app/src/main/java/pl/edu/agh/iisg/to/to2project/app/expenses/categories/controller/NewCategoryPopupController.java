package pl.edu.agh.iisg.to.to2project.app.expenses.categories.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.categories.controller.generic.AbstractCategoriesPopupController;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Controller
@Scope("prototype")
public class NewCategoryPopupController extends AbstractCategoriesPopupController {

    @Override
    protected Category produceCategory(String categoryName) {
        return new Category(categoryName);
    }

    public void addCategory() {
        showDialog();
    }
}
