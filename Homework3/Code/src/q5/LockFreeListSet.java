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
        Node current = (Node) head.reference.getReference();
        while (current != null && current.value < value) {
            previous = (Node) previous.reference.getReference();
            current = (Node) current.reference.getReference();
        }
        if (current == null || current.value > value) {
            add.next = previous.next;
            if (previous.reference.compareAndSet(current, add, false, false)) {
                return true;
            }
        }
        else if (current.value == value) {
            return false;
        }
    }
  }
	  
  public boolean remove(int value) {
      while (true) {
          Node previous = head;
          Node current = (Node) head.reference.getReference();
          while (current != null && current.value < value) {
              previous = (Node) previous.reference.getReference();
              current = (Node) current.reference.getReference();
          }
          if (current == null || current.value > value) {
              return false;
          }
          else if (current.value == value) {
              if (current.reference.attemptMark(current.next, true)) {
                  if (previous.reference.compareAndSet(current, current.next, false, false)) {
                      return true;
                  }
              }
          }
      }
  }
	  
  public boolean contains(int value) {
      while (true) {
          Node current = (Node) head.reference.getReference();
          while (current != null && current.value < value) {
              current = (Node) current.reference.getReference();
          }
          if (current.value == value && !current.reference.isMarked()) {
              return true;
          }

          else if (current.value > value) {
              return false;
          }
      }
  }
	  
  protected class Node {
  	public Integer value;
  	private Node next;
  	public AtomicMarkableReference reference;
  	public Node(Integer x) {
  	  value = x;
  	  next = null;
  	  reference = new AtomicMarkableReference(this.next, false);
  	}
  }

  /*
  return the string of list, if: 1 -> 2 -> 3, then return "1,2,3,"
  check simpleTest for more info
  */
  public String toString() {
      StringBuilder sb = new StringBuilder();
      Node current = (Node) head.reference.getReference();
      while (current != null) {
          sb.append(current.value).append(",");
          current = (Node) current.reference.getReference();
      }
      return sb.toString();
  }
}
