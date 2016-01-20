package pl.edu.agh.iisg.to.to2project.domain.entity.budget;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Created by mike on 13.01.16.
 */
public class LabeledProgressBar {
    private VBox vbox;
    private TextField textField;
    private ProgressBar progressBar;

    public LabeledProgressBar(VBox vbox) {
        this.vbox = vbox;
        textField = new TextField();
        textField.setEditable(false);
        progressBar = new ProgressBar();
        progressBar.setPrefWidth(180.0);
        add();
    }

    public void add() {
        vbox.getChildren().add(textField);
        vbox.getChildren().add(progressBar);
    }

    private void updateTextField(DisplayedItem item) {
        textField.setText(item.getCategoryName());
        item.getCategoryNameProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal) {
                textField.setText((String) newVal);
            }
        });
    }

    private void setProgressBarValue(double value, boolean isSpending) {
        progressBar.setProgress(value);

        if ((isSpending && value > 1.0) || (!isSpending && value < 0.5))
            progressBar.setStyle("-fx-accent: red;");
        else
            progressBar.setStyle("-fx-accent: deepskyblue;");
    }

    private void updateProgressBar(DisplayedItem item) {
        setProgressBarValue(item.getProgressBarStateValue(), item.isSpending());
        item.getProgressBarStateBinding().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal) {
                setProgressBarValue(item.getProgressBarStateBinding().getValue(), item.isSpending());
            }
        });
    }

    public void update(DisplayedItem item) {
        updateTextField(item);
        updateProgressBar(item);
    }

    public void remove() {
        vbox.getChildren().remove(textField);
        vbox.getChildren().remove(progressBar);
    }

}
