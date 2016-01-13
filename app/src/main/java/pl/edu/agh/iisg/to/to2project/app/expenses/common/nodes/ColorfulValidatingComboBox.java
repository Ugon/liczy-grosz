package pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.function.BooleanSupplier;

/**
 * @author Wojciech Pachuta.
 */
public class ColorfulValidatingComboBox<T> extends ComboBox<T> implements IColorfulValidatingNode{

    private BooleanSupplier validationSupplier;

    public ColorfulValidatingComboBox() {
        this(FXCollections.<T>observableArrayList());
    }

    public ColorfulValidatingComboBox(ObservableList<T> items) {
        super(items);
        setValidationSupplier(() -> true);
        valueProperty().addListener((observable, oldValue, newValue) -> {
            validate();
        });
    }

    private void validate(){
        if(!validationSupplier.getAsBoolean()){
            setStyle("-fx-background-color: red");
        }
        else{
            setStyle("");
        }
    }

    @Override
    public void setValidationSupplier(BooleanSupplier supplier){
        validationSupplier = supplier;
    }

    @Override
    public void triggerValidation(){
        validate();
    }
}
