

class Room {
  
}

object Room {
  val rooms = readRoomsFromFile()
  
  def readRoomsFromFile():Array[Room] = {
   val source = io.Source.fromFile("rooms.txt")
   val lines = source.getLines()
   val r = Array.fill(lines.next.toInt)(readRoom(lines))
   source.close
   r
  }
  
  def readRoom(lines:Iterator[String}): Room = {
    val name = lines.next()
    val desc = lines.next()
    val items = List.fill(lines.next.toInt) {
      val itm = lines.next.split(";")
      Item(itm(0). itm(1))
    }
  
}
