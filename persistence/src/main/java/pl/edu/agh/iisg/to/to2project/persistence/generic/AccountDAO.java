package pl.edu.agh.iisg.to.to2project.persistence.generic;

import pl.edu.agh.iisg.to.to2project.domain.Account;
import pl.edu.agh.iisg.to.to2project.persistence.generic.generic.TransactionalGenericDAO;

/**
 * @author Wojciech Pachuta.
 */
public interface AccountDAO extends TransactionalGenericDAO<Account, Long> {
}
