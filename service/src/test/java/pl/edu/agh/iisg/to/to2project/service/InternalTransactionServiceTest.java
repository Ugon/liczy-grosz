package pl.edu.agh.iisg.to.to2project.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;
import pl.edu.agh.iisg.to.to2project.persistence.generic.InternalTransactionDAO;
import pl.edu.agh.iisg.to.to2project.service.impl.InternalTransactionServiceImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Wojciech Pachuta.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class InternalTransactionServiceTest {

    @Configuration
    public static class TestConfiguration {

        @Bean
        public InternalTransactionService internalTransactionService() {
            return new InternalTransactionServiceImpl();
        }

        @Bean
        public InternalTransactionDAO internalTransactionDAO() {
            return mock(InternalTransactionDAO.class);
        }
    }

    @Autowired
    private InternalTransactionService instance;

    @Autowired
    private InternalTransactionDAO internalTransactionDAO;

    @Test
    public void shouldInternalTransactionServiceInvokeInternalTransactionDAO() throws Exception {
        //given
        InternalTransaction internalTransaction = mock(InternalTransaction.class);
        //when
        instance.save(internalTransaction);
        //then
        verify(internalTransactionDAO).saveOrUpdate(internalTransaction);
    }
}
