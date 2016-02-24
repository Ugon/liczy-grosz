package pl.edu.agh.iisg.to.to2project.service;

import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.service.generic.CRUDService;

/**
 * @author Wojciech Pachuta.
 */
public interface AccountService extends CRUDService<Account, Long>{

    boolean canDelete(Account account);

}
