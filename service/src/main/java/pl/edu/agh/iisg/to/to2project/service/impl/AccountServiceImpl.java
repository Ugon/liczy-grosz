package pl.edu.agh.iisg.to.to2project.service.impl;

import org.springframework.stereotype.Service;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.service.AccountService;
import pl.edu.agh.iisg.to.to2project.service.generic.CRUDServiceGeneric;

/**
 * @author Wojciech Pachuta.
 */
@Service
public class AccountServiceImpl extends CRUDServiceGeneric<Account, Long> implements AccountService {

}
