package pl.edu.agh.iisg.to.to2project.domain;

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Wojciech Pachuta.
 */
public class Category {
    private String name;
    private Set<Category> subCategories;
    private Optional<Category> parentCategory;

    Category() {
    }

    public Category(String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        this.name = name;
        this.subCategories = new HashSet<>();
        this.parentCategory = Optional.empty();
    }

    public Category(String name, Category parentCategory) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        Preconditions.checkNotNull(parentCategory);
        this.name = name;
        this.subCategories = new HashSet<>();
        this.parentCategory = Optional.of(parentCategory);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        this.name = name;
    }

    public Set<Category> getSubCategories() {
        return Collections.unmodifiableSet(subCategories);
    }

    public boolean addSubCategory(Category category){
        Preconditions.checkNotNull(category);
        return subCategories.add(category);
    }

    public boolean removeSubCategory(Category category){
        Preconditions.checkNotNull(category);
        return subCategories.remove(category);
    }

    public Optional<Category> getParentCategory(){
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory){
        Preconditions.checkNotNull(parentCategory);
        this.parentCategory = Optional.of(parentCategory);
    }

    public void removeParentCategory(){
        this.parentCategory = Optional.empty();
    }

}
