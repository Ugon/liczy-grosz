package pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes;

import javafx.scene.control.RadioButton;

import java.util.function.BooleanSupplier;

/**
 * @author Wojciech Pachuta.
 */
public class ColorfulValidatingRadioButton extends RadioButton implements IColorfulValidatingNode{

    private BooleanSupplier validationSupplier;

    public ColorfulValidatingRadioButton() {
        this("");
    }

    public ColorfulValidatingRadioButton(String text) {
        super(text);
        setValidationSupplier(() -> true);
        selectedProperty().addListener((observable, oldValue, newValue) -> {
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
