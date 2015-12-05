package pl.edu.agh.iisg.to.to2project.app.expenses.categories.view;

import pl.edu.agh.iisg.to.to2project.app.core.utils.SpringFXMLLoader;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;

/**
 * @author Bartłomiej Grochal
 */
public class AddCategoryPopup extends Popup {

    @Override
    public String getPopupTitle() {
        return "Add New Category.";
    }

    @Override
    public String getFullyQualifiedResource() {
        return "AddCategoryPopupView.fxml";
    }

}
