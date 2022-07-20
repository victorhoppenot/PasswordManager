import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.Collections;

public class OverviewScene extends VBox {
    private boolean minimized = false;

    private final App app;

    private final VBox scrollBox;

    private final TextField searchField;

    private final ComboBox<SearchOption> searchCombo;

    private final ComboBox<Tag> tagCombo;

    private final ComboBox<AddOption> addCombo;

    private enum SearchOption{
        Items,
        Services,
        Tagged,
    }

    public enum AddOption{
        Account,
        Service,
        Tag,
        ProxyAccount,
    }

    public OverviewScene(App app){
        this.app = app;

        this.setMinSize(400, 0);
        app.primary.setAlwaysOnTop(true);


        HBox tb = Prefab.createTopBar(app.primary, event -> app.session.saveExit());
        this.getChildren().add(tb);

        VBox internal = new VBox();
        internal.setPrefWidth(400);
        internal.getStylesheets().add("overview.css");
        internal.setSpacing(5);
        internal.setPadding(new Insets(5));
        this.getChildren().add(internal);


        Button miniBtn = new Button("-");
        miniBtn.getStyleClass().add("mini");

        miniBtn.setOnAction(event -> {
            internal.setManaged(minimized);
            internal.setVisible(minimized);
            minimized = !minimized;
            app.primary.sizeToScene();
        });

        tb.getChildren().add(miniBtn);


        Label title = new Label("Password Manager");
        title.getStyleClass().add("title");
        internal.getChildren().add(title);


        HBox searchBox = new HBox();
        searchBox.setSpacing(2);

        searchField = new TextField();
        searchField.getStyleClass().add("search_field");


        tagCombo = new ComboBox<>();
        tagCombo.getItems().addAll(app.session.tags);
        tagCombo.getStyleClass().add("search_combo");
        tagCombo.setVisible(false);
        tagCombo.setManaged(false);

        Button removeTagButton = new Button("🗑");
        removeTagButton.getStylesheets().add("icongen.css");
        removeTagButton.getStyleClass().add("icon");
        removeTagButton.setVisible(false);
        removeTagButton.setManaged(false);
        removeTagButton.setOnAction(event -> {
            if(!tagCombo.getValue().isRemovable){
                return;
            }
            app.session.tags.remove(tagCombo.getValue());
            tagCombo.getItems().remove(tagCombo.getValue());
        });

        searchCombo = new ComboBox<>();
        searchCombo.getItems().addAll(SearchOption.values());
        searchCombo.getStyleClass().add("search_combo");



        //🔎
        Button searchButton = new Button("🔎");
        searchButton.getStylesheets().add("icongen.css");
        searchButton.getStyleClass().add("icon");


        searchBox.getChildren().add(searchField);
        searchBox.getChildren().add(tagCombo);
        searchBox.getChildren().add(searchCombo);
        searchBox.getChildren().add(searchButton);
        searchBox.getChildren().add(removeTagButton);

        internal.getChildren().add(searchBox);



        scrollBox = new VBox();
        ScrollPane scrollPane = new ScrollPane(scrollBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.getStyleClass().add("scroll_pane");
        internal.getChildren().add(scrollPane);

        scrollBox.setSpacing(3);
        scrollBox.setPadding(new Insets(3));


        searchButton.setOnAction(this::search);

        searchCombo.setOnAction(event ->{
            if(searchCombo.getValue() == SearchOption.Tagged){
                searchField.setVisible(false);
                searchField.setManaged(false);
                tagCombo.setVisible(true);
                tagCombo.setManaged(true);
                removeTagButton.setVisible(true);
                removeTagButton.setManaged(true);
                tagCombo.getItems().clear();
                tagCombo.getItems().addAll(app.session.tags);
            }else{
                searchField.setVisible(true);
                searchField.setManaged(true);
                tagCombo.setVisible(false);
                tagCombo.setManaged(false);
                removeTagButton.setVisible(false);
                removeTagButton.setManaged(false);
            }
        });

        HBox addBox = new HBox();
        addCombo = new ComboBox<>();
        addCombo.getItems().addAll(AddOption.values());
        addCombo.getStyleClass().add("search_combo");
        addBox.getChildren().add(addCombo);
        Button addButton = new Button("+");
        addButton.getStylesheets().add("icongen.css");
        addButton.getStyleClass().add("icon");
        addBox.getChildren().add(addButton);
        internal.getChildren().add(addBox);
        addButton.setOnAction(this::createThing);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private Parent boxWithEdit(Searchable thing){
        Parent box = thing.getInfoBox();
        GridPane grid = new GridPane();

        VBox buttonBox = new VBox();
        //⚙
        Button edit = new Button("⚙");
        edit.setPadding(new Insets(-2,2,-2,2));
        edit.getStylesheets().add("icongen.css");
        edit.getStyleClass().add("icon");
        edit.setOnAction(event -> {
            app.session.edit(thing);
            scrollBox.getChildren().clear();
        });

        Button delete = new Button("🗑");
        delete.setPadding(new Insets(-2,3,-2,3));
        delete.getStylesheets().add("icongen.css");
        delete.getStyleClass().add("icon");
        delete.setOnAction(event -> {
            app.session.tags.remove(thing);
            app.session.services.remove(thing);
            app.session.items.remove(thing);
            scrollBox.getChildren().remove(grid);
        });

        buttonBox.getChildren().add(edit);
        buttonBox.getChildren().add(delete);

        ColumnConstraints boxConst = new ColumnConstraints();
        ColumnConstraints buttonCost = new ColumnConstraints();
        boxConst.setPercentWidth(96);
        buttonCost.setPercentWidth(4);
        grid.getColumnConstraints().addAll(boxConst, buttonCost);
        grid.add(box,0, 0);
        grid.add(buttonBox,1,0);
        return grid;

    }

    private void createThing(ActionEvent e){
        if(addCombo.getSelectionModel().isEmpty()){
            return;
        }
        Searchable thing = null;
        switch(addCombo.getValue()) {
            case Tag -> {
                Tag tag = new Tag(Util.None.class, "", true);
                app.session.tags.add(tag);
                thing = tag;

            }
            case Account -> {
                Account account = new Account(Service.NO_SERVICE, "","", "", "");
                app.session.items.add(account);
                thing = account;
            }
            case Service -> {
                Service service = new Service("", "");
                app.session.services.add(service);
                thing = service;
            }
            case ProxyAccount -> {
                ProxyAccount proxyAccount = new ProxyAccount(Service.NO_SERVICE, Item.NO_ITEM, "");
                app.session.items.add(proxyAccount);
                thing = proxyAccount;
            }
        }
        if(thing != null){
            app.session.edit(thing);
            scrollBox.getChildren().clear();
        }
    }
    private void search(ActionEvent e){
        if(searchCombo.getSelectionModel().isEmpty()){
            return;
        }
        switch (searchCombo.getValue()) {
            case Services -> {
                scrollBox.getChildren().clear();
                ArrayList<Service> searchedServices = Searchable.searchFor(app.session.services, searchField.getText());
                for (Service i : searchedServices) {

                    scrollBox.getChildren().add(boxWithEdit(i));
                }
            }
            case Items -> {
                scrollBox.getChildren().clear();
                ArrayList<Item> searchedItems = Searchable.searchFor(app.session.items, searchField.getText());
                for (Item i : searchedItems) {
                    scrollBox.getChildren().add(boxWithEdit(i));
                }
            }
            case Tagged -> {
                if(tagCombo.getSelectionModel().isEmpty()){
                    return;
                }

                scrollBox.getChildren().clear();

                Tag tag = tagCombo.getValue();

                ArrayList<HasOrder<Searchable>> outOrder = new ArrayList<>();
                for(Item i : app.session.items){
                    int a = i.tags.indexOf(tag);
                    if(a != -1){
                        outOrder.add(new HasOrder<>(i, a));
                    }
                }
                for(Service s : app.session.services){
                    int a = s.tags.indexOf(tag);
                    if(a != -1){
                        outOrder.add(new HasOrder<>(s, a));
                    }
                }

                Collections.sort(outOrder);
                for(HasOrder<Searchable> i : outOrder){
                    scrollBox.getChildren().add(boxWithEdit(i.get()));
                }
            }
        }


    }






}
