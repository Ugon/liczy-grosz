package pl.edu.agh.iisg.to.to2project.service.impl;

import org.springframework.stereotype.Service;
import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;
import pl.edu.agh.iisg.to.to2project.service.InternalTransactionService;
import pl.edu.agh.iisg.to.to2project.service.generic.CRUDServiceGeneric;

/**
 * @author Wojciech Pachuta.
 */
@Service
public class InternalTransactionServiceImpl extends CRUDServiceGeneric<InternalTransaction, Long> implements InternalTransactionService {

    @Override
    public boolean canDelete(InternalTransaction transaction) {
        return true;
    }

}
