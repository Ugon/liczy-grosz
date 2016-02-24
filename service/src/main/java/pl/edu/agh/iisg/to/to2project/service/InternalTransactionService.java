package pl.edu.agh.iisg.to.to2project.service;

import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;
import pl.edu.agh.iisg.to.to2project.service.generic.CRUDService;

/**
 * @author Wojciech Pachuta.
 */
public interface InternalTransactionService extends CRUDService<InternalTransaction, Long>{

    boolean canDelete(InternalTransaction transaction);

}
