package pl.edu.agh.iisg.to.to2project.domain;

import com.google.common.base.Preconditions;

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
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentCategory")
    private Set<Category> subCategories;

    @OneToOne(optional = true)
    private Category parentCategory;

    Category() {
    }

    public Category(String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        this.name = name;
        this.subCategories = new HashSet<>();
        this.parentCategory = null;
    }

    public Category(String name, Category parentCategory) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        Preconditions.checkNotNull(parentCategory);
        this.name = name;
        this.subCategories = new HashSet<>();
        this.parentCategory = parentCategory;
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
        return Optional.ofNullable(parentCategory);
    }

    public void setParentCategory(Category parentCategory){
        Preconditions.checkNotNull(parentCategory);
        this.parentCategory = parentCategory;
    }

    public void removeParentCategory(){
        this.parentCategory = null;
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
