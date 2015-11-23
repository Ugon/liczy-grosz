package pl.edu.agh.iisg.to.to2project.persistence.generic;

import pl.edu.agh.iisg.to.to2project.domain.Transaction;
import pl.edu.agh.iisg.to.to2project.persistence.generic.generic.TransactionalGeneticDao;

/**
 * @author Wojciech Pachuta.
 */
public interface TransactionDAO extends TransactionalGeneticDao<Transaction, Long> {
}
