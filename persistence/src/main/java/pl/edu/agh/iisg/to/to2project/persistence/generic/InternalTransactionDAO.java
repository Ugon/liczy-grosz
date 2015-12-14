package pl.edu.agh.iisg.to.to2project.persistence.generic;

import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;
import pl.edu.agh.iisg.to.to2project.persistence.generic.generic.TransactionalGenericCachingDAO;

/**
 * @author Wojciech Pachuta.
 */
public interface InternalTransactionDAO extends TransactionalGenericCachingDAO<InternalTransaction, Long> {
}
