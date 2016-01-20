package pl.edu.agh.iisg.to.to2project.domain.entity;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Wojciech Pachuta.
 */
public class InternalTransactionTest {

    private Account sourceAccount;
    private Account destinationAccount;

    private InternalTransaction internalTransaction1;
    private InternalTransaction internalTransaction2;
    private InternalTransaction internalTransaction3;

    private LocalDate localDate1;
    private LocalDate localDate2;
    private LocalDate localDate3;

    private BigDecimal bigDecimal1;
    private BigDecimal bigDecimal2;
    private BigDecimal bigDecimal3;

    @Before
    public void setUp() throws Exception {
        localDate1 = new LocalDate(10);
        localDate2 = localDate1.plusYears(1);
        localDate3 = localDate2.plusYears(1);
        bigDecimal1 = new BigDecimal(10);
        bigDecimal2 = new BigDecimal(100);
        bigDecimal3 = new BigDecimal(1000);
        sourceAccount = new Account("sourceAccount", new BigDecimal(1));
        sourceAccount.setId(1L);
        destinationAccount = new Account("destinationAccount", new BigDecimal(2));
        destinationAccount.setId(2L);
        internalTransaction2 = new InternalTransaction(sourceAccount, destinationAccount, bigDecimal2, localDate2);
        internalTransaction2.setId(2L);
    }

    @Test
    public void shouldCreatingInternalTransactionAddItToAccounts() throws Exception {
        //given
        //when
        internalTransaction1 = new InternalTransaction(sourceAccount, destinationAccount, bigDecimal1, localDate1);
        //then
        assertThat(sourceAccount.getInternalTransactionSourceSet()).contains(internalTransaction1);
        assertThat(destinationAccount.getInternalTransactionDestinationSet()).contains(internalTransaction1);
    }

    @Test
    public void shouldBalanceAfterTransactionBeCalculatedProperlyAfterAddingTransactionBefore() throws Exception {
        //given

        //when
        internalTransaction1 = new InternalTransaction(sourceAccount, destinationAccount, bigDecimal1, localDate1);
        //then
        BigDecimal balanceAfter = destinationAccount.initialBalanceProperty().get()
                .add(bigDecimal1).add(bigDecimal2);
        assertThat(internalTransaction2.accountBalanceAfterThisTransaction().get().compareTo(balanceAfter)).isEqualTo(0);
    }

    @Test
    public void shouldBalanceAfterTransactionBeCalculatedProperlyAfterAddingTransactionAfter() throws Exception {
        //given

        //when
        internalTransaction3 = new InternalTransaction(sourceAccount, destinationAccount, bigDecimal3, localDate3);
        //then
        BigDecimal balanceAfter = destinationAccount.initialBalanceProperty().get()
                .add(bigDecimal2);
        System.out.println(internalTransaction2.accountBalanceAfterThisTransaction().get());
        assertThat(internalTransaction2.accountBalanceAfterThisTransaction().get().compareTo(balanceAfter)).isEqualTo(0);
    }


}
