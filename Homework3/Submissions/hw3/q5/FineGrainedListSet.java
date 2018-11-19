package q5;

import java.util.concurrent.locks.ReentrantLock;

public class FineGrainedListSet implements ListSet {
  private Node head;

  public FineGrainedListSet() {
    head = new Node(null);
  }
	  
  public boolean add(int value) {
    Node add = new Node(value);
    Node previous = head;
    previous.lock.lock();
    Node current = head.next;
    if (current == null) { // first element added to the list
        previous.next = add;
        previous.lock.unlock();
        return true;
    }
    current.lock.lock();

    while (current.value < value) {
        previous.lock.unlock();
        previous = current;
        if (current.next == null) {
            current.next = add;
            previous.lock.unlock();
            return true;
        }
        current = current.next;
        current.lock.lock();
    }

    if (current.value == value) {
        previous.lock.unlock();
        current.lock.unlock();
        return false;
    }
    else {
        previous.next = add;
        add.next = current;
        previous.lock.unlock();
        current.lock.unlock();
        return true;
    }
  }
	  
  public boolean remove(int value) {
    Node previous = head;
    previous.lock.lock();
    Node current = head.next;
    if (current == null) {
        previous.lock.unlock();
        return false;
    }
    current.lock.lock();
    while (current.value <= value) {
        if (current.value == value) {
            previous.next = current.next;
            current.lock.unlock();
            previous.lock.unlock();
            return true;
        }
        previous.lock.unlock();
        previous = current;
        if (current.next == null) {
            current.lock.unlock();
            return false;
        }
        current = current.next;
        current.lock.lock();
    }
    return false;
  }
	  
  public boolean contains(int value) {
    Node previous = head;
    previous.lock.lock();
    Node current = head.next;
    if (current == null) {
        previous.lock.unlock();
        return false;
    }
    current.lock.lock();
    while (current.value <= value) {
        if (current.value == value) {
            current.lock.unlock();
            previous.lock.unlock();
            return true;
        }
        previous.lock.unlock();
        previous = current;
        if (current.next == null) {
            current.lock.unlock();
            return false;
        }
        current = current.next;
        current.lock.lock();
    }
    return false;
  }
	  
  protected class Node {
    public Integer value;
    public volatile Node next;
    public ReentrantLock lock;

  	public Node(Integer x) {
  		value = x;
  		next = null;
  		lock = new ReentrantLock();
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
}
