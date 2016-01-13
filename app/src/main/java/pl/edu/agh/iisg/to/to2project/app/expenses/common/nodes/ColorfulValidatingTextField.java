package pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes;

import javafx.scene.control.TextField;

import java.util.function.BooleanSupplier;

/**
 * @author Wojciech Pachuta.
 */
public class ColorfulValidatingTextField extends TextField implements IColorfulValidatingNode{

    private BooleanSupplier validationSupplier;

    public ColorfulValidatingTextField() {
        this("");
    }

    public ColorfulValidatingTextField(String text) {
        super(text);
        setValidationSupplier(() -> true);
        textProperty().addListener((observable, oldValue, newValue) -> {
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
