package stack;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack implements MyStack {
    AtomicReference<Node> top;
    AtomicInteger count;

    public LockFreeStack() {
        // implement your constructor here
        top = new AtomicReference<>(null);
        count = new AtomicInteger(0);
    }

    public boolean push(Integer value) {
        // implement your push method here
        Node addNode = new Node(value);
        while (true) {
          Node pointer = top.get();
          addNode.next = pointer;
          if (top.compareAndSet(pointer, addNode)) {
              count.getAndIncrement();
              return true;
          }
          Thread.yield();
        }
    }

    public Integer pop() throws EmptyStack {
        // implement your pop method here
        while (true) {
          Node pointer = top.get();
          if (pointer == null) throw new EmptyStack();
          int value = pointer.value;
          Node next = pointer.next;
          if (top.compareAndSet(pointer, next)) {
              count.getAndDecrement();
              return value;
          }
          Thread.yield();
        }
    }

    public int getCount() {
        return count.get();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node pointer = top.get();
        while (pointer != null) {
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
