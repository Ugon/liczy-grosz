package pl.edu.agh.iisg.to.to2project.expenses.categories.view;

import javafx.fxml.FXMLLoader;
import pl.edu.agh.iisg.to.to2project.expenses.common.view.Popup;

/**
 * @author Bartłomiej Grochal
 */
public class AddCategoryPopup extends Popup {

    @Override
    public FXMLLoader getFXMLLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AddCategoryPopup.class.getResource("AddCategoryPopupView.fxml"));

        return loader;
    }

    @Override
    public String getPopupTitle() {
        return "Add New Category.";
    }
}
