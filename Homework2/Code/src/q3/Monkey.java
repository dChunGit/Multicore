package q3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monkey {

    static Lock ropeLock = new ReentrantLock();
    static Condition ropeBusy = ropeLock.newCondition();
    static Condition ropeNotEmpty = ropeLock.newCondition();

    private static int crossing = 0;
    private static boolean Kong = false;
    private static int numMonkeys = 0;

    public Monkey() {

    }

    public void ClimbRope(int direction) throws InterruptedException {
        ropeLock.lock();
        if (direction == -1) {
            Kong = true;
            while (numMonkeys != 0) {
                ropeNotEmpty.await();
            }
            numMonkeys++;
            Kong = false;
        }
        else {
            while ((crossing != direction && numMonkeys != 0) || (crossing == direction && numMonkeys == 3) || Kong) {
                ropeBusy.await();
            }
            crossing = direction;
            numMonkeys++;
        }
        ropeLock.unlock();
    }

    public void LeaveRope() {
        ropeLock.lock();
        numMonkeys--;
        if (Kong) {
            if (numMonkeys == 0) {
                ropeNotEmpty.signal();
            }
        }
        else {
            ropeBusy.signalAll();
        }
        ropeLock.unlock();

    }

    /**
     * Returns the number of monkeys on the rope currently for test purpose.
     *
     * @return the number of monkeys on the rope
     *
     * Positive Test Cases:
     * case 1: when normal monkey (0 and 1) is on the rope, this value should <= 3, >= 0
     * case 2: when Kong is on the rope, this value should be 1
     */
    public int getNumMonkeysOnRope() {
        return numMonkeys;
    }

}
