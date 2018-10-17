package q5;

import java.util.concurrent.locks.ReentrantLock;

public class CoarseGrainedListSet implements ListSet {
    private Node head;

  public CoarseGrainedListSet() {
    head = new Node(null);
  }
  
  public synchronized boolean add(int value) {
    Node add = new Node(value);
    Node current = head;
    while (current.next != null && current.next.value < value) {
        current = current.next;
    }
    if (current.next == null) {
        current.next = add;
        return true;
    }
    else if (current.next.value == value) {
        return false;
    }
    else {
        add.next = current.next;
        current.next = add;
        return true;
    }
  }
  
  public synchronized boolean remove(int value) {
    Node previous = head;
    Node current = head.next;
    while (current != null) {
        if (current.value == value) {
            previous.next = current.next;
            return true;
        }
        previous = current;
        current = current.next;
    }
    return false;
  }
  
  public synchronized boolean contains(int value) {
    Node current = head.next;
    while (current != null) {
        if (current.value == value) {
            return true;
        }
        current = current.next;
    }
    return false;
  }
  
  protected class Node {
	  public Integer value;
	  public Node next;
		    
	  public Node(Integer x) {
		  value = x;
		  next = null;
	  }
  }

  /*
  return the string of list, if: 1 -> 2 -> 3, then return "1,2,3,"
  check simpleTest for more info
  */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Node current = head.next;
    while (current != null) {
        sb.append(current.value).append(",");
        current = current.next;
    }
    return sb.toString();
  }

  public void main (String args[]) {
      CoarseGrainedListSet list = new CoarseGrainedListSet();
      list.add(1);
      System.out.println(list.toString());

  }
}
