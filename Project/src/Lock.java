public interface Lock {
    public void lock(Object lock);
    public void unlock(Object unlock);
    public void abort(Object abort);
}
