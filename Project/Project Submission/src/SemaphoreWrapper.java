import java.util.concurrent.Semaphore;

public class SemaphoreWrapper implements Lock{
    private Semaphore semaphore;

    public SemaphoreWrapper(int permits) {
        semaphore = new Semaphore(permits);
    }


    @Override
    public void lock() {
        try {
            semaphore.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlock() {
        try {
            semaphore.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
