package pl.edu.agh.iisg.to.to2project.persistence.generic.generic;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;

import java.io.Serializable;

/**
 * @author Wojciech Pachuta.
 */
public interface TransactionalGeneticDao<T, ID extends Serializable> extends GenericDAO<T, ID>{
}
