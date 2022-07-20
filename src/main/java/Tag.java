import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Tag implements Searchable{

    public final static Tag IDENTIFY_BY_EMAIL = new Tag(Account.class, "Identify by Email", false);

    public final static Class<?>[] tagTypes = new Class[]{Account.class, Service.class, Item.class, ProxyAccount.class, Searchable.class};

    private Class<?> type;

    private String name;
    public final boolean isRemovable;
    public Tag(Class<?> type, String name, boolean isRemovable){
        this.type = type;
        this.name = name;
        this.isRemovable = isRemovable;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }


    public boolean isValidFor(Class<?> c) {
        return type.isAssignableFrom(c);
    }

    public static Tag createRandomTag(Class<?> type){
        String name = Util.randomString(8);
        return new Tag(type, name, true);
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public Parent getInfoBox() {
        VBox vbox = new VBox();
        vbox.getStylesheets().add("infobox.css");
        vbox.getStyleClass().add("infobox");
        vbox.setSpacing(2);
        vbox.setPadding(new Insets(5));

        Label serviceLabel = new Label(name);
        serviceLabel.getStyleClass().add("service");
        vbox.getChildren().add(serviceLabel);
        return vbox;
    }

    @Override
    public Parent getSimpleInfoBox() {
        return getInfoBox();
    }

    @Override
    public <T> Parent getEditBox(T maybeitem, App app) {
        if(!(maybeitem instanceof Tag item)){
            System.err.println("SEVERE WARNING: Expected a tag");
            return new HBox();
        }

        VBox internal = new VBox();
        internal.setPrefSize(400, 600);
        internal.getStylesheets().add("edit.css");
        internal.setSpacing(2);
        internal.setPadding(new Insets(5));

        Label title = new Label("Tag Edit");
        title.getStyleClass().add("title");
        internal.getChildren().add(title);

        Label typeLabel = new Label("Type:");
        typeLabel.getStyleClass().add("input_label");
        internal.getChildren().add(typeLabel);
        ComboBox<Class<?>> typeCombo = new ComboBox<>();
        typeCombo.getStyleClass().add("input");
        internal.getChildren().add(typeCombo);
        typeCombo.getItems().addAll(Tag.tagTypes);
        typeCombo.getSelectionModel().select(item.type);
        typeCombo.valueProperty().addListener(((observableValue, oldValue, newValue) -> item.type = newValue));

        Label nameLabel = new Label("Name:");
        nameLabel.getStyleClass().add("input_label");
        internal.getChildren().add(nameLabel);
        TextField nameField = new TextField(item.getName());
        nameField.getStyleClass().add("input");
        internal.getChildren().add(nameField);
        nameField.textProperty().addListener((observable, oldValue, newValue) -> item.setName(newValue));


        return internal;
    }

    @Override
    public String searchString() {
        return name;
    }
}