package pl.edu.agh.iisg.to.to2project.app.stats.util;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.service.IBasicDataSource;
import pl.edu.agh.iisg.to.to2project.service.impl.InOutWindowMockImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Wojciech Dymek on 15.12.2015.
 */

@Component
public class CategoryTreeProviderUtil {
    @Autowired
    private IBasicDataSource expensesDataSource;

    @Autowired
    private InOutWindowMockImpl dataSource;
//    private static InOutWindowMock mock = InOutWindowMock.getInstance();
    private CheckMenuItem source;

    private List<Category> allCategories = new LinkedList<>();


    public void selectCategoriesInMenu(MenuButton button, CheckMenuItem eventSource, List<CategoryAndCheckMenuItem> map){
        getAllCategoriesFromProvider();
        source = eventSource;
        if (Objects.isNull(map)) {
            init(button);
        }

        if (source.getText().equalsIgnoreCase(PropertiesUtil.ALL)) {
            if (source.isSelected())
                selectAll(button);
            else
                unselectAll(button);
            clearCategories();
            return;
        }

        unselectSubcategoriesIfCategoryIsUnselected(map);
        selectCategoryIfAllSubcategoriesAreSelected(map);
        unselectCategoryIfSubcategoryIsUnselected(map);
        selectAllSubcategoriesIfCategoryIsSelected(map);

        if (!getButtonText(map).equalsIgnoreCase(PropertiesUtil.ALL)) {
            for (MenuItem menuItem : button.getItems()) {
                if (menuItem.getText().equalsIgnoreCase(PropertiesUtil.ALL)) {
                    ((CheckMenuItem)menuItem).setSelected(false);
                    break;
                }
            }
        } else {
            for (MenuItem menuItem : button.getItems()) {
                if (menuItem.getText().equalsIgnoreCase(PropertiesUtil.ALL)) {
                    ((CheckMenuItem)menuItem).setSelected(true);
                    break;
                }
            }
        }
        clearCategories();
    }
    public List<Category> getSelectedCategories(List<CategoryAndCheckMenuItem> map){
        if (getButtonText(map).equalsIgnoreCase(PropertiesUtil.ALL)) {
            return new LinkedList<>(expensesDataSource.getCategories());
        }

        getAllCategoriesFromProvider();
        List<Category> result = new ArrayList<>();

        for (Category category : allCategories) {
            if (getObjectFromCategory(category, map).menuItem.isSelected()) {
                result.add(category);
            } else {
                List<Category> subcategories = new LinkedList<>(category.subCategoriesObservableSet());
                if (subcategories != null) {
                    for (Category subcategory : subcategories) {
                        if (getObjectFromCategory(subcategory, map).menuItem.isSelected()) {
                            result.add(subcategory);
                        }
                    }
                }
            }
        }

        clearCategories();
        return result;
    }
    public String getButtonText(List<CategoryAndCheckMenuItem> map){
        String result = "";

        int howManyAreSelected = 0;

        for (CategoryAndCheckMenuItem categoryAndCheckMenuItem : map) {
            if (categoryAndCheckMenuItem.menuItem.isSelected()) {
                result = categoryAndCheckMenuItem.category.nameProperty().get();
                howManyAreSelected++;
            }
        }

        if (howManyAreSelected == 0) {
            return PropertiesUtil.EMPTY;
        } else if (howManyAreSelected == 1) {
            return result;
        } else if (howManyAreSelected != map.size()) {
            return PropertiesUtil.SOME;
        } else {
            return PropertiesUtil.ALL;
        }
    }

    private void getAllCategoriesFromProvider() {
        if (allCategories.isEmpty()) {
            allCategories = new LinkedList<>(expensesDataSource.getCategories());
        }
    }
    private void clearCategories() {
        if (!allCategories.isEmpty()) {
            allCategories.clear();
        }
    }

    public List<CategoryAndCheckMenuItem> init(MenuButton button) {
        getAllCategoriesFromProvider();
        List<CategoryAndCheckMenuItem> result = getCategoryAndCheckMenuItems(button);
//        clearCategories();
        return result;
    }

    private void unselectAll(List<CategoryAndCheckMenuItem> map){
        map.forEach(item -> item.menuItem.setSelected(false));
    }

    private void selectAll(List<CategoryAndCheckMenuItem> map){
        map.forEach(item -> item.menuItem.setSelected(true));
    }

