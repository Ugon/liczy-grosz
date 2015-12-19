package pl.edu.agh.iisg.to.to2project.domain.entity;

import com.google.common.base.Preconditions;
import javafx.beans.Observable;
import javafx.beans.property.*;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicObservableValue;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import pl.edu.agh.iisg.to.to2project.domain.ITransaction;

import javax.persistence.*;
import java.math.BigDecimal;


/**
 * @author Wojciech Pachuta.
 * @author Bartłomiej Grochal.
 */
@MappedSuperclass
public abstract class AbstractTransaction extends AbstractEntity implements ITransaction{

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "destinationAccount")
    private Account destinationAccountEntity;

    @Column(name = "delta", nullable = false)
    private BigDecimal deltaPOJO;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Column(name = "date", nullable = false)
    private LocalDate datePOJO;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "category")
    protected Category categoryEntity;

    @Column(name = "comment", nullable = true)
    private String commentPOJO;

    @Transient
    private final ObjectProperty<Account> destinationAccount;

    @Transient
    private final ObjectProperty<BigDecimal> delta;

    @Transient
    private final ObjectProperty<LocalDate> date;

    @Transient
    protected final ObjectProperty<Category> category;

    @Transient
    private final MonadicObservableValue<Category> categoryMonadic;

    @Transient
    private final StringProperty comment;

    @Transient
    private final MonadicObservableValue<String> commentMonadic;

    @Transient
    private final MonadicObservableValue<BigDecimal> destinationAccountBalanceAfterThisTransaction;

    AbstractTransaction() {
        this.destinationAccount = new SimpleObjectProperty<>();
        this.delta = new SimpleObjectProperty<>();
        this.date = new SimpleObjectProperty<>();
        this.category = new SimpleObjectProperty<>();
        this.categoryMonadic = EasyBind.monadic(category);
        this.comment = new SimpleStringProperty();
        this.commentMonadic = EasyBind.monadic(comment);
        this.destinationAccountBalanceAfterThisTransaction = EasyBind.monadic(destinationAccount).flatMap(acc -> acc.calculateBalanceAtInclusive(date));
    }

    public AbstractTransaction(Account destinationAccount, BigDecimal delta, LocalDate date) {
        this();
        setDelta(delta);
        setDate(date);
        setDestinationAccount(destinationAccount);
    }

    void initProperties() {
        destinationAccount.setValue(destinationAccountEntity);
        delta.setValue(deltaPOJO);
        date.setValue(datePOJO);
        category.setValue(categoryEntity);
        comment.setValue(commentPOJO);
    }

    void updatePOJOs(){
        destinationAccountEntity = destinationAccount.get();
        deltaPOJO = delta.get();
        datePOJO = date.get();
        categoryEntity = categoryMonadic.getOrElse(null);
        commentPOJO = commentMonadic.getOrElse(null);
    }


    @Override
    public Observable[] extractObservables() {
        return new Observable[] {destinationAccount, delta, date, categoryMonadic, commentMonadic,
                destinationAccountBalanceAfterThisTransaction, sourcePropertyAsMonadicString()};
    }


    abstract void updateDestinationAccounts(Account oldDestinationAccount, Account newDestinationAccount);

    public void setDestinationAccount(Account destinationAccount) {
        Preconditions.checkNotNull(destinationAccount);
        Account oldDestinationAccount = this.destinationAccount.get();
        this.destinationAccount.set(destinationAccount);
        updateDestinationAccounts(oldDestinationAccount, destinationAccount);
    }

    @Override
    public ReadOnlyObjectProperty<Account> destinationAccountProperty() {
        return destinationAccount;
    }


    public void setDelta(BigDecimal delta) {
        Preconditions.checkNotNull(delta);
        this.delta.set(delta);
    }

    @Override
    public MonadicObservableValue<BigDecimal> deltaProperty() {
        return EasyBind.monadic(delta);
    }


    public void setDate(LocalDate date) {
        Preconditions.checkNotNull(date);
        this.date.set(date);
    }

    public ReadOnlyObjectProperty<LocalDate> dateProperty() {
        return date;
    }


    abstract void updateCategorySet(Category oldCategory, Category newCategory);

    abstract void updateCategoryRemove(Category removedCategory);

    public void setCategory(Category category){
        Preconditions.checkNotNull(category);
        Preconditions.checkState(categoryMonadicProperty().isEmpty());
        Category oldCategory = this.category.get();
        this.category.set(category);
        updateCategorySet(oldCategory, category);
    }

    public void removeCategoryIfPresent(){
        if(categoryMonadicProperty().isPresent()){
            Category removedCategory = categoryMonadicProperty().get();
            this.category.set(null);
            updateCategoryRemove(removedCategory);
        }
    }

    @Override
    public MonadicObservableValue<Category> categoryMonadicProperty() {
        return categoryMonadic;
    }


    public void setComment(String comment) {
        Preconditions.checkNotNull(comment);
        Preconditions.checkArgument(!comment.isEmpty());
        this.comment.set(comment);
    }

    public void removeCommentIfPresent() {
        if (categoryMonadicProperty().isPresent()) {
            this.comment.set(null);
        }
    }

    @Override
    public MonadicObservableValue<String> commentMonadicProperty() {
        return commentMonadic;
    }


    @Override
    public MonadicObservableValue<BigDecimal> accountBalanceAfterThisTransaction(){
        return destinationAccountBalanceAfterThisTransaction;
    }


    public abstract MonadicObservableValue<String> sourcePropertyAsMonadicString();

}
