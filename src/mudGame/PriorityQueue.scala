package mudGame

class PriorityQueue[A] (higherP: (A, A) => Boolean)  {
  private var default: A = _
  private class Node(val data: A, var prev: Node, var next: Node)
  private val end = new Node(default, null, null)
  end.next =  end
  end.prev = end
  
  // O(n)
  def enqueue(a: A): Unit = {
    var rover = end.prev
    while (rover!=end && higherP(a, rover.data)) rover = rover.prev
    rover.next.prev = new Node(a, rover, rover.next)
    rover.next = rover.next.prev
  }
    
  
  /**
   * Removes item with the highest priority.
   */
  //O(1)
  def dequeue(): A = {
    val ret = end.next.data
     end.next.next.prev = end
     end.next =  end.next.next
     ret
  }
  
  def peek: A = end.next.data
  //O(1)
  def isEmpty = end.next == end
}