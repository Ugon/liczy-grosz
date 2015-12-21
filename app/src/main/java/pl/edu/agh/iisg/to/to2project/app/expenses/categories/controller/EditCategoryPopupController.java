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
public class EditCategoryPopupController extends AbstractCategoriesPopupController {

    private Category category;

    @Override
    protected Category produceCategory(String categoryName) {
        category.setName(categoryName);
        return category;
    }

    @Override
    protected boolean isNameValid() {
        return nameTextField.getText().equals(category.nameProperty().get()) || super.isNameValid();
    }

    private void adaptComboBox() {
        parentCategoryComboBox.getItems().removeAll(category.deepSubCategoriesSet());
    }

    private void fillDialog() {
        nameTextField.setText(category.nameProperty().getValue());
        if(category.parentCategoryMonadicProperty().isPresent()){
            parentCategoryComboBox.setValue(category.parentCategoryMonadicProperty().getValue());
        }
        if(category.descriptionMonadicProperty().isPresent()){
            descriptionTextField.setText(category.descriptionMonadicProperty().get());
        }
    }

    public void editCategory(Category category) {
        this.category = category;
        adaptComboBox();
        fillDialog();
        showDialog();
    }

}
