

case class Room (
    name: String,
    dscrp: String,
    loot: List [Item], //mutable list
    mapvals: Array [Int],
    neighbors: Array [String])
    {

 def addItem(Item:) Unit {
   add Item to list
}
 def removeItem
   remove Item from list
 def print info
   room description 
   print room inventory
     for each item in inventory
       print item.name
       print item.dscrp
   print all nonempty neighbors
     for 
  
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
    val dscrp = lines.next()
    val items = List.fill(lines.next.toInt) {
      val itm = lines.next.split(";") 
      //for each item call item constructor
      Item(itm(0). itm(1))
    }
  
}
