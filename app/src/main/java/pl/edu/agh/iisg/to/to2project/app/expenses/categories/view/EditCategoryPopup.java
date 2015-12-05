package pl.edu.agh.iisg.to.to2project.app.expenses.categories.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.categories.controller.EditCategoryPopupController;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;

/**
 * @author Bartłomiej Grochal
 */
@Component
@Scope("prototype")
public class EditCategoryPopup extends Popup<EditCategoryPopupController> {

    @Override
    public String getPopupTitle() {
        return "Edit Selected Category.";
    }

    @Override
    public String getFullyQualifiedResource() {
        return "/pl/edu/agh/iisg/to/to2project/app/expenses/categories/view/EditCategoryPopupView.fxml";
    }

}
