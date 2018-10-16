package stack;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack implements MyStack {
    AtomicReference<Node> top;

    public LockFreeStack() {
        // implement your constructor here
        top = new AtomicReference<>(null);
    }

    public boolean push(Integer value) {
        // implement your push method here
        Node addNode = new Node(value);
        while (true) {
          Node pointer = top.get();
          addNode.next = pointer;
          if (top.compareAndSet(pointer, addNode)) {
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
              return value;
          }
          Thread.yield();
        }
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
