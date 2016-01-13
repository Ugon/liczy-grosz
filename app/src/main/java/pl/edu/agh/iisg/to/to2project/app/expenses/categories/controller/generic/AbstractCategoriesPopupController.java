package pl.edu.agh.iisg.to.to2project.app.expenses.categories.controller.generic;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes.ColorfulValidatingTextField;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;

/**
 * @author Wojciech Pachuta.
 */
@Controller
@Scope("prototype")
public abstract class AbstractCategoriesPopupController extends PopupController {

    private static final Category NO_CATEGORY = new Category("None");

    @Autowired
    private CategoryService categoryService;

    @FXML
    protected ColorfulValidatingTextField nameTextField;

    @FXML
    protected ComboBox<Category> parentCategoryComboBox;

    @FXML
    protected TextField descriptionTextField;

    private ObservableList<Category> currentCategories;

    @FXML
    public void initialize() {
        currentCategories = categoryService.getList();

        parentCategoryComboBox.getItems().addAll(categoryService.getList());
        parentCategoryComboBox.getItems().add(NO_CATEGORY);
        parentCategoryComboBox.setValue(NO_CATEGORY);

        nameTextField.setValidationSupplier(this::isNameValid);
    }

    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        if (isInputValid()) {
            updateModel();
            closeDialog();
        }
    }

    private boolean isInputValid() {
        return isNameValid();
    }

    protected boolean isNameValid() {
        return !nameTextField.getText().isEmpty() &&
                currentCategories.filtered(cat -> cat.nameProperty().get().equals(nameTextField.getText())).isEmpty();
    }

    private void updateModel() {
        String categoryName = nameTextField.getText();

        Category category = produceCategory(categoryName);

        String description = descriptionTextField.getText();
        if (description.isEmpty()) {
            category.removeDescriptionIfPresent();
        } else {
            category.setDescription(description);
        }

        Category parentCategory = parentCategoryComboBox.getSelectionModel().getSelectedItem();
        category.removeParentCategoryIfPresent();
        if (!parentCategory.equals(NO_CATEGORY)) {
            category.removeParentCategoryIfPresent();
            parentCategory.addSubCategory(category);
        }

        categoryService.save(category);
    }

    protected abstract Category produceCategory(String categoryName);

}
