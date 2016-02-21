package pl.edu.agh.iisg.to.to2project.app.stats.util.entity;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class BorderedTitledPane extends StackPane {
    public BorderedTitledPane(){
        getStyleClass().add("darkgreyBorder");
    }

    public BorderedTitledPane(String titleString, Node content) {
        this();
        Label title = new Label(" " + titleString + " ");
        StackPane.setAlignment(title, Pos.TOP_CENTER);

        StackPane contentPane = new StackPane();
        contentPane.getChildren().add(content);
        getChildren().addAll(title, contentPane);
    }

    public void setText(String text){
        Label title = new Label(" " + text + " ");
        StackPane.setAlignment(title, Pos.TOP_CENTER);
        getChildren().addAll(title);
    }
}