import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProxyAccount extends Item {

    private Service service;
    private Item proxy;
    private String description;

    public ProxyAccount(Service service, Item proxy, String description) {
        this.service = service;
        this.proxy = proxy;
        this.description = description;
    }

    public Service getService() {
        return service;
    }
    public Item getProxy() {
        return proxy;
    }
    public String getDescription() {
        return description;
    }

    public void setService(Service service) {
        this.service = service;
    }
    public void setProxy(Item proxy){
        this.proxy = proxy;
    }
    public void setDescription(String description) {
        this.description = description;
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

        Parent proxyInfo = proxy.getSimpleInfoBox();
        internal.getChildren().add(proxyInfo);

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
    public Parent getSimpleInfoBox() {
        GridPane infoBox = new GridPane();
        VBox internal = new VBox();
        infoBox.getStylesheets().add("infobox.css");
        infoBox.getStyleClass().add("infobox");
        internal.setSpacing(2);
        internal.setPadding(new Insets(5));

        Label serviceLabel = new Label(service.getName());
        serviceLabel.getStyleClass().add("service");
        internal.getChildren().add(serviceLabel);

        Parent proxyInfo = proxy.getSimpleInfoBox();
        internal.getChildren().add(proxyInfo);

        Separator hsep = new Separator();
        internal.getChildren().add(hsep);


        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.getStyleClass().add("desc");
        internal.getChildren().add(descriptionLabel);

        infoBox.add(internal,0,0);

        return infoBox;
    }

    @Override
    public <T> Parent getEditBox(T maybeitem, App app) {
        if(!(maybeitem instanceof ProxyAccount item)){
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

        Label proxyLabel = new Label("Proxy:");
        proxyLabel.getStyleClass().add("input_label");
        internal.getChildren().add(proxyLabel);
        ComboBox<Item> proxyCombo = new ComboBox<>();
        proxyCombo.getStyleClass().add("input");
        internal.getChildren().add(proxyCombo);
        proxyCombo.getItems().addAll(app.session.items);
        proxyCombo.getSelectionModel().select(item.getProxy());
        proxyCombo.valueProperty().addListener(((observableValue, oldValue, newValue) -> item.setProxy(newValue)));

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
        return service.getName() + " " + description + " " + tagString + " " + proxy.searchString();
    }

    @Override
    public String toString(){
        return service.getName() + " @ " + proxy.toString();
    }
}
