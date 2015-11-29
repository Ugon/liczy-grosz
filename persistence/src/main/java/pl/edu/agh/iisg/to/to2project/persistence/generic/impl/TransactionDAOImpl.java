package pl.edu.agh.iisg.to.to2project.persistence.generic.impl;

import org.springframework.stereotype.Repository;
import pl.edu.agh.iisg.to.to2project.domain.Transaction;
import pl.edu.agh.iisg.to.to2project.persistence.generic.TransactionDAO;
import pl.edu.agh.iisg.to.to2project.persistence.generic.generic.TransactionalGenericDAOImpl;

/**
 * @author Wojciech Pachuta.
 */
@Repository
public class TransactionDAOImpl extends TransactionalGenericDAOImpl<Transaction, Long> implements TransactionDAO {
}