import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ExternalScene extends VBox {

    public ExternalScene(App app) {
        getChildren().add(Prefab.createTopBar(app.primary));

        VBox internal = new VBox();
        internal.setPrefSize(400,140);
        internal.getStylesheets().add("external.css");
        internal.setSpacing(5);
        internal.setPadding(new Insets(5));
        getChildren().add(internal);

        Label title = new Label("USB Password Manager");
        title.getStyleClass().add("title");
        internal.getChildren().add(title);

        Button submit = new Button("Open");
        submit.getStyleClass().add("submit");
        internal.getChildren().add(submit);

        submit.setOnAction(event -> {
            Session session = Session.newSession(app);
            if(session != null){
                app.session = session;
                app.currentState = App.State.OVERVIEW;
                app.updateScene();
            }
        });
    }
}
