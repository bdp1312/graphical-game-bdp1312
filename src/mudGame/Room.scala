package mudGame

import collection.immutable.Map
import collection.immutable.Map

class Room(
    val name: String,
    val desc: String,
    private var _loot: List [Item], //change to Set type
    private val _mapvals: Array [String]) {
  
  def mapvals = _mapvals
  def loot = _loot
  
  def addItem(item: Item): Boolean = {
    _loot =  item :: _loot
    return true
   }
    
  
   //add Item to list
   
 
 
 /**
    * remove Item from room inventory
    */
  def removeItem(item: Item): Boolean = {
    println(item)
    if (loot.isEmpty) {
      return false
    } else {
      for (i <- 0 until loot.length) {
        if (item == loot(i)){
          val newLoot = loot.take(i) ++ loot.drop(i+1)
          _loot = newLoot
          printDesc()
          return true
        }
      }
      
      return false
    }
  }
   //Remove item from list
   
 
   
  def printDesc(): Unit = {
   println(desc)
   for (i <-0 until loot.length)
     loot(i).printDesc()
   }
 /**
  * print room description
  */
// def print info {
//   room description 
//   print room inventory
//     for each item in inventory
//       print item.name
//       print item.dscrp
//   print all nonempty neighbors
//     for 

  
/**
 * takes room input from file
 */
}
/*
object Room {
//  val Map: List[Room] = Nil
  val directions = Array("North", "South", "East", "West", "Up", "Down")
  directions.foreach(println)

  val rooms = readRoomsFromFile()
  
   def readRoomsFromFile(): Array[Room] = {
     val source = io.Source.fromFile("rooms.txt")
     val lines = source.getLines()
     val r = Array.fill(lines.next.trim.toInt)(readRoom(lines))
     source.close
     r  
   }
  def readRoom(lines:Iterator[String]): Room = {
    val name = lines.next()
    val desc = lines.next()
    val loot = List.fill(lines.next.trim.toInt) {
       val itm = lines.next.split(";")
       new Item(itm(0), itm(1))
     }             
    val mapvals = lines.next.split(",").map(_.trim.toInt)
    new Room( name, desc, loot, mapvals)
  }  
}
*/

object Room {
//  val Map: List[Room] = Nil
  val directions = Array("North", "South", "East", "West", "Up", "Down")
  directions.foreach(println)

  val rooms = readRoomsFromFile()
  
   def readRoomsFromFile():Map[String, Room] = {
     val source = io.Source.fromFile("rooms.txt")
     val lines = source.getLines()
     val r = Array.fill(lines.next.trim.toInt)(readRoom(lines))
     source.close
     var roomTup = Array[(String, Room)]()
     println(r.length)
     for ( i <- 0 until r.length){
       roomTup = roomTup :+ (r(i).name, r(i))
       //roomTup
     }
     return roomTup.toMap
   }
  def readRoom(lines:Iterator[String]): Room = {
    val name = lines.next()
    val desc = lines.next()
    val loot = List.fill(lines.next.trim.toInt) {
       val itm = lines.next.split(";")
       new Item(itm(0), itm(1))
     }             
    val mapvals = lines.next.split(",").map(_.trim)
    new Room( name, desc, loot, mapvals)
  }  
}