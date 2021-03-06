package pl.edu.agh.iisg.to.to2project.persistence.generic.generic;

import javafx.collections.ObservableList;
import pl.edu.agh.iisg.to.to2project.domain.entity.AbstractEntity;

import java.io.Serializable;

/**
 * @author Wojciech Pachuta.
 */
public interface TransactionalGenericCachingDAO<T extends AbstractEntity, ID extends Serializable> {

    ObservableList<T> findAll();

    void saveOrUpdate(T entity);

    void remove(T entity);

    T find(ID id);

    void refreshCache();

}
