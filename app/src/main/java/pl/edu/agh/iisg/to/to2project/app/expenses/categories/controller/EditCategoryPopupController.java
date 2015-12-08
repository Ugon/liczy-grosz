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
public class EditCategoryPopupController extends PopupController {

    private static final Category NO_CATEGORY = new Category("None");

    @Autowired
    private CategoryService categoryService;

    @FXML
    private TextField categoryNameTextField;

    @FXML
    private ComboBox<Category> parentCategoryCombo;

    @FXML
    private TextField descriptionTextField;

    private Category editedCategory;


    @FXML
    public void initialize() {
        parentCategoryCombo.getItems().addAll(categoryService.getList());
        parentCategoryCombo.getItems().add(NO_CATEGORY);
        parentCategoryCombo.setValue(NO_CATEGORY);
    }

    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        updateModel();
        dialogStage.close();
    }

    private void updateModel() {
        Category parentCategory = parentCategoryCombo.getSelectionModel().getSelectedItem();
        String categoryName = categoryNameTextField.getText();
        String description = descriptionTextField.getText();

        editedCategory.setName(categoryName);
        if(description.isEmpty()){
            editedCategory.removeDescriptionIfPresent();
        }
        else{
            editedCategory.setDescription(description);
        }

        if(parentCategory.equals(NO_CATEGORY)) {
            editedCategory.removeParentCategoryIfPresent();
        }
        else {
            editedCategory.removeParentCategoryIfPresent();
            parentCategory.addSubCategory(editedCategory);
        }

        categoryService.save(editedCategory);
    }

    private void adaptComboBox(Category category) {
        parentCategoryCombo.getItems().removeAll(category.deepSubCategoriesSet());
    }

    public void editCategory(Category category) {
        editedCategory = category;
        adaptComboBox(editedCategory);
        showDialog();
    }
}
