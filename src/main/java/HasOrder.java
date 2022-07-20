public class HasOrder<T> implements Comparable<HasOrder<?>> {
    private final T thing;
    private final int order;
    public HasOrder(T thing, int order){
        this.thing = thing;
        this.order = order;
    }

    public T get(){
        return thing;
    }
    @Override
    public int compareTo(HasOrder<?> o) {
        return order - o.order;
    }
}