    private void unselectSubcategoriesIfCategoryIsUnselected(List<CategoryAndCheckMenuItem> map){
        if (source.isSelected())
            return;

        allCategories.forEach(category -> {
            if (category.nameProperty().get().equalsIgnoreCase(source.getText())) {
                List<Category> subcategories = new LinkedList<Category>(category.subCategoriesObservableSet());
                if (subcategories != null) {
                    subcategories.forEach(subcategory -> getObjectFromCategory(subcategory, map).menuItem.setSelected(false));
                }
            }
        });
    }

    private void selectAllSubcategoriesIfCategoryIsSelected(List<CategoryAndCheckMenuItem> map) {
        allCategories.forEach(item -> {
            if (!item.subCategoriesObservableSet().isEmpty() && getObjectFromCategory(item, map).menuItem.isSelected()) {
                item.subCategoriesObservableSet().forEach(subcategory -> {
                    getObjectFromCategory(subcategory, map).menuItem.setSelected(true);
                });
            }
        });
    }

    private void unselectCategoryIfSubcategoryIsUnselected(List<CategoryAndCheckMenuItem> map){
        if (source.isSelected()) {
            return;
        }

        allCategories.forEach(item -> {
            List<Category> subcategories = new LinkedList<>(item.subCategoriesObservableSet());
            if (subcategories != null) {
                for (Category subcategory : subcategories) {
                    if (subcategory.nameProperty().get().equalsIgnoreCase(source.getText())) {
                        getObjectFromCategory(item, map).menuItem.setSelected(false);
                        return;
                    }
                }
            }
        });
    }

    private boolean areAllSubcategoriesSelected(Category parent, List<CategoryAndCheckMenuItem> map) {
        boolean result = true;
        List<Category> subcategories = new LinkedList<>(parent.subCategoriesObservableSet());

        if (subcategories == null) {
            return false;
        }

        for (Category subcategory : subcategories) {
            if (!getObjectFromCategory(subcategory, map).menuItem.isSelected()) {
                result = false;
                break;
            }
        }

        return result;
    }

    private void selectCategoryIfAllSubcategoriesAreSelected(List<CategoryAndCheckMenuItem> map) {
        allCategories.forEach(item -> {
            if (!item.subCategoriesObservableSet().isEmpty() && areAllSubcategoriesSelected(item, map)) {
                getObjectFromCategory(item, map).menuItem.setSelected(true);
            }
        });
    }

    private CategoryAndCheckMenuItem getObjectFromCategory(Category category, List<CategoryAndCheckMenuItem> map) {
        for (CategoryAndCheckMenuItem categoryAndCheckMenuItem : map) {
            if (categoryAndCheckMenuItem.category.nameProperty().get().equalsIgnoreCase(category.nameProperty().get()))
                return categoryAndCheckMenuItem;
        }
        return null;
    }

    private List<CategoryAndCheckMenuItem> getCategoryAndCheckMenuItems(MenuButton button){
        List<CategoryAndCheckMenuItem> result = new ArrayList<>();

        button.getItems().forEach(item -> {
            if (item instanceof CheckMenuItem && !item.getText().equalsIgnoreCase(PropertiesUtil.ALL)) {
                CategoryAndCheckMenuItem object = new CategoryAndCheckMenuItem((CheckMenuItem)item,
                                                    getCategoryByName(allCategories, item.getText()));
                result.add(object);
            }
        });

        return result;
    }
    public Category getCategoryByName(List<Category> categories, String categoryName) {
        for (Category category : categories) {
            if (category.nameProperty().get().equalsIgnoreCase(categoryName)) return category;
            if (!category.subCategoriesObservableSet().isEmpty()) {
                Category result = getCategoryByName(new ArrayList<>(category.subCategoriesObservableSet()), categoryName);
                if (result!=null) {
                    return result;
                }
            }
        }
        return null;
    }
    public class CategoryAndCheckMenuItem {
        public CheckMenuItem menuItem;
        public Category category;

        public CategoryAndCheckMenuItem(CheckMenuItem item, Category category) {
            this.menuItem = item;
            this.category = category;
        }
    }

    private void selectAll(MenuButton button) {
        setCommonSelectValue(button, true);
    }

    private void unselectAll(MenuButton button) {
        setCommonSelectValue(button, false);
    }

    private void setCommonSelectValue(MenuButton button, boolean value) {
        button.getItems().forEach(item -> {
            if (item instanceof CheckMenuItem) {
                ((CheckMenuItem)item).setSelected(value);
            }
        });
    }
}
