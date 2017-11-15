package mudGame

import akka.actor.Actor
import akka.actor.ActorRef
import collection.immutable.Map
import collection.immutable.Map

class Room(
    //val keyword: String,
    val keyword: String,
    val name: String,
    val desc: String,
    private var _loot: List [Item], //change to Set type
    private val exitNames: Array [String]) extends Actor {
  
  private var playersInRoom = collection.mutable.Buffer[ActorRef]()
  
  def loot = _loot
  
  /**
   * Prints Room Description
   */


  println("Made room: "+name)
  
  private var exits: Array[Option[ActorRef]] = Array.empty
  
  import Room._
  
  /**
   * Takes actor messages 
   */
  def receive = {
    case LinkExits(rooms) =>
      exits = exitNames.map(rooms.get)
    
    case GetItem(itemName: String) =>
      println(itemName)
      if (loot.isEmpty) {
        sender ! Player.NoSuchItem
      } else {
        for (i <- 0 until loot.length) {
          if (itemName == loot(i).name){
            val prize = loot(i)
            val newLoot = loot.take(i) ++ loot.drop(i+1)
          _loot = newLoot
            sender ! Player.AddItem(prize)
            sender ! Player.Print(s"${prize.name}: ${prize.effect}")
          }
          if (loot(loot.length).name != itemName)
            sender ! Player.NoSuchItem
          }
      }
    
    case DropItem(item) =>
      item :: _loot
      sender ! Player.RemoveItem(item)
    
    case PrintDesc =>
      val description = s"$name, $desc\n loot.foreach(_.name\n)"
      sender ! Player.Print(description)
    
    case AddPlayer(player) =>
      playersInRoom += player
    case DropPlayer(player) =>
      playersInRoom -= player
    case GetExit(direction) =>
      println("GetExit")
      val player =  sender
      val place = exitNames(direction)
      if( place != "-1"){
        context.parent ! RoomManager.SendPlayerRoom(player, place)
      }
      else sender ! Player.Print("There is nothing here.")
    case PrintExits =>
      val directionalExits = Map(directions -> exitNames)
      directionalExits.filter((t) => t._2.contains("-1"))
      for (tup <- directionalExits) sender ! Player.Print(s"${tup._1}: ${tup._2}")
      
          
    /**
     * error message 
     */
    case m =>
      println(sender)
      println("Oops! Bad message to room: "+ m)
  }
} 
  
//Remove item from list
   


/**
 * companion object to room
 */
object Room {
  
  val directions = Array[String]("Up", "Down", "East", "West", "North", "South")
  
  case class LinkExits(rooms: Map[String, ActorRef])
  /**
   * remove Item from room inventory
   */
  case class GetItem(itemName: String)
  /**
   * add item to room inventory
   */ 
  case class DropItem(item: Item)
  /**
   * Takes actor message, calls printDesc
   */
  case object PrintDesc
  /**
   * Takes player, Adds player to playersInRoom
   */  
  case class AddPlayer(player: ActorRef)
  /**
   * Remove Player form playersInRoom
   */
  case class DropPlayer(player: ActorRef)
  /**
   * Takes exit value, enters it into exits list
   * Returns result to sender
   */
  case class GetExit(direction: Int)
  
  /**
   * Room returns all exit values != -1
   */
  case object PrintExits
  
  // More message types here
  
  /**
   * room apply method 
   */
  def apply(n: xml.Node): (String, () => Room) = {
    val keyword = (n \ "@keyword").text.trim
    println("making" + keyword)
    val name = (n \ "@name").text.trim
    val desc = (n \ "desc").text.trim
    val items = (n \ "item").map(Item.apply).toList
    val exits = (n \ "exits").text.split(",").map(_.trim)
    (keyword, () => new Room(keyword, name, desc, items, exits))
    
   
  }
}  