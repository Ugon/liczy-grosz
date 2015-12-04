package pl.edu.agh.iisg.to.to2project.service.impl;

import org.springframework.stereotype.Service;
import pl.edu.agh.iisg.to.to2project.domain.Transaction;
import pl.edu.agh.iisg.to.to2project.service.TransactionService;
import pl.edu.agh.iisg.to.to2project.service.generic.CRUDServiceGeneric;

/**
 * @author Wojciech Pachuta.
 */
@Service
public class TransactionServiceImpl extends CRUDServiceGeneric<Transaction, Long> implements TransactionService {

//    @Autowired
//    protected void setDao(TransactionDAO dao) {
//        this.dao = dao;
//    }

}
