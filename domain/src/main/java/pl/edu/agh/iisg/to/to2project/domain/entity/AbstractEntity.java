package pl.edu.agh.iisg.to.to2project.domain.entity;

import pl.edu.agh.iisg.to.to2project.domain.ExtractableEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Wojciech Pachuta.
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable, ExtractableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    public AbstractEntity() {
        this.id = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractEntity that = (AbstractEntity) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
