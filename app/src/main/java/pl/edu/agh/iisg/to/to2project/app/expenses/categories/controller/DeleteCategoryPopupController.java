package pl.edu.agh.iisg.to.to2project.app.expenses.categories.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.Category;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;

/**
 * @author Bartłomiej Grochal
 */
@Controller
@Scope("prototype")
public class DeleteCategoryPopupController extends PopupController {

    @Autowired
    private CategoryService categoryService;

    @FXML
    private Text errorLabel;

    private Category deletedCategory;


    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        if(deletedCategory.subCategoriesObservableSet().isEmpty()) {
            if(deletedCategory.externalTransactionsObservableSet().isEmpty() && deletedCategory.internalTransactionObservableSet().isEmpty()) {
                categoryService.remove(deletedCategory);
                dialogStage.close();
            }
            else {
                errorLabel.setText("You are not able to delete this category, because it contains transactions.");
            }

        }
        else {
            errorLabel.setText("You are not able to delete this category, because it contains subcategories.");
        }
    }

    public void deleteCategory(Category category) {
        deletedCategory = category;
        showDialog();
    }
}
