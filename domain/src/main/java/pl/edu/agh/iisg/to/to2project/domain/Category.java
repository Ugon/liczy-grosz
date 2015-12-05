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

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

/**
 * @author Wojciech Pachuta.
 * @author Bartłomiej Grochal.
 */
@Entity
@Table
public class Category extends AbstractEntity {

    private final StringProperty name;

    private final ObservableSet<Category> subCategories;

    private final ObjectProperty<Optional<Category>> parentCategory;

    private final ObservableSet<InternalTransaction> internalTransactions;

    private final ObservableSet<ExternalTransaction> externalTransactions;

    private final ObjectProperty<Optional<String>> description;

    Category() {
        this.name = new SimpleStringProperty();
        this.subCategories = FXCollections.observableSet(new HashSet<>());
        this.parentCategory = new SimpleObjectProperty<>(Optional.empty());
        this.internalTransactions = FXCollections.observableSet(new HashSet<>());
        this.externalTransactions = FXCollections.observableSet(new HashSet<>());
        this.description = new SimpleObjectProperty<>();
    }

    public Category(String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        this.name = new SimpleStringProperty(name);
        this.subCategories = FXCollections.observableSet(new HashSet<>());
        this.parentCategory = new SimpleObjectProperty<>(Optional.empty());
        this.internalTransactions = FXCollections.observableSet(new HashSet<>());
        this.externalTransactions = FXCollections.observableSet(new HashSet<>());
        this.description = new SimpleObjectProperty<>();
    }

    public Category(String name, String description) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        Preconditions.checkNotNull(description);
        Preconditions.checkArgument(!description.isEmpty());
        this.name = new SimpleStringProperty(name);
        this.subCategories = FXCollections.observableSet(new HashSet<>());
        this.parentCategory = new SimpleObjectProperty<>(Optional.empty());
        this.internalTransactions = FXCollections.observableSet(new HashSet<>());
        this.externalTransactions = FXCollections.observableSet(new HashSet<>());
        this.description = new SimpleObjectProperty<>(of(description));
    }

    public Category(String name, Category parentCategory) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        Preconditions.checkNotNull(parentCategory);
        this.name = new SimpleStringProperty(name);
        this.subCategories = FXCollections.observableSet(new HashSet<>());
        this.parentCategory = new SimpleObjectProperty<>(of(parentCategory));
        this.internalTransactions = FXCollections.observableSet(new HashSet<>());
        this.externalTransactions = FXCollections.observableSet(new HashSet<>());
        this.description = new SimpleObjectProperty<>();
    }

    public Category(String name, Category parentCategory, String description) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        Preconditions.checkNotNull(parentCategory);
        Preconditions.checkNotNull(description);
        Preconditions.checkArgument(!description.isEmpty());
        this.name = new SimpleStringProperty(name);
        this.subCategories = FXCollections.observableSet(new HashSet<>());
        this.parentCategory = new SimpleObjectProperty<>(of(parentCategory));
        this.internalTransactions = FXCollections.observableSet(new HashSet<>());
        this.externalTransactions = FXCollections.observableSet(new HashSet<>());
        this.description = new SimpleObjectProperty<>(of(description));
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
        this.parentCategory.set(ofNullable(parentCategory));
    }

    private void removeParentCategory(){
        this.parentCategory.set(Optional.empty());
    }

    public ObjectProperty<Optional<Category>> parentCategoryProperty(){
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



    @Column(name = "description", nullable = true)
    private String getDescription() {
        return description.get().orElse("");
    }

    @Column(name = "description", nullable = true)
    private void setDescriptionHibernate(String description) {
        this.description.set(ofNullable(description));
    }

    public void setDescription(String description) {
        Preconditions.checkNotNull(description);
        Preconditions.checkArgument(!description.isEmpty());
        this.description.set(of(description));
    }

    public ObjectProperty<Optional<String>> descriptionProperty() {
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
}
