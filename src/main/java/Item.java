
public abstract class Item implements Searchable {
    public static final Item NO_ITEM = new Account(Service.NO_SERVICE, "","NO NAME", "", "");
    public final TagList tags = new TagList(this.getClass());
}
