package pl.edu.agh.iisg.to.to2project.service.generic;

import javafx.collections.ObservableList;
import pl.edu.agh.iisg.to.to2project.domain.AbstractEntity;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author Wojciech Pachuta.
 */
public interface CRUDService<T extends AbstractEntity, ID extends Serializable> {
    Optional<T> getByPK(ID pk);

    ObservableList<T> getList();

    void save(T entity);

    void update(T entity);

    void remove(T entity);

    void removeByPK(ID pk);
}
