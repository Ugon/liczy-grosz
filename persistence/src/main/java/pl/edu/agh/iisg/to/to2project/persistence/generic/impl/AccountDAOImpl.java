package pl.edu.agh.iisg.to.to2project.persistence.generic.impl;

import org.springframework.stereotype.Repository;
import pl.edu.agh.iisg.to.to2project.domain.Account;
import pl.edu.agh.iisg.to.to2project.persistence.generic.AccountDAO;
import pl.edu.agh.iisg.to.to2project.persistence.generic.generic.TransactionalGenericDAOImpl;

/**
 * @author Wojciech Pachuta.
 */
@Repository
public class AccountDAOImpl extends TransactionalGenericDAOImpl<Account, Long> implements AccountDAO{
}
