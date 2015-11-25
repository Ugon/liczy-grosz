package pl.edu.agh.iisg.to.to2project.core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import pl.edu.agh.iisg.to.to2project.domain.Account;
import pl.edu.agh.iisg.to.to2project.service.AccountService;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;
import pl.edu.agh.iisg.to.to2project.service.TransactionService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Logger;

import static java.util.logging.Level.ALL;

/**
 * @author Bartłomiej Grochal, Wojciech Pachuta
 */
@SpringBootApplication
@ImportResource({"classpath:persistenceContext.xml"})
@ComponentScan({"pl.edu.agh.iisg.to.to2project"})
public class ApplicationMain extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        initStage();
        initRootContent();
    }

    private void initStage() {
        primaryStage.setTitle("Home Budget Manager.");
    }

    private void initRootContent() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationMain.class.getResource("view/RootContent.fxml"));

        try {
            primaryStage.setScene(new Scene((BorderPane) loader.load()));
        } catch (IOException exc) {
            Logger.getLogger("GUI").log(ALL, "Cannot instantiate Application Root Content Layout.");
            exc.printStackTrace();
        }

        primaryStage.show();
    }

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(ApplicationMain.class, args);

        AccountService accountService = (AccountService) ctx.getBean("accountServiceImpl");
        CategoryService categoryService = (CategoryService) ctx.getBean("categoryServiceImpl");
        TransactionService transactionService = (TransactionService) ctx.getBean("transactionServiceImpl");

        accountService.save(new Account("SampleAccount", new BigDecimal(10)));

        launch(args);
    }
}
