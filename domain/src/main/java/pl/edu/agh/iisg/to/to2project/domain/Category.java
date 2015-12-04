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
import java.util.Optional;
import java.util.Set;

/**
 * @author Wojciech Pachuta.
 */
@Entity
@Table
public class Category extends AbstractEntity {

    private final StringProperty name;

    private final ObservableSet<Category> subCategories;

    private final ObjectProperty<Optional<Category>> parentCategory;

    private final ObservableSet<InternalTransaction> internalTransactions;

    private final ObservableSet<ExternalTransaction> externalTransactions;

    Category() {
        this.name = new SimpleStringProperty();
        this.subCategories = FXCollections.observableSet(new HashSet<>());
        this.parentCategory = new SimpleObjectProperty<>(Optional.empty());
        this.internalTransactions = FXCollections.observableSet(new HashSet<>());
        this.externalTransactions = FXCollections.observableSet(new HashSet<>());
    }

    public Category(String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        this.name = new SimpleStringProperty(name);
        this.subCategories = FXCollections.observableSet(new HashSet<>());
        this.parentCategory = new SimpleObjectProperty<>(Optional.empty());
        this.internalTransactions = FXCollections.observableSet(new HashSet<>());
        this.externalTransactions = FXCollections.observableSet(new HashSet<>());
    }

    public Category(String name, Category parentCategory) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        Preconditions.checkNotNull(parentCategory);
        this.name = new SimpleStringProperty(name);
        this.subCategories = FXCollections.observableSet(new HashSet<>());
        this.parentCategory = new SimpleObjectProperty<>(Optional.of(parentCategory));
        this.internalTransactions = FXCollections.observableSet(new HashSet<>());
        this.externalTransactions = FXCollections.observableSet(new HashSet<>());
    }



    @Column(unique = true, nullable = false)
    public String getName() {
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
        category.setParentCategory(this);
        return subCategories.add(category);
    }

    public boolean removeSubCategory(Category category){
        Preconditions.checkNotNull(category);
        Preconditions.checkArgument(this.equals(category.getParentCategory()));
        category.removeParentCategory();
        return subCategories.remove(category);
    }

    public Set<Category> subCategoriesObservableSet() {
        return Collections.unmodifiableSet(subCategories);
    }




    @OneToOne(optional = true, fetch = FetchType.EAGER)
    private Category getParentCategory(){
        return parentCategory.get().orElse(null);
    }

    private void setParentCategory(Category parentCategory){
        this.parentCategory.set(Optional.ofNullable(parentCategory));
    }

    private void removeParentCategory(){
        this.parentCategory.set(Optional.empty());
    }

    public ObjectProperty<Optional<Category>> parentCategoryProperty(){
        return this.parentCategory;
    }



    @OneToMany(mappedBy = "category")
    private Set<ExternalTransaction> getExternalTransactions() {
        return Collections.unmodifiableSet(externalTransactions);
    }

    private void setExternalTransactions(Set<ExternalTransaction> externalTransactions){
        this.externalTransactions.addAll(externalTransactions);
    }

    public boolean addExternalTransaction(ExternalTransaction externalTransaction){
        Preconditions.checkNotNull(externalTransaction);
        return externalTransactions.add(externalTransaction);
    }

    public boolean removeExternalTransaction(ExternalTransaction internalTransaction){
        Preconditions.checkNotNull(internalTransaction);
        return externalTransactions.remove(internalTransaction);
    }

    public ObservableSet<ExternalTransaction> externalTransactionsObservableSet() {
        return externalTransactions;
    }



    @OneToMany(mappedBy = "category")
    private Set<InternalTransaction> getInternalTransactions() {
        return Collections.unmodifiableSet(internalTransactions);
    }

    private void setInternalTransactions(Set<InternalTransaction> internalTransactions){
        this.internalTransactions.addAll(internalTransactions);
    }

    public boolean addInternalTransaction(InternalTransaction internalTransaction){
        Preconditions.checkNotNull(internalTransaction);
        return internalTransactions.add(internalTransaction);
    }

    public boolean removeInternalTransaction(InternalTransaction internalTransaction){
        Preconditions.checkNotNull(internalTransaction);
        return internalTransactions.remove(internalTransaction);
    }

    public ObservableSet<InternalTransaction> internalTransactionObservableSet() {
        return internalTransactions;
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
}
