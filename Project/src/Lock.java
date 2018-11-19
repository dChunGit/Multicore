public interface Lock {
    void lock(Object lock, boolean saveStatics, Object ... others);
    void unlock(Object unlock);
    void abort(Object abort, Object ... others);
}
