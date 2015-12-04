package pl.edu.agh.iisg.to.to2project.domain;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Wojciech Pachuta.
 */
@MappedSuperclass
@Access(AccessType.PROPERTY)
public abstract class AbstractEntity implements Serializable {

    protected final LongProperty id;

    public AbstractEntity() {
        this.id = new SimpleLongProperty(this, "id");
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractEntity that = (AbstractEntity) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
