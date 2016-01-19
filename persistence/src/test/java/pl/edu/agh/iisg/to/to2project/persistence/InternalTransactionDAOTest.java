package pl.edu.agh.iisg.to.to2project.persistence;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;
import pl.edu.agh.iisg.to.to2project.persistence.generic.InternalTransactionDAO;
import pl.edu.agh.iisg.to.to2project.persistence.generic.impl.InternalTransactionDAOImpl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Wojciech Pachuta.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class InternalTransactionDAOTest {

    @Configuration
    public static class TestConfiguration {
        @Bean
        public InternalTransactionDAO internalTransactionDAO(){
            return new InternalTransactionDAOImpl();
        }

        @Bean
        public SessionFactory sessionFactory() {
            SessionFactory sessionFactoryMock = mock(SessionFactory.class);
            when(sessionFactoryMock.getCurrentSession()).thenReturn(mock(Session.class));
            return sessionFactoryMock;
        }
    }

    @Autowired
    private InternalTransactionDAO instance;

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(instance, "cache", FXCollections.observableArrayList());

    }

    @Test
    public void shouldSaveOrUpdateMethodAddInstanceToCache() throws Exception {
        //given
        InternalTransaction internalTransaction = mock(InternalTransaction.class);
        //when
        instance.saveOrUpdate(internalTransaction);
        //then
        assertThat(instance.findAll()).contains(internalTransaction);
    }

    @Test
    public void shouldSaveOrUpdateMethodGenerateCacheEvent() throws Exception {
        //given
        ObservableList<InternalTransaction> list = instance.findAll();
        InternalTransaction internalTransaction = mock(InternalTransaction.class);
        ListChangeListener<InternalTransaction> listChangeListener = mock(ListChangeListener.class);
        list.addListener(listChangeListener);
        //when
        instance.saveOrUpdate(internalTransaction);
        //then
        verify(listChangeListener).onChanged(any());
    }
}