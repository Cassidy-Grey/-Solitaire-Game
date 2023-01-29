/**
 * The Name class, for saving a username
 * @author Cassidy
 * @version 1.5
 */

package uk.ac.aber.dcs.cs12320.cards.gui.javafx;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Name {
    private final Stage stage;
    public String name;

    public Name(Stage stage) {
        this.stage = stage;
        stage.setTitle("The Cards");
        name = "";
    }

    public void nameField() {
        Platform.runLater(() -> {
            Label message = new Label("Enter your name to save your score to the leaderboard!");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));

            grid.add(message, 0, 0, 2, 1);
            Button btn = new Button("Submit");
            Label lbl = new Label("Enter name:");
            lbl.setId("btnlabel");
            TextField textField = new TextField();
            textField.setMaxWidth(200);

            HBox hbBtn = new HBox();
            hbBtn.getChildren().add(btn);
            hbBtn.setAlignment(Pos.BOTTOM_RIGHT);

            btn.setOnAction(e -> name = textField.getText());

            grid.add(lbl, 0, 1);
            grid.add(textField, 1, 1);
            grid.add(hbBtn, 1, 2);

            Scene scene = new Scene(grid, 700, 250);
            message.setId("message");
            stage.setScene(scene);
            stage.show();
        });
    }

    public String getName() {
        return name;
    }
}
