package pl.edu.agh.iisg.to.to2project.persistence.generic.impl;

import org.springframework.stereotype.Repository;
import pl.edu.agh.iisg.to.to2project.domain.Category;
import pl.edu.agh.iisg.to.to2project.persistence.generic.CategoryDAO;
import pl.edu.agh.iisg.to.to2project.persistence.generic.generic.TransactionalGenericCachingDAOImpl;

/**
 * @author Wojciech Pachuta.
 */
@Repository
public class CategoryDAOImpl extends TransactionalGenericCachingDAOImpl<Category, Long> implements CategoryDAO {

    @Override
    protected Class<Category> getPersistentType() {
        return Category.class;
    }

}
