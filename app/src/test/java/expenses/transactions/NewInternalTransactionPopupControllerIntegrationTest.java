package expenses.transactions;

import javafx.collections.FXCollections;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import pl.edu.agh.iisg.to.to2project.app.core.ApplicationMain;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes.ColorfulValidatingComboBox;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes.ColorfulValidatingDatePicker;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes.ColorfulValidatingTextField;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.NewInternalTransactionPopupController;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.TransactionsController;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;
import pl.edu.agh.iisg.to.to2project.persistence.generic.AccountDAO;
import pl.edu.agh.iisg.to.to2project.persistence.generic.CategoryDAO;
import pl.edu.agh.iisg.to.to2project.persistence.generic.InternalTransactionDAO;
import pl.edu.agh.iisg.to.to2project.service.AccountService;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;
import pl.edu.agh.iisg.to.to2project.service.InternalTransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Wojciech Pachuta.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationMain.class, NewInternalTransactionPopupControllerIntegrationTest.TestConfig.class})
public class NewInternalTransactionPopupControllerIntegrationTest {

    @Configuration
    public static class TestConfig {
        @Bean
        @Primary
        public AccountDAO accountDAOImpl() {
            return mock(AccountDAO.class);
        }

        @Bean
        @Primary
        public CategoryDAO categoryDAOImpl() {
            return mock(CategoryDAO.class);
        }

        @Bean
        @Primary
        public InternalTransactionDAO internalTransactionDAOImpl() {
            return mock(InternalTransactionDAO.class);
        }

        @Bean
        @Primary
        public TransactionsController transactionsController() {
            return mock(TransactionsController.class);
        }
    }

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private InternalTransactionDAO internalTransactionDAO;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private InternalTransactionService internalTransactionService;

    private NewInternalTransactionPopupController instance;

    private ColorfulValidatingComboBox<Account> destinationAccountCombo;
    private ColorfulValidatingComboBox<Account> sourceAccountCombo;
    private ComboBox<Category> categoryCombo;
    private ColorfulValidatingTextField transferTextField;
    private ColorfulValidatingDatePicker datePicker;
    private TextArea commentTextArea;

    private Account sourceAccount;
    private Account destinationAccount;
    private Category category;
    private LocalDate localDate;
    private String comment;
    private String transfer;

    @Before
    public void setUp() throws Exception {
        new JFXPanel();
        init();
        instance = context.getBean(NewInternalTransactionPopupController.class);
        ReflectionTestUtils.setField(instance, "dialogStage", mock(Stage.class));
        setFieldsWithReflection();
        fillFXMLFields();
        ReflectionTestUtils.invokeMethod(instance, "initialize");

        assertThat((Boolean) ReflectionTestUtils.invokeMethod(instance, "isInputValid")).isTrue();
    }

    private void init() {
        sourceAccount = new Account("acc1", new BigDecimal(1));
        destinationAccount = new Account("acc2", new BigDecimal(2));
        category = new Category("cat 1");
        localDate = LocalDate.now();
        comment = "comment";
        transfer = "10.00";
        sourceAccount.setId(1L);
        sourceAccount.setId(2L);

        when(accountDAO.findAll()).thenReturn(FXCollections.observableArrayList(sourceAccount, destinationAccount));
        when(categoryDAO.findAll()).thenReturn(FXCollections.observableArrayList(category));
    }

    private void setFieldsWithReflection() {
        destinationAccountCombo = new ColorfulValidatingComboBox<>();
        sourceAccountCombo = new ColorfulValidatingComboBox<>();
        categoryCombo = new ColorfulValidatingComboBox<>();
        transferTextField = new ColorfulValidatingTextField();
        datePicker = new ColorfulValidatingDatePicker();
        commentTextArea = new TextArea();

        ReflectionTestUtils.setField(instance, "destinationAccountCombo", destinationAccountCombo);
        ReflectionTestUtils.setField(instance, "sourceAccountCombo", sourceAccountCombo);
        ReflectionTestUtils.setField(instance, "categoryCombo", categoryCombo);
        ReflectionTestUtils.setField(instance, "transferTextField", transferTextField);
        ReflectionTestUtils.setField(instance, "datePicker", datePicker);
        ReflectionTestUtils.setField(instance, "commentTextArea", commentTextArea);
    }

    private void fillFXMLFields() {
        destinationAccountCombo.setValue(destinationAccount);
        sourceAccountCombo.setValue(sourceAccount);
        categoryCombo.setValue(category);
        transferTextField.setText(transfer);
        datePicker.setValue(localDate);
        commentTextArea.setText(comment);
    }

    @Test
    public void shouldCreatedTransactionHaveCorrectProperties() throws Exception {
        //given
        //when
        ReflectionTestUtils.invokeMethod(instance, "handleOKButtonClick", new ActionEvent());
        ArgumentCaptor<InternalTransaction> internalTransactionArgumentCaptor = ArgumentCaptor.forClass(InternalTransaction.class);
        verify(internalTransactionDAO).saveOrUpdate(internalTransactionArgumentCaptor.capture());
        InternalTransaction internalTransaction = internalTransactionArgumentCaptor.getValue();
        //then
        assertThat(internalTransaction.destinationAccountProperty().get()).isEqualTo(destinationAccount);
        assertThat(internalTransaction.sourceAccountProperty().get()).isEqualTo(sourceAccount);
        assertThat(internalTransaction.commentMonadicProperty().get()).isEqualTo(comment);
        assertThat(internalTransaction.deltaProperty().get().compareTo(new BigDecimal(transfer))).isEqualTo(0);

    }
}
