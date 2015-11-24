package pl.edu.agh.iisg.to.to2project.expenses.categories.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import pl.edu.agh.iisg.to.to2project.expenses.categories.view.AddCategoryPopup;
import pl.edu.agh.iisg.to.to2project.expenses.categories.view.DeleteCategoryPopup;
import pl.edu.agh.iisg.to.to2project.expenses.categories.view.EditCategoryPopup;

/**
 * @author Bartłomiej Grochal
 */
public class CategoryButtonsHandlers {

    @FXML
    private void handleAddCategoryClick(ActionEvent actionEvent) {
        new AddCategoryPopup();
    }

    @FXML
    private void handleEditCategoryClick(ActionEvent actionEvent) {
        new EditCategoryPopup();
    }

    @FXML
    private void handleDeleteCategoryClick(ActionEvent actionEvent) {
        new DeleteCategoryPopup();
    }
}
