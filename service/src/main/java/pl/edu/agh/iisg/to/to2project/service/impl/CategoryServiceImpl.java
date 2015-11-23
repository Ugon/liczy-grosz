package pl.edu.agh.iisg.to.to2project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.agh.iisg.to.to2project.domain.Category;
import pl.edu.agh.iisg.to.to2project.persistence.generic.CategoryDAO;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;
import pl.edu.agh.iisg.to.to2project.service.generic.CRUDServiceGeneric;

/**
 * @author Wojciech Pachuta.
 */
public class CategoryServiceImpl extends CRUDServiceGeneric<Category, Long> implements CategoryService {

    @Autowired
    protected void setDao(CategoryDAO dao) {
        this.dao = dao;
    }

}
