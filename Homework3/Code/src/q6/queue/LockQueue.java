package queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockQueue implements MyQueue {
    // you are free to add members
    private ReentrantLock enqueue, dequeue;
    private Condition available;
    private Node header = null;
    private Node tail = null;

    public LockQueue() {
      // implement your constructor here
        enqueue = new ReentrantLock();
        dequeue = new ReentrantLock();
        available = dequeue.newCondition();
    }

    public boolean enq(Integer value) {
        // implement your enq method here
        enqueue.lock();

        if(header == null) {
            header = new Node(value);
            tail = header;
        } else {
            Node addNode = new Node(value);
            tail.next = addNode;
            tail = addNode;
        }

        available.signalAll();
        enqueue.unlock();

        return true;
    }

    public Integer deq() {
        // implement your deq method here
        dequeue.lock();

        while(header == null) {
            try {
                available.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int value = header.value;
        header = header.next;

        dequeue.unlock();

        return value;
    }

    protected class Node {
        public Integer value;
        public Node next;

        public Node(Integer x) {
            value = x;
            next = null;
        }
    }
}
