import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddWindow extends Stage {

    private final App app;

    public AddWindow(App app){
        this.app = app;
        setTitle("Password Manager");
        initStyle(StageStyle.UNDECORATED);
        setAlwaysOnTop(true);
    }
    public void edit(Searchable thing){
        if(thing == null){
            return;
        }

        show();
        app.primary.hide();

        VBox vbox = new VBox();
        vbox.getChildren().add(Prefab.createTopBar(this, event -> {
            app.primary.show();
            hide();
        }));

        Parent internal = thing.getEditBox(thing, app);
        vbox.getChildren().add(internal);

        setScene(new Scene(vbox));
    }
}
