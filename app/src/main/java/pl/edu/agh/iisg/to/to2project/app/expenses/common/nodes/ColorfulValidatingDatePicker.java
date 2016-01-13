package pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes;

import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.util.function.BooleanSupplier;

/**
 * @author Wojciech Pachuta.
 */
public class ColorfulValidatingDatePicker extends DatePicker implements IColorfulValidatingNode {

    private BooleanSupplier validationSupplier;

    public ColorfulValidatingDatePicker() {
        this(null);
    }

    public ColorfulValidatingDatePicker(LocalDate localDate) {
        super(localDate);
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
    public void setValidationSupplier(BooleanSupplier supplier) {
        this.validationSupplier = supplier;
    }

    @Override
    public void triggerValidation() {
        validate();
    }
}
