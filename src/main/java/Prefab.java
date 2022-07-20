import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class Prefab {
    private static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public static HBox createTopBar(Stage stage, EventHandler<ActionEvent> exitEvent){
        final double[] xOffset = {0};
        final double[] yOffset = {0};

        HBox topbar = new HBox();
        topbar.getStylesheets().add("topbar.css");
        topbar.getStyleClass().add("topbar");
        topbar.setAlignment(Pos.BASELINE_RIGHT);

        Button closeBtn = new Button("x");
        closeBtn.getStyleClass().add("exit");

        closeBtn.setOnAction(exitEvent);

        topbar.getChildren().add(closeBtn);

        topbar.setOnMousePressed(event -> {
            xOffset[0] = (stage.getX() - event.getScreenX());
            yOffset[0] = (stage.getY() - event.getScreenY());
        });

        topbar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset[0]);
            stage.setY(event.getScreenY() + yOffset[0]);
        });

        return topbar;
    }

    public static HBox createTopBar(Stage stage){
        return createTopBar(stage, e -> Platform.exit());
    }

    public static HBox addCopyButton(Node thing, String copyValue){
        HBox copyLabel = new HBox();
        copyLabel.setSpacing(3);

        copyLabel.getChildren().add(thing);
        //⎘
        Button copy = new Button("⎘");
        copy.setPadding(new Insets(-2,2,-2,2));
        copy.getStylesheets().add("icongen.css");
        copy.getStyleClass().add("icon");
        copyLabel.getChildren().add(copy);

        copy.setOnAction(event -> {
            StringSelection selection = new StringSelection(copyValue);
            clipboard.setContents(selection,null);
        });


        return copyLabel;
    }

    public static HBox makeHidden(Label label){
        final boolean[] isHidden = {true};
        HBox hidden = new HBox();
        hidden.setSpacing(3);

        hidden.getChildren().add(label);

        String unhiddenText = label.getText();
        //"👁"
        String passwordChar = "●";
        String hiddenText = passwordChar.repeat(unhiddenText.length());

        label.setText(hiddenText);

        Button hide = new Button("👁");
        hide.setPadding(new Insets(-2,1.5,-2,1.5));
        hide.getStylesheets().add("icongen.css");
        hide.getStyleClass().add("icon");
        hidden.getChildren().add(hide);
        hide.setOnAction(event -> {
            if(isHidden[0]){
                label.setText(unhiddenText);
            }else{
                label.setText(hiddenText);
            }
            isHidden[0] = !isHidden[0];
        });

        return hidden;
    }
}
