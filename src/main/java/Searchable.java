import javafx.scene.Parent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public interface Searchable extends Serializable {

    Parent getInfoBox();
    Parent getSimpleInfoBox();

    <T> Parent getEditBox(T item, App app);
    String searchString();
    static <T extends Searchable> ArrayList<T> searchFor(Collection<? extends T> things, String query){
        ArrayList<HasOrder<T>> outOrder = new ArrayList<>();
        for(T i : things){
            String searchString = i.searchString().toLowerCase();
            int a = searchString.indexOf(query.toLowerCase());
            if(a != -1){
                outOrder.add(new HasOrder<>(i, a));
            }
        }
        Collections.sort(outOrder);
        ArrayList<T> out = new ArrayList<>();
        for(HasOrder<T> i : outOrder){
            out.add(i.get());
        }
        return out;
    }
}
