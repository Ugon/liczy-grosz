package pl.edu.agh.iisg.to.to2project.persistence.generic.impl;

import org.springframework.stereotype.Repository;
import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;
import pl.edu.agh.iisg.to.to2project.persistence.generic.InternalTransactionDAO;
import pl.edu.agh.iisg.to.to2project.persistence.generic.generic.TransactionalGenericCachingDAOImpl;

/**
 * @author Wojciech Pachuta.
 */
@Repository
public class InternalTransactionDAOImpl extends TransactionalGenericCachingDAOImpl<InternalTransaction, Long> implements InternalTransactionDAO {

    @Override
    protected Class<InternalTransaction> getPersistentType() {
        return InternalTransaction.class;
    }

}
