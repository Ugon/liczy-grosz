package pl.edu.agh.iisg.to.to2project.persistence.generic.generic;

import com.google.common.base.Preconditions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Criteria;
import org.hibernate.Session;
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
public abstract class TransactionalGenericCachingDAOImpl<T extends AbstractEntity, ID extends Serializable> implements TransactionalGenericCachingDAO<T, ID> {

    private SessionFactory sessionFactory;

    private ObservableList<T> cache;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected abstract Class<T> getPersistentType();

    private void populateCache() {
        Session session = sessionFactory.getCurrentSession();
        List<T> list = session.createCriteria(getPersistentType()).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        cache = FXCollections.observableArrayList(list);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public ObservableList<T> findAll() {
        if (cache == null) {
            populateCache();
        }
        return FXCollections.unmodifiableObservableList(cache);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveOrUpdate(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(entity);
        session.flush();
        if (cache != null) {
            refresh(entity);
            session.flush();
            List<T> cachedEntity = cache.filtered(elem -> elem.getId().equals(entity.getId()));
            cache.removeAll(cachedEntity);
            cache.add(entity);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void remove(T entity) {
        if (entity != null) {
            Session session = sessionFactory.getCurrentSession();
            session.delete(entity);
            session.flush();
            if (cache != null) {
                List<T> cachedEntity = cache.filtered(elem -> elem.getId().equals(entity.getId()));
                cache.removeAll(cachedEntity);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public T find(ID id) {
        Preconditions.checkNotNull(id);
        Session session = sessionFactory.getCurrentSession();
        T object = (T) session.get(getPersistentType(), id);
        return object;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void refresh(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.refresh(entity);
        session.flush();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void refreshCache() {
        if (cache == null) {
            populateCache();
        } else {
            Session session = sessionFactory.getCurrentSession();
            cache.forEach(session::refresh);
            session.flush();
        }
    }
}