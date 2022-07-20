
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.Serializable;

public class Account extends Item implements Serializable{
    private Service service;
    private String email;
    private String username;
    private String password;
    private String description;

    public Account(Service service, String email, String username, String password, String description) {
        this.service = service;
        this.email = email;
        this.username = username;
        this.password = password;
        this.description = description;
    }

    public Service getService() {
        return service;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDescription() {
        return description;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Parent getSimpleInfoBox(){
        GridPane infoBox = new GridPane();
        VBox internal = new VBox();
        infoBox.getStylesheets().add("infobox.css");
        infoBox.getStyleClass().add("infobox");
        internal.setSpacing(2);
        internal.setPadding(new Insets(5));

        Label serviceLabel = new Label(service.getName());
        serviceLabel.getStyleClass().add("service");
        internal.getChildren().add(serviceLabel);

        String identifyValue;
        if(tags.contains(Tag.IDENTIFY_BY_EMAIL)){
            identifyValue = email;
        }else{
            identifyValue = username;
        }
        Label identifierLabel = new Label(identifyValue);
        identifierLabel.getStyleClass().add("identifier");
        internal.getChildren().add(Prefab.addCopyButton(identifierLabel, identifyValue));

        HBox passwordLabel = Prefab.makeHidden(new Label(password));
        internal.getChildren().add(Prefab.addCopyButton(passwordLabel, password));

        Separator hsep = new Separator();
        internal.getChildren().add(hsep);


        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.getStyleClass().add("desc");
        internal.getChildren().add(descriptionLabel);

        infoBox.add(internal,0,0);

        return  infoBox;
    }

    @Override
    public Parent getInfoBox() {
        final boolean[] isServiceHidden = {true};

        GridPane infoBox = new GridPane();
        VBox internal = new VBox();
        infoBox.getStylesheets().add("infobox.css");
        infoBox.getStyleClass().add("infobox");
        internal.setSpacing(2);
        internal.setPadding(new Insets(5));

        Label serviceLabel = new Label(service.getName());
        serviceLabel.getStyleClass().add("service");
        internal.getChildren().add(serviceLabel);

        Parent serviceInfo = service.getSimpleInfoBox();
        internal.getChildren().add(serviceInfo);
        serviceInfo.setManaged(false);
        serviceInfo.setVisible(false);
        serviceLabel.setOnMouseClicked(event -> {
            serviceInfo.setManaged(isServiceHidden[0]);
            serviceInfo.setVisible(isServiceHidden[0]);
            isServiceHidden[0] = !isServiceHidden[0];
        });

        String identifyValue;
        if(tags.contains(Tag.IDENTIFY_BY_EMAIL)){
            identifyValue = email;
        }else{
            identifyValue = username;
        }
        Label identifierLabel = new Label(identifyValue);
        identifierLabel.getStyleClass().add("identifier");
        internal.getChildren().add(Prefab.addCopyButton(identifierLabel, identifyValue));

        HBox passwordLabel = Prefab.makeHidden(new Label(password));
        internal.getChildren().add(Prefab.addCopyButton(passwordLabel, password));

        Separator hsep = new Separator();
        internal.getChildren().add(hsep);


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
        if(!(maybeitem instanceof Account item)){
            System.err.println("SEVERE WARNING: Expected an account");
            return new HBox();
        }

        VBox internal = new VBox();
        internal.setPrefSize(400, 600);
        internal.getStylesheets().add("edit.css");
        internal.setSpacing(2);
        internal.setPadding(new Insets(5));

        Label title = new Label("Account Edit");
        title.getStyleClass().add("title");
        internal.getChildren().add(title);

        Label serviceLabel = new Label("Service:");
        serviceLabel.getStyleClass().add("input_label");
        internal.getChildren().add(serviceLabel);
        ComboBox<Service> serviceCombo = new ComboBox<>();
        serviceCombo.getStyleClass().add("input");
        internal.getChildren().add(serviceCombo);
        serviceCombo.getItems().addAll(app.session.services);
        serviceCombo.getSelectionModel().select(item.getService());
        serviceCombo.valueProperty().addListener(((observableValue, oldValue, newValue) -> item.setService(newValue)));

        Label usernameLabel = new Label("Username:");
        usernameLabel.getStyleClass().add("input_label");
        internal.getChildren().add(usernameLabel);
        TextField usernameField = new TextField(item.getUsername());
        usernameField.getStyleClass().add("input");
        internal.getChildren().add(usernameField);
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> item.setUsername(newValue));

        Label emailLabel = new Label("Email:");
        emailLabel.getStyleClass().add("input_label");
        internal.getChildren().add(emailLabel);
        TextField emailField = new TextField(item.getEmail());
        emailField.getStyleClass().add("input");
        internal.getChildren().add(emailField);
        emailField.textProperty().addListener((observable, oldValue, newValue) -> item.setEmail(newValue));

        Label passwordLabel = new Label("Password:");
        passwordLabel.getStyleClass().add("input_label");
        internal.getChildren().add(passwordLabel);
        TextField passwordField = new TextField(item.getPassword());
        passwordField.getStyleClass().add("input");
        internal.getChildren().add(passwordField);
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> item.setPassword(newValue));

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
    public String searchString(){
        StringBuilder tagString = new StringBuilder();
        for(Tag t: tags){
            tagString.append(t.getName()).append(" ");
        }
        return service.getName() + " " + username + " " + email + " " + tagString + " " + description;
    }

    @Override
    public String toString(){
        return service.getName() + " @ " + username;
    }

}