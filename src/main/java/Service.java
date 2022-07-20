import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Service implements Searchable{
    public static final Service NO_SERVICE = new Service("No Service", "This item has no service");
    private String name;
    private String description;
    public final TagList tags;

    public Service(String name, String description) {
        this.name = name;
        this.description = description;
        this.tags = new TagList(Service.class);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String searchString() {
        StringBuilder tagString = new StringBuilder();
        for(Tag t: tags){
            tagString.append(t.getName()).append(" ");
        }
        return name + " " + tagString + " " + description;
    }

    @Override
    public Parent getSimpleInfoBox(){
        VBox vbox = new VBox();
        vbox.getStylesheets().add("infobox.css");
        vbox.getStyleClass().add("infobox");
        vbox.setSpacing(2);
        vbox.setPadding(new Insets(5));

        Label serviceLabel = new Label(name);
        serviceLabel.getStyleClass().add("service");
        vbox.getChildren().add(serviceLabel);

        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.getStyleClass().add("desc");
        vbox.getChildren().add(descriptionLabel);

        return vbox;
    }
    public Parent getInfoBox() {
        GridPane infoBox = new GridPane();
        VBox internal = new VBox();

        infoBox.getStylesheets().add("infobox.css");
        infoBox.getStyleClass().add("infobox");
        internal.setSpacing(2);
        internal.setPadding(new Insets(5));

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("service");
        internal.getChildren().add(nameLabel);

        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.getStyleClass().add("desc");
        internal.getChildren().add(descriptionLabel);

        infoBox.add(internal,0,0);

        Separator vsep = new Separator();
        vsep.setOrientation(Orientation.VERTICAL);

        infoBox.add(vsep,1,0);


        VBox tagBox = new VBox();
        tagBox.setFillWidth(true);
        tagBox.setAlignment(Pos.BASELINE_RIGHT);
        tagBox.setSpacing(2);
        tagBox.setPadding(new Insets(0, 5, 5, 5));


        Label tagTitle = new Label("Tags");
        tagTitle.getStyleClass().add("service");
        tagBox.getChildren().add(tagTitle);

        int maxTags = 4;
        for(Tag t : tags){
            maxTags--;
            if(maxTags == 0){
                Label extraTags = new Label(". . .");
                extraTags.getStyleClass().add("identifier");
                tagBox.getChildren().add(extraTags);
                break;
            }
            Label tag = new Label(t.getName());
            tag.getStyleClass().add("identifier");
            tagBox.getChildren().add(tag);
        }

        infoBox.add(tagBox,2,0);

        ColumnConstraints internalConst = new ColumnConstraints();
        ColumnConstraints septConst = new ColumnConstraints();
        ColumnConstraints tagBoxConst = new ColumnConstraints();
        internalConst.setPercentWidth(60);
        septConst.setPercentWidth(2);
        tagBoxConst.setPercentWidth(38);
        infoBox.getColumnConstraints().addAll(internalConst,septConst,tagBoxConst);

        return infoBox;
    }

    @Override
    public <T> Parent getEditBox(T maybeitem, App app) {
        if(!(maybeitem instanceof Service item)){
            System.err.println("SEVERE WARNING: Expected a service");
            return new HBox();
        }

        VBox internal = new VBox();
        internal.setPrefSize(400, 600);
        internal.getStylesheets().add("edit.css");
        internal.setSpacing(2);
        internal.setPadding(new Insets(5));

        Label title = new Label("Service Edit");
        title.getStyleClass().add("title");
        internal.getChildren().add(title);


        Label nameLabel = new Label("Name:");
        nameLabel.getStyleClass().add("input_label");
        internal.getChildren().add(nameLabel);
        TextField usernameField = new TextField(item.getName());
        usernameField.getStyleClass().add("input");
        internal.getChildren().add(usernameField);
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> item.setName(newValue));


        Label descLabel = new Label("Description:");
        descLabel.getStyleClass().add("input_label");
        internal.getChildren().add(descLabel);
        TextArea descField = new TextArea(item.getDescription());
        descField.setWrapText(true);
        descField.getStyleClass().add("input");
        internal.getChildren().add(descField);
        descField.textProperty().addListener((observable, oldValue, newValue) -> item.setDescription(newValue));

        TagField tagField = new TagField(app.session.tags, item.tags);
        internal.getChildren().add(tagField);

        return internal;
    }

    @Override
    public String toString(){
        return name;
    }
}

