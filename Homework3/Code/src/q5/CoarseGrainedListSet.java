package q5;

import java.util.concurrent.locks.ReentrantLock;

public class CoarseGrainedListSet implements ListSet {
    private Node head;
    private static ReentrantLock lock;
	
  public CoarseGrainedListSet() {
    head = new Node(null);
    lock = new ReentrantLock();
  }
  
  public boolean add(int value) {
    lock.lock();
    Node add = new Node(value);
    Node current = head;
    while (current.next != null && current.next.value < value) {
        current = current.next;
    }
    if (current.next == null) {
        current.next = add;
        lock.unlock();
        return true;
    }
    else if (current.next.value == value) {
        lock.unlock();
        return false;
    }
    else {
        add.next = current.next;
        current.next = add;
        lock.unlock();
        return true;
    }
  }
  
  public boolean remove(int value) {
    lock.lock();
    Node previous = head;
    Node current = head.next;
    while (current != null) {
        if (current.value == value) {
            previous.next = current.next;
            lock.unlock();
            return true;
        }
        previous = current;
        current = current.next;
    }
    lock.unlock();
    return false;
  }
  
  public boolean contains(int value) {
	lock.lock();
    Node current = head.next;
    while (current != null) {
        if (current.value == value) {
            lock.unlock();
            return true;
        }
        current = current.next;
    }
    lock.unlock();
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
