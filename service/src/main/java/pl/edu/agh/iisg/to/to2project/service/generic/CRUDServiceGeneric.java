package pl.edu.agh.iisg.to.to2project.service.generic;

import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.iisg.to.to2project.domain.AbstractEntity;
import pl.edu.agh.iisg.to.to2project.persistence.generic.generic.TransactionalGenericCachingDAO;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author Wojciech Pachuta.
 */
public abstract class CRUDServiceGeneric<T extends AbstractEntity, ID extends Serializable> implements CRUDService<T, ID> {

    @Autowired
    protected TransactionalGenericCachingDAO<T, ID> dao;

//    protected void setDao(TransactionalGenericDAO<T, ID> dao){
//        this.dao = dao;
//    }

    protected TransactionalGenericCachingDAO<T, ID> getDao(){
        return dao;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Optional<T> getByPK(ID pk) {
        return Optional.ofNullable(dao.find(pk));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public ObservableList<T> getList() {
        return dao.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public void save(T entity) {
        dao.saveOrUpdate(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public void update(T entity) {
        dao.saveOrUpdate(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public void remove(T entity) {
        dao.remove(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public void removeByPK(ID pk) {
//        dao.removeById(pk);
    }
}
