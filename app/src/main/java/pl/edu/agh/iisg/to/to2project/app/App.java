package pl.edu.agh.iisg.to.to2project.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import pl.edu.agh.iisg.to.to2project.domain.Account;
import pl.edu.agh.iisg.to.to2project.service.AccountService;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;
import pl.edu.agh.iisg.to.to2project.service.TransactionService;

import java.math.BigDecimal;

/**
 * @author Wojciech Pachuta.
 */
@SpringBootApplication
@ImportResource({"classpath:persistenceContext.xml"})
@ComponentScan({"pl.edu.agh.iisg.to.to2project"})
public class App {

    private static TransactionService transactionService;
    private static CategoryService categoryService;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(App.class, args);

        System.out.println("dupa\n\n");

        AccountService accountService = (AccountService) ctx.getBean("accountServiceImpl");
        CategoryService categoryService = (CategoryService) ctx.getBean("categoryServiceImpl");
        TransactionService transactionService = (TransactionService) ctx.getBean("transactionServiceImpl");

        accountService.save(new Account("Dupa", new BigDecimal(10)));

    }

}
