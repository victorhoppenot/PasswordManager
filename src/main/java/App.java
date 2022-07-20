import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class App extends Application {

    public enum State {
        EXTERNAL,
        OVERVIEW
    }
    public Session session;
    public Stage primary;

    public void updateScene(){
        primary.setScene(getScene(currentState));
    }

    public Scene getScene(State s){
        return new Scene(switch(s){
            case EXTERNAL -> new ExternalScene(this);
            case OVERVIEW -> new OverviewScene(this);
        });
    }
    public State currentState = State.EXTERNAL;
    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primary) {
        this.primary = primary;
        primary.setTitle("Password Manager");
        primary.initStyle(StageStyle.UNDECORATED);
        primary.setScene(getScene(currentState));
        primary.show();
    }


}