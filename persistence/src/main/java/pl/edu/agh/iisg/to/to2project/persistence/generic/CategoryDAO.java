package pl.edu.agh.iisg.to.to2project.persistence.generic;

import pl.edu.agh.iisg.to.to2project.domain.Category;
import pl.edu.agh.iisg.to.to2project.persistence.generic.generic.TransactionalGenericCachingDAO;

/**
 * @author Wojciech Pachuta.
 */
public interface CategoryDAO extends TransactionalGenericCachingDAO<Category, Long> {
}
