package pl.edu.agh.iisg.to.to2project.service.generic;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author Wojciech Pachuta.
 */
public interface CRUDService<T, ID extends Serializable> {
    Optional<T> getByPK(ID pk);

    List<T> getList();

    void save(T entity);

    void update(T entity);

    void remove(T entity);

    void removeByPK(ID pk);
}
