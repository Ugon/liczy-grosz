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

    private static final Category NO_CATEGORY = new Category("None");

    @Autowired
    private CategoryService categoryService;

    @FXML
    private TextField categoryNameTextField;

    @FXML
    private ComboBox<Category> parentCategoryCombo;

    @FXML
    private TextField descriptionTextField;

    private Category newCategory;


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
        String categoryName = categoryNameTextField.getText();
        Category parentCategory = parentCategoryCombo.getSelectionModel().getSelectedItem();
        String description = descriptionTextField.getText();

        newCategory = new Category(categoryName);
        if(!parentCategory.equals(NO_CATEGORY)){
            parentCategory.addSubCategory(newCategory);
        }
        if(!description.isEmpty()){
            newCategory.setDescription(description);
        }

        categoryService.save(newCategory);
    }

    public void addCategory() {
        showDialog();
    }
}
