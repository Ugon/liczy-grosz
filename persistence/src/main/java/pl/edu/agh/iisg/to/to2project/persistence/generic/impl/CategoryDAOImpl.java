package pl.edu.agh.iisg.to.to2project.persistence.generic.impl;

import org.springframework.stereotype.Repository;
import pl.edu.agh.iisg.to.to2project.domain.Category;
import pl.edu.agh.iisg.to.to2project.persistence.generic.CategoryDAO;
import pl.edu.agh.iisg.to.to2project.persistence.generic.generic.TransactionalGenericDAOImpl;

/**
 * @author Wojciech Pachuta.
 */
@Repository
public class CategoryDAOImpl extends TransactionalGenericDAOImpl<Category, Long> implements CategoryDAO {
}
