package pl.edu.agh.iisg.to.to2project.persistence.generic.generic;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.ExampleOptions;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.SearchResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author Wojciech Pachuta.
 */
public class TransactionalGenericDAOImpl<T, ID extends Serializable> extends GenericDAOImpl<T, ID>
        implements TransactionalGeneticDao<T, ID> {

    @Override
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean save(T entity) {
        return super.save(entity);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean remove(T entity) {
        return super.remove(entity);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public T find(Serializable id) {
        return super.find(id);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public int count(ISearch search) {
        return super.count(search);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public T[] find(Serializable... ids) {
        return super.find(ids);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<T> findAll() {
        return super.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void flush() {
        super.flush();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public T getReference(Serializable id) {
        return super.getReference(id);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public T[] getReferences(Serializable... ids) {
        return super.getReferences(ids);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean isAttached(T entity) {
        return super.isAttached(entity);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void refresh(T... entities) {
        super.refresh(entities);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void remove(T... entities) {
        super.remove(entities);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void removeByIds(Serializable... ids) {
        super.removeByIds(ids);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean[] save(T... entities) {
        return super.save(entities);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public <RT> List<RT> search(ISearch search) {
        return super.search(search);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public <RT> SearchResult<RT> searchAndCount(ISearch search) {
        return super.searchAndCount(search);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public <RT> RT searchUnique(ISearch search) {
        return super.searchUnique(search);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Filter getFilterFromExample(T example) {
        return super.getFilterFromExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Filter getFilterFromExample(T example, ExampleOptions options) {
        return super.getFilterFromExample(example, options);
    }

}
