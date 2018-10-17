package q5;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeListSet implements ListSet {

    private Node head;
	
    public LockFreeListSet() {
        head = new Node(null);
    }

    public boolean add(int value) {
        Node add = new Node(value);

        while (true) {
            Node previous = head;
            Node current = head.next.getReference();

            while (current != null && current.value < value) {
                previous = current;
                current = current.next.getReference();
            }

            if (current == null || current.value > value) {
                if (previous.next.compareAndSet(current, add, false, false)) {
                    add.next.set(current, false);
                    return true;
                }
                // if cas fails, loop will try again
            } else return false;
        }
    }

    public boolean remove(int value) {
        while (true) {
            Node previous = head;
            Node current = head.next.getReference();

            while (current != null && current.value < value) {
                previous = current;
                current = current.next.getReference();
            }

            if (current == null || current.value > value) {
                return false;
            } else if (current.next.attemptMark(current.next.getReference(), true)) {
                if (previous.next.compareAndSet(current, current.next.getReference(), false, false)) {
                    return true;
                }
            }
        }
    }

    public boolean contains(int value) {
        while (true) {
            Node current = head.next.getReference();

            while (current != null && current.value < value) {
                current = current.next.getReference();
            }

            if (current != null) {
                if (current.value == value && !current.next.isMarked()) {
                    return true;
                } else if (current.value > value) {
                    return false;
                }
            } else return false;
        }
    }

    protected class Node {
        public Integer value;
//        private Node next;
        public AtomicMarkableReference<Node> next;

        public Node(Integer x) {
            value = x;
//            next = null;
            next = new AtomicMarkableReference<>(null, false);
        }
    }

    /*
    return the string of list, if: 1 -> 2 -> 3, then return "1,2,3,"
    check simpleTest for more info
    */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = head.next.getReference();
        while (current != null) {
            sb.append(current.value).append(",");
            current = current.next.getReference();
        }
        return sb.toString();
    }
}
