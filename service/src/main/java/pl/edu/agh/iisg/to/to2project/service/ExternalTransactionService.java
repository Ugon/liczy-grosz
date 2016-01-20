package pl.edu.agh.iisg.to.to2project.service;

import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;
import pl.edu.agh.iisg.to.to2project.service.generic.CRUDService;

/**
 * @author Wojciech Pachuta.
 */
public interface ExternalTransactionService extends CRUDService<ExternalTransaction, Long> {

    boolean canDelete(ExternalTransaction transaction);

}