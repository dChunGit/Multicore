package queue;

import java.util.concurrent.locks.ReentrantLock;

public class LockQueue implements MyQueue {
    // you are free to add members
    private ReentrantLock enqueue, dequeue;
    private Node header;
    private Node tail;

    public LockQueue() {
      // implement your constructor here
        enqueue = new ReentrantLock();
        dequeue = new ReentrantLock();

        header = new Node(-1);
        header.next = null;
        tail = header;
    }

    public boolean enq(Integer value) {
        // implement your enq method here
        Node addNode = new Node(value);
        addNode.next = null;

        enqueue.lock();
        tail.next = addNode;
        tail = addNode;
        enqueue.unlock();

        return true;
    }

    public Integer deq() {
        // implement your deq method here
        dequeue.lock();
        Node pointer = header;
        pointer = pointer.next;

        if(pointer == null) {
            dequeue.unlock();
            return -1;
        }

        int value = pointer.value;
        header = pointer;

        dequeue.unlock();

        return value;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node pointer = header.next;
        while(pointer != null) {
            sb.append(pointer.value);
            if (pointer.next != null) {
                sb.append(", ");
            }
            pointer = pointer.next;
        }

        return sb.toString();
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
