package pl.edu.agh.iisg.to.to2project.persistence.generic.impl;

import org.springframework.stereotype.Repository;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;
import pl.edu.agh.iisg.to.to2project.persistence.generic.ExternalTransactionDAO;
import pl.edu.agh.iisg.to.to2project.persistence.generic.generic.TransactionalGenericCachingDAOImpl;

/**
 * @author Wojciech Pachuta.
 */
@Repository
public class ExternalTransactionDAOImpl extends TransactionalGenericCachingDAOImpl<ExternalTransaction, Long> implements ExternalTransactionDAO {

    @Override
    protected Class<ExternalTransaction> getPersistentType() {
        return ExternalTransaction.class;
    }

}
