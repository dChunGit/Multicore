package queue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class LockFreeQueue implements MyQueue {
    private AtomicStampedReference<Node> header, tail;
    private AtomicInteger count;

    public LockFreeQueue() {
        Node sentinel = new Node(-1);
        header = new AtomicStampedReference<>(sentinel, 0);
        tail = new AtomicStampedReference<>(sentinel, 0);
        count = new AtomicInteger(0);
    }

    public boolean enq(Integer value) {
        Node addNode = new Node(value);
        AtomicStampedReference<Node> pointer;

        while(true) {
            pointer = tail;
            AtomicStampedReference<Node> next = pointer.getReference().next;

            if(pointer == tail) {
                if(next.getReference() == null) {
                    if(tail.getReference().next.compareAndSet(next.getReference(), addNode,
                            next.getStamp(), next.getStamp()+1)) {
                        break;
                    }
                } else tail.compareAndSet(pointer.getReference(), next.getReference(),
                            pointer.getStamp(), pointer.getStamp() + 1);
            }
        }

        tail.compareAndSet(pointer.getReference(), addNode,
                pointer.getStamp(), pointer.getStamp() + 1);
        count.getAndIncrement();

        return true;
    }

    public Integer deq() {
        int value;

        while(true) {
            AtomicStampedReference<Node> head = header, pointer = tail, next = head.getReference().next;

            if(head == header) {
                if(head.getReference() == pointer.getReference()) {
                    if(next.getReference() == null) {
                        return null;
                    }

                    tail.compareAndSet(pointer.getReference(), next.getReference(),
                            pointer.getStamp(), pointer.getStamp() + 1);
                } else {
                    value = next.getReference().value;
                    if(header.compareAndSet(head.getReference(), next.getReference(),
                            head.getStamp(), head.getStamp() + 1)) {
                        break;
                    }
                }
            }
        }

        count.getAndDecrement();

        return value;
    }

    public int getCount() {
        return count.get();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node pointer = header.getReference().next.getReference();

        while(pointer != null) {
            sb.append(pointer.value).append(",");
            pointer = pointer.next.getReference();
        }

        return sb.toString();
    }

    protected class Node {
        public Integer value;
        public AtomicStampedReference<Node> next;


        public Node(Integer x) {
            value = x;
            next = new AtomicStampedReference<>(null, 0);
        }
    }
}
