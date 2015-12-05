package pl.edu.agh.iisg.to.to2project.persistence.generic.generic;

import com.googlecode.genericdao.dao.DAOUtil;
import com.googlecode.genericdao.dao.hibernate.HibernateBaseDAO;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.iisg.to.to2project.domain.AbstractEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author Wojciech Pachuta.
 */
public class TransactionalGenericCachingDAOImpl<T extends AbstractEntity, ID extends Serializable> extends HibernateBaseDAO implements TransactionalGenericCachingDAO<T, ID> {

    @SuppressWarnings("unchecked")
    protected Class<T> persistentClass = (Class<T>) DAOUtil.getTypeArguments(TransactionalGenericCachingDAOImpl.class, this.getClass()).get(0);

    private ObservableList<T> cache;

    @Autowired
    @Override
    public void setSessionFactory(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

    @SuppressWarnings("unchecked")
    private void populateCache(){
        cache = FXCollections.observableArrayList(_all(persistentClass));
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public ObservableList<T> findAll() {
        if(cache == null){
            populateCache();
        }
        return new ReadOnlyListWrapper<>(cache);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveOrUpdate(T entity) {
        _saveOrUpdate(entity);
        if(cache != null){
            if(entity.getId() == null) {
                _flush();
                _refresh(entity);
            }
            List<T> cachedEntity = cache.filtered(elem -> elem.getId().equals(entity.getId()));
            if (!cachedEntity.isEmpty()) {
                cache.removeAll(cachedEntity);
            }
            cache.add(entity);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void remove(T entity) {
        if(entity != null) {
            _deleteEntity(entity);
            if(cache != null) {
                cache.remove(entity);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public T find(ID id) {
        return _get(persistentClass, id);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void flush() {
        _flush();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void refresh(T entity) {
        _refresh(entity);
    }
}
