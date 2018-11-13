public interface Lock {
    public void lock(Object lock, boolean saveStatics, Object ... others);
    public void unlock(Object unlock);
    public void abort(Object abort, Object ... others);
}
