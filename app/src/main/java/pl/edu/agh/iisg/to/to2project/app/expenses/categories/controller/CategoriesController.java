package pl.edu.agh.iisg.to.to2project.app.expenses.categories.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.categories.view.DeleteCategoryPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.categories.view.EditCategoryPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.categories.view.NewCategoryPopup;
import pl.edu.agh.iisg.to.to2project.domain.Category;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;

import java.util.Optional;

import static javafx.scene.control.SelectionMode.SINGLE;

/**
 * @author Bartłomiej Grochal
 */
@Controller
public class CategoriesController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private CategoryService categoryService;

    @FXML
    private TableView<Category> categoriesTable;

    @FXML
    private TableColumn<Category, String> nameColumn;

    @FXML
    private TableColumn<Category, String> parentColumn;

    @FXML
    private TableColumn<Category, Optional<String>> descriptionColumn;

    private ObservableList<Category> data;


    @FXML
    public void initialize() {
        data = categoryService.getList();
        categoriesTable.setItems(data);

        categoriesTable.getSelectionModel().setSelectionMode(SINGLE);

        nameColumn.setCellValueFactory(dataValue -> dataValue.getValue().nameProperty());
        parentColumn.setCellValueFactory(dataValue -> dataValue.getValue().parentCategoryProperty().getValue().get().nameProperty());
        descriptionColumn.setCellValueFactory(dataValue -> dataValue.getValue().descriptionProperty());
    }


    @FXML
    private void handleAddCategoryClick(ActionEvent actionEvent) {
        NewCategoryPopup popup = context.getBean(NewCategoryPopup.class);
        NewCategoryPopupController controller = popup.getController();

        controller.addCategory();
    }

    @FXML
    private void handleEditCategoryClick(ActionEvent actionEvent) {
        EditCategoryPopup popup = context.getBean(EditCategoryPopup.class);
        EditCategoryPopupController controller = popup.getController();

        Category selectedCategory = categoriesTable.getSelectionModel().getSelectedItem();
        if(selectedCategory != null) {
            controller.editCategory(selectedCategory);
        }
    }

    @FXML
    private void handleDeleteCategoryClick(ActionEvent actionEvent) {
        DeleteCategoryPopup popup = context.getBean(DeleteCategoryPopup.class);
        DeleteCategoryPopupController contoller = popup.getController();

        Category selectedCategory = categoriesTable.getSelectionModel().getSelectedItem();
        if(selectedCategory != null) {
            contoller.deleteCategory(selectedCategory);
        }
    }
}
