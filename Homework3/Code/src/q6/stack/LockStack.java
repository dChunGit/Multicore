package stack;

import queue.LockQueue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class LockStack implements MyStack {
    private ReentrantLock popLock, pushLock;
    private Node header;
    private AtomicInteger count;

    public LockStack() {
        popLock = new ReentrantLock();
        pushLock = new ReentrantLock();
        count = new AtomicInteger(0);

        header = new Node(-1);
    }

    public boolean push(Integer value) {
        // implement your push method here
        Node node = new Node(value);

        pushLock.lock();
        node.next = header.next;
        header.next = node;
        count.getAndIncrement();
        pushLock.unlock();

        return true;
    }

    public Integer pop() throws EmptyStack {
        popLock.lock();
        Node pointer = header.next;

        if(pointer == null) {
            throw new EmptyStack();
        }
        int value = pointer.value;
        header = header.next;
        count.getAndDecrement();

        popLock.unlock();

        return value;
    }

    public int getCount() {
        return count.get();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node pointer = header.next;

        while(pointer != null) {
            sb.append(pointer.value).append(",");
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
