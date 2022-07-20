import java.util.ArrayList;
import java.util.Collection;


public class TagList extends ArrayList<Tag> {
    public final Class<?> type;

    public TagList(Class<?> type) {
        super();
        this.type = type;
    }

    public TagList(Class<?> type, ArrayList<Tag> tagList){
        this(type);
        addAll(tagList);
    }

    @Override
    public boolean add(Tag tag) {
        if(tag.isValidFor(type) && !contains(tag)){
            return super.add(tag);
        }
        return false;
    }

    @Override
    public void add(int index, Tag tag) {
        if(tag.isValidFor(type) && !contains(tag)){
            super.add(index, tag);
        }

    }


    @Override
    public boolean addAll(Collection<? extends Tag> c) {
        TagList toAdd = new TagList(type);
        for(Tag tag : c){
            toAdd.add(tag);
        }
        return super.addAll(toAdd);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Tag> c) {
        TagList toAdd = new TagList(type);
        for(Tag tag : c){
            toAdd.add(tag);
        }
        return super.addAll(index, toAdd);
    }

}
