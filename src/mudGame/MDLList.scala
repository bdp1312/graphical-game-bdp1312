package mudGame

class MDLList [A] {
  private class Node(var prev: Node, var value: A, var next: Node)
  private var default: A = _
  private val end = new Node(null, default, null)
  end.next= end
  end.prev = end
  private var size = 0
  
  def apply(i : Int): A = {
    if (i < 0 || i >= size) throw new IndexOutOfBoundsException(s"Index $i out of $size")
    var rover = end.next
    for(_ <- 0 until i) rover = rover.next
    rover.value
  }
  def update(i: Int, a: A): Unit= {
    if (i < 0 || i >= size) throw new IndexOutOfBoundsException(s"Index $i out of $size")
    var rover = end.next
    for(_ <- 0 until i) rover = rover.next
    rover.value = a
  }
  def insert(i: Int, a: A): Unit= {
    if (i < 0 || i > size) throw new IndexOutOfBoundsException(s"Index $i out of $size")
    var rover = end.next
    for(_ <- 0 until i) rover = rover.next
    val n = new Node(rover.prev, a, rover)
    rover.prev.next = n
    rover.prev = n
    size += 1
    }
  def remove(i: Int): A= {
    if (i < 0 || i >= size) throw new IndexOutOfBoundsException(s"Index $i out of $size")
    var rover = end.next
    for(_ <- 0 until i) rover = rover.next
    rover.value
    rover.prev.next = rover.next
    rover.next.prev = rover.prev
    size -= 1
    rover.value
  }
  //def remove(a: A): A
  def length: Int = size
  
  
  /**
   * Looks for a of type A in list
   * if a is not found returns -1
   */
  def find(a: A): Int = {
    var rover = end.next
    for (i <- 0 until size){
      if (a == rover.value) return i
      rover = rover.next
    }
    return -1
  }
  def map(f: (A) => B): MDLList[B] = {
    
  }

}