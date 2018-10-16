package queue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue implements MyQueue {
    private AtomicReference<Node> header, tail;
    private AtomicInteger count;

    public LockFreeQueue() {
        Node sentinel = new Node(-1);
        header = new AtomicReference<>(sentinel);
        tail = new AtomicReference<>(sentinel);
        count = new AtomicInteger(0);
    }

    public boolean enq(Integer value) {
        Node addNode = new Node(value);
        Node pointer;

        while(true) {
            pointer = tail.get();
            Node next = pointer.next.get();
            if(pointer == tail.get()) {
                if(next == null) {
                    if(tail.get().next.compareAndSet(next, addNode)) {
                        break;
                    }
                } else {
                    tail.compareAndSet(pointer, next);
                }
            }
        }
        tail.compareAndSet(pointer, addNode);
        count.getAndIncrement();
        return true;
    }

    public Integer deq() {
        int value;

        while(true) {
            Node head = header.get();
            Node pointer = tail.get();
            Node next = head.next.get();
            if(head == header.get()) {
                if(head == pointer) {
                    if(next == null) {
                        return null;
                    }
                    tail.compareAndSet(pointer, next);
                } else {
                    value = next.value;
                    if(header.compareAndSet(head, next)) {
                        break;
                    }
                }
            }
        }
        count.getAndDecrement();
        return value;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node pointer = header.get().next.get();
        while(pointer != null) {
            sb.append(pointer.value);
            if (pointer.next.get() != null) {
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
//            next = null;
            next = new AtomicReference<>(null);
        }
    }
}
