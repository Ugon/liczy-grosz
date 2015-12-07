package pl.edu.agh.iisg.to.to2project.domain;

import com.google.common.base.Preconditions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Wojciech Pachuta.
 * @author Bartłomiej Grochal.
 */
@Entity
@Table
public class Category extends AbstractEntity {

    private final StringProperty name;

    private final ObservableSet<Category> subCategories;

    private final ObjectProperty<Category> parentCategory;

    private final ObservableSet<InternalTransaction> internalTransactions;

    private final ObservableSet<ExternalTransaction> externalTransactions;

    private final ObjectProperty<String> description;

    Category() {
        this.name = new SimpleStringProperty();
        this.subCategories = FXCollections.observableSet(new HashSet<>());
        this.parentCategory = new SimpleObjectProperty<>();
        this.internalTransactions = FXCollections.observableSet(new HashSet<>());
        this.externalTransactions = FXCollections.observableSet(new HashSet<>());
        this.description = new SimpleObjectProperty<>();
    }

    public Category(String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        this.name = new SimpleStringProperty(name);
        this.subCategories = FXCollections.observableSet(new HashSet<>());
        this.parentCategory = new SimpleObjectProperty<>();
        this.internalTransactions = FXCollections.observableSet(new HashSet<>());
        this.externalTransactions = FXCollections.observableSet(new HashSet<>());
        this.description = new SimpleObjectProperty<>();
    }



    @Column(unique = true, nullable = false)
    private String getName() {
        return name.get();
    }

    public void setName(String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }



    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentCategory")
    private Set<Category> getSubCategories(){
        return this.subCategories;
    }

    private void setSubCategories(Set<Category> subCategories){
        this.subCategories.addAll(subCategories);
    }

    public boolean addSubCategory(Category category){
        Preconditions.checkNotNull(category);
        Preconditions.checkArgument(category.parentCategoryProperty().get() == null);
        Preconditions.checkArgument(canAddSubcategoryWithoutCausingCycles(category));
        category.setParentCategory(this);
        return subCategories.add(category);
    }

    public boolean removeSubCategory(Category category){
        Preconditions.checkNotNull(category);
        Preconditions.checkArgument(category.parentCategoryProperty().get() == this);
        category.parentCategory.set(null);
        return subCategories.remove(category);
    }

    public ObservableSet<Category> subCategoriesObservableSet() {
        return FXCollections.unmodifiableObservableSet(subCategories);
    }

    public Set<Category> deepSubCategoriesSet() {
        Set<Category> set = FXCollections.observableSet(this);
        subCategoriesObservableSet().stream()
                .map(Category::deepSubCategoriesSet)
                .forEach(set::addAll);
        return set;
    }

    private boolean canAddSubcategoryWithoutCausingCycles(Category subCategory){
        return subCategory.parentCategoryProperty().get() == null && !subCategory.deepSubCategoriesSet().contains(this);
    }



    @OneToOne(optional = true, fetch = FetchType.EAGER)
    private Category getParentCategory(){
        return parentCategory.get();
    }

    private void setParentCategory(Category parentCategory){
        this.parentCategory.set(parentCategory);
    }

    public void removeParentCategoryIfPresent(){
        if(this.parentCategory.get() != null) {
            this.parentCategory.get().removeSubCategory(this);
        }
    }

    public ObjectProperty<Category> parentCategoryProperty(){
        return this.parentCategory;
    }



    @OneToMany(mappedBy = "categoryColumn")
    private Set<ExternalTransaction> getExternalTransactions() {
        return Collections.unmodifiableSet(externalTransactions);
    }

    private void setExternalTransactions(Set<ExternalTransaction> externalTransactions){
        this.externalTransactions.addAll(externalTransactions);
    }

    boolean addExternalTransaction(ExternalTransaction externalTransaction){
        Preconditions.checkNotNull(externalTransaction);
        return externalTransactions.add(externalTransaction);
    }

    boolean removeExternalTransaction(ExternalTransaction internalTransaction){
        Preconditions.checkNotNull(internalTransaction);
        return externalTransactions.remove(internalTransaction);
    }

    public ObservableSet<ExternalTransaction> externalTransactionsObservableSet() {
        return externalTransactions;
    }



    @OneToMany(mappedBy = "categoryColumn")
    private Set<InternalTransaction> getInternalTransactions() {
        return Collections.unmodifiableSet(internalTransactions);
    }

    private void setInternalTransactions(Set<InternalTransaction> internalTransactions){
        this.internalTransactions.addAll(internalTransactions);
    }

    boolean addInternalTransaction(InternalTransaction internalTransaction){
        Preconditions.checkNotNull(internalTransaction);
        return internalTransactions.add(internalTransaction);
    }

    boolean removeInternalTransaction(InternalTransaction internalTransaction){
        Preconditions.checkNotNull(internalTransaction);
        return internalTransactions.remove(internalTransaction);
    }

    public ObservableSet<InternalTransaction> internalTransactionObservableSet() {
        return internalTransactions;
    }



    @Column(name = "description")
    private String getDescriptionHibernate() {
        return description.get();
    }

    private void setDescriptionHibernate(String description) {
        this.description.set(description);
    }

    public void setDescription(String description) {
        Preconditions.checkNotNull(description);
        Preconditions.checkArgument(!description.isEmpty());
        this.description.set(description);
    }

    public void removeDescriptionIfPresent() {
        description.set(null);
    }

    public ObjectProperty<String> descriptionProperty() {
        return description;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Category category = (Category) o;

        return name.equals(category.name);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getName();
    }
}
