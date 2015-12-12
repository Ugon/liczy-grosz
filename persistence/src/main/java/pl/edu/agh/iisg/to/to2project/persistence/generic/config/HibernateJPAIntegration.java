package pl.edu.agh.iisg.to.to2project.persistence.generic.config;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.jpa.event.spi.JpaIntegrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Wojciech Pachuta.
 */
@Component
public class HibernateJPAIntegration {

    @Autowired
    private LocalSessionFactoryBean sessionFactoryBean;

    @PostConstruct
    public void integrate(){
        SessionFactoryImplementor sessionFactory = (SessionFactoryImplementor) sessionFactoryBean.getObject();
        SessionFactoryServiceRegistry serviceRegistry = (SessionFactoryServiceRegistry) sessionFactory.getServiceRegistry();
        Configuration configuration = sessionFactoryBean.getConfiguration();

        new JpaIntegrator().integrate(configuration, sessionFactory, serviceRegistry);
    }
}
