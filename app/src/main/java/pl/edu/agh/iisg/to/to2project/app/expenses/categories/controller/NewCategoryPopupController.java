package pl.edu.agh.iisg.to.to2project.app.expenses.categories.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
public class NewCategoryPopupController extends PopupController {

    @Autowired
    private CategoryService categoryService;

    @FXML
    private TextField categoryNameTextField;

    @FXML
    private ComboBox parentCategoryCombo;

    @FXML
    private TextField descriptionTextField;

    private Category defaultCategory;
    private Category newCategory;


    @FXML
    public void initialize() {
        defaultCategory = new Category("None");

        parentCategoryCombo.getItems().addAll(categoryService.getList());
        parentCategoryCombo.getItems().add(defaultCategory);
        parentCategoryCombo.setValue(defaultCategory);
    }

    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        updateModel();
        dialogStage.close();
    }

    private void updateModel() {
        Category parentCategory = (Category) parentCategoryCombo.getSelectionModel().getSelectedItem();
        String categoryName = categoryNameTextField.getText();
        String description = descriptionTextField.getText();

        if(!parentCategory.equals(defaultCategory)) {
            newCategory = new Category(categoryName, parentCategory, description);
        }
        else {
            newCategory = new Category(categoryName, description);
        }

        categoryService.save(newCategory);
    }

    public void addCategory() {
        showDialog();
    }
}
