import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class TagField extends VBox {
    public final Class<?> type;
    public ListView<Tag> selectorListView;
    public ListView<Tag> selectedListView;

    public final TagList selectedList;
    public final TagList selectorList;

    public TagField(ArrayList<Tag> tagList, TagList selectedList){
        this.type = selectedList.type;
        selectorList = new TagList(type, tagList);
        for(Tag tag : selectedList){
            selectorList.remove(tag);
        }

        this.selectedList = selectedList;

        HBox buttons = new HBox();
        buttons.getStylesheets().add("icongen.css");

        Button left = new Button("←");
        left.getStyleClass().add("icon");
        left.setOnAction(event -> {
            List<Tag> selectedItems = selectedListView.getSelectionModel().getSelectedItems();
            if(selectedItems.size() == 0){
                return;
            }
            for(Tag tag : selectedItems){
                selectedListView.getItems().remove(tag);
                selectorListView.getItems().add(tag);
                if(selectedItems.size() == 0){
                    return;
                }
            }
        });
        Button right = new Button("→");
        right.getStyleClass().add("icon");
        right.setOnAction(event -> {
            List<Tag> selectedItems = selectorListView.getSelectionModel().getSelectedItems();
            if(selectedItems.size() == 0){
                return;
            }
            for(Tag tag : selectedItems){
                selectorListView.getItems().remove(tag);
                selectedListView.getItems().add(tag);
                if(selectedItems.size() == 0){
                    return;
                }
            }
        });

        buttons.getChildren().add(left);
        buttons.getChildren().add(right);
        getChildren().add(buttons);

        HBox internal = new HBox();
        selectorListView = new ListView<>(FXCollections.observableList(selectorList));
        selectedListView = new ListView<>(FXCollections.observableList(selectedList));

        internal.getChildren().add(selectorListView);
        internal.getChildren().add(selectedListView);
        getChildren().add(internal);
    }


}
