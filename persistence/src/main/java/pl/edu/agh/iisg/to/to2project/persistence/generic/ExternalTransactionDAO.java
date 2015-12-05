package pl.edu.agh.iisg.to.to2project.persistence.generic;

import pl.edu.agh.iisg.to.to2project.domain.ExternalTransaction;
import pl.edu.agh.iisg.to.to2project.persistence.generic.generic.TransactionalGenericCachingDAO;

/**
 * @author Wojciech Pachuta.
 */
public interface ExternalTransactionDAO extends TransactionalGenericCachingDAO<ExternalTransaction, Long> {
}
