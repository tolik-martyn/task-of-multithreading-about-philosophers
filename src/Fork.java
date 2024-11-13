public class Fork {

    private static int counter = 0;

    private volatile boolean using;
    private final int id;

    public Fork(boolean using) {
        this.using = using;
        this.id = ++counter;
    }

    public Fork() {
        this(false);
    }

    public boolean isUsing() {
        return using;
    }

    public void setUsing(boolean using) {
        this.using = using;
    }

    public int getId() {
        return id;
    }
}
