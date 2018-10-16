package queue;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue implements MyQueue {
    // you are free to add members
    private AtomicReference<Node> header, tail;

    public LockFreeQueue() {
        // implement your constructor here
        Node sentinel = new Node(-1);
        header = new AtomicReference<>(sentinel);
        tail = header;
        int a = 0;
    }

    public boolean enq(Integer value) {
        // implement your enq method here
        Node addNode = new Node(value);

        while(true) {
            AtomicReference<Node> pointer = tail;
            AtomicReference<Node> next = pointer.get().next;
            if (pointer == tail) {
                if (next == null) {
                    if (tail.get().next.compareAndSet(null, addNode)) {
                        break;
                    }
                } else {
                    tail.compareAndSet(tail.get(), next.get());
                }
            }
        }
        tail.compareAndSet(tail.get(), addNode);
        return true;
    }

    public Integer deq() {
        // implement your deq method here
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node pointer = header.get();
        while(pointer != null) {
            sb.append(pointer.value);
            if (pointer.next != null) {
                sb.append(", ");
            }
            pointer = pointer.next.get();
        }

        return sb.toString();
    }

    protected class Node {
        public Integer value;
//        public Node next;
        public AtomicReference<Node> next;

        public Node(Integer x) {
            value = x;
            next = new AtomicReference<>(null);
        }
    }
}
