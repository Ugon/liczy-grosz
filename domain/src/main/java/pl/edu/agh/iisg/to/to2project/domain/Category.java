package pl.edu.agh.iisg.to.to2project.domain;

import com.google.common.base.Preconditions;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicObservableValue;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Wojciech Pachuta.
 * @author Bartłomiej Grochal.
 */
@Entity
@Table
public class Category extends AbstractEntity {

    @Column(name = "name", unique = true, nullable = false)
    private String namePOJO;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentCategoryEntity", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Category> subCategoriesPOJO;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private Category parentCategoryEntity;

    @OneToMany(mappedBy = "categoryEntity")
    private Set<InternalTransaction> internalTransactionsPOJO;

    @OneToMany(mappedBy = "categoryEntity")
    private Set<ExternalTransaction> externalTransactionsPOJO;

    @Column(name = "description")
    private String descriptionPOJO;

    @Transient
    private final StringProperty name;

    @Transient
    private final ObservableSet<Category> subCategories;

    @Transient
    private final ObjectProperty<Category> parentCategory;

    @Transient
    private final MonadicObservableValue<Category> parentCategoryMonadic;

    @Transient
    private final ObservableSet<InternalTransaction> internalTransactions;

    @Transient
    private final ObservableSet<ExternalTransaction> externalTransactions;

    @Transient
    private final StringProperty description;

    @Transient
    private final MonadicObservableValue<String> descriptionMonadic;

    Category() {
        this.name = new SimpleStringProperty();
        this.subCategories = FXCollections.observableSet(new HashSet<>());
        this.parentCategory = new SimpleObjectProperty<>();
        this.parentCategoryMonadic = EasyBind.monadic(parentCategory);
        this.internalTransactions = FXCollections.observableSet();
        this.externalTransactions = FXCollections.observableSet();
        this.description = new SimpleStringProperty();
        this.descriptionMonadic = EasyBind.monadic(description);
    }

    public Category(String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        this.name = new SimpleStringProperty(name);
        this.subCategories = FXCollections.observableSet(new HashSet<>());
        this.parentCategory = new SimpleObjectProperty<>();
        this.parentCategoryMonadic = EasyBind.monadic(parentCategory);
        this.internalTransactions = FXCollections.observableSet();
        this.externalTransactions = FXCollections.observableSet();
        this.description = new SimpleStringProperty();
        this.descriptionMonadic = EasyBind.monadic(description);
    }

    @PostLoad
    private void initProperties(){
        subCategories.clear();
        subCategories.addAll(subCategoriesPOJO);
        externalTransactions.clear();
        externalTransactions.addAll(externalTransactionsPOJO);
        internalTransactions.clear();
        internalTransactions.addAll(internalTransactionsPOJO);
        name.set(namePOJO);
        parentCategory.set(parentCategoryEntity);
        description.set(descriptionPOJO);
    }

    @PrePersist
    @PreUpdate
    private void updatePOJOs(){
        namePOJO = name.get();
        parentCategoryEntity = parentCategory.get();
        descriptionPOJO = description.get();
        if(externalTransactionsPOJO == null) {
            externalTransactionsPOJO = new HashSet<>(externalTransactions);
        }
        else{
            externalTransactionsPOJO.clear();
            externalTransactionsPOJO.addAll(externalTransactions);
        }

        if(internalTransactionsPOJO == null) {
            internalTransactionsPOJO = new HashSet<>(internalTransactions);
        }
        else{
            internalTransactionsPOJO.clear();
            internalTransactionsPOJO.addAll(internalTransactions);
        }

        if(subCategoriesPOJO == null){
            subCategoriesPOJO = new HashSet<>(subCategories);
        }
        else {
            subCategoriesPOJO.clear();
            subCategoriesPOJO.addAll(subCategories);
        }
    }


    public void setName(String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        this.name.set(name);
    }

    public ReadOnlyStringProperty nameProperty() {
        return name;
    }


    public void addSubCategory(Category category) {
        Preconditions.checkNotNull(category);
        Preconditions.checkArgument(category.parentCategory.get() == null);
        Preconditions.checkArgument(canAddSubcategoryWithoutCausingCycles(category));
        category.parentCategory.set(this);
        subCategories.add(category);
    }

    public void removeSubCategory(Category category) {
        Preconditions.checkNotNull(category);
        Preconditions.checkArgument(category.parentCategory.get() == this);
        category.parentCategory.set(null);
        subCategories.remove(category);
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

    private boolean canAddSubcategoryWithoutCausingCycles(Category subCategory) {
        return subCategory.parentCategoryMonadicProperty().isEmpty() && !subCategory.deepSubCategoriesSet().contains(this);
    }


    public void removeParentCategoryIfPresent() {
        if (this.parentCategory.get() != null) {
            this.parentCategory.get().removeSubCategory(this);
        }
    }

    public MonadicObservableValue<Category> parentCategoryMonadicProperty() {
        return this.parentCategoryMonadic;
    }


    void addExternalTransaction(ExternalTransaction externalTransaction) {
        Preconditions.checkNotNull(externalTransaction);
        externalTransactions.add(externalTransaction);
    }

    void removeExternalTransaction(ExternalTransaction internalTransaction) {
        Preconditions.checkNotNull(internalTransaction);
        externalTransactions.remove(internalTransaction);
    }

    public ObservableSet<ExternalTransaction> externalTransactionsObservableSet() {
        return FXCollections.unmodifiableObservableSet(externalTransactions);
    }


    void addInternalTransaction(InternalTransaction internalTransaction) {
        Preconditions.checkNotNull(internalTransaction);
        internalTransactions.add(internalTransaction);
    }

    void removeInternalTransaction(InternalTransaction internalTransaction) {
        Preconditions.checkNotNull(internalTransaction);
        internalTransactions.remove(internalTransaction);
    }

    public ObservableSet<InternalTransaction> internalTransactionObservableSet() {
        return FXCollections.unmodifiableObservableSet(internalTransactions);
    }


    public void setDescription(String description) {
        Preconditions.checkNotNull(description);
        Preconditions.checkArgument(!description.isEmpty());
        this.description.set(description);
    }

    public void removeDescriptionIfPresent() {
        description.set(null);
    }

    public MonadicObservableValue<String> descriptionMonadicProperty() {
        return descriptionMonadic;
    }

    @Override
    public String toString() {
        return nameProperty().get();
    }
}
