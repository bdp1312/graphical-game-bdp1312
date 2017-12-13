package mudGame


import scala.collection.immutable._

import akka.actor.Actor
import akka.actor.ActorRef


class Room(
    //val keyword: String,
    val keyword: String,
    val name: String,
    val desc: String,
    private var loot: MDLList [Item], 
    private val exitNames: Array [String]
    ) extends Actor {
  
   
  private var presentPlayers = collection.mutable.Buffer[(String, ActorRef)]()
  //private var NPCsPresent = collection.mutable.Buffer[ActorRef]()
  
  override def preStart{
    println("Made room: "+name + desc +"\n" + loot.map(_.name).mkString("\n"))
  }
  
  def describe(): String = {
    var description: String = s"$name, $desc\n Players:\n"
    for(i <- 0 until presentPlayers.length){
      description = (description + presentPlayers(i)._1+"\n") 
    }
    description = (description + "Items\n"+ loot.map(_.name).mkString("\n"))
    return description
  }
  
  def sendMessage(msg: String): Unit = {
    presentPlayers.foreach((_)._2 ! Player.Print(msg))
  }
  
  private var exits: Array[Option[ActorRef]] = Array.empty
  
 import Room._
  
  /**
   * Takes actor messages 
   */
  def receive = {
    case LinkExits(rooms) =>
      exits = exitNames.map(rooms.get)
    
    case GetItem(itemName: String) =>
      println(loot.length)
      println(loot.map(_.name).mkString("\n"))
      println("itemName: " + itemName)
      if (loot.length == 0) {
        sender ! Player.NoSuchItem
      } else {     
        var found = false
        for (i <- 0 until loot.length) {
          if(found == false){
            println(i)
            if (itemName == loot(i).name.toString()){
              println("Found " + loot(i).name.toString())
              found = true
              val prize = loot(i)
              loot.remove(i)
              sender ! Player.AddItem(prize)
              sender ! Player.Print(s"${prize.name}: ${prize.desc}")
            }
          }
        }
        if (found == false) sender ! Player.NoSuchItem  
      }
    
    case DropItem(item) =>
      loot.add(item)
    
    case PrintDesc =>
      sender ! Player.Print(describe())
    
    case AddPlayer(name, player) =>
      sendMessage(s"$name is entering the room.")
      val mapRef = (name, player)
      presentPlayers += mapRef
      //val description = name + ", " + desc + "\n" + loot.map(_.name).mkString("\n")
      //println(description)
      player ! Player.Print(describe())
      //player ! Player.Print("Test")
      
    case DropPlayer(name, player) =>
      val mapRef = (name, player)
      presentPlayers -= mapRef
      sendMessage(s"$name has left the room.")
    case GetExit(playerName, direction) =>
      //println("GetExit")
      if (direction < 0 || direction > 5) sender ! Player.Print("There is nothing here.")
      else {
        val player =  sender
        val place = exitNames(direction)
        if( place != "-1"){
          context.parent ! RoomManager.SendPlayerRoom(name, player, place)
        }
        else sender ! Player.Print("There is nothing here.")
      }
    case PrintExits =>
      for(i <- 0 until exitNames.length){
        if (exitNames(i) != "-1") sender ! Player.Print((directions(i) + ": " + exitNames(i)))
      }
      
    case SendMessage(message) =>
      sendMessage(message)
      
    case AtemptAttack(target, speed, damage) =>
      val prosPlayer = presentPlayers.find( t => (t._1.toLowerCase.filter(_.isLetterOrDigit) == target.toLowerCase.filter(_.isLetterOrDigit)))
      if (prosPlayer == None) {
        sender ! PlayChar.NoSuchPlayer(target + " not in room.")
      }
      else{
        val target = prosPlayer.get 
        val msg: Any = (PlayChar.Hit(target._1, target._2, damage))
        Main.actManager ! ActivityManager.Schedual(context.sender, msg, speed)
      }
      
          
    /**
     * error message 
     */
    case m =>
      println(sender)
      println("Oops! Bad message to room: "+ m)
  }
} 
  

   


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
  case class AddPlayer(name: String, player: ActorRef)
  /**
   * Remove Player form playersInRoom
   */
  case class DropPlayer(name: String, player: ActorRef)
  /**
   * Takes exit value, enters it into exits list
   * Returns result to sender
   */
  case class GetExit(playerName: String, direction: Int)
  
  /**
   * Room returns all exit values != -1
   */
  case object PrintExits
  
  /**
   * Send message to each player in room
   */
  case class SendMessage(message: String)
  
  case class AtemptAttack(playerName: String, speed: Int, damage: Int)
  
  // More message types here
  
  /**
   * room apply method 
   */
  def apply(n: xml.Node): (String, () => Room) = {
    val keyword = (n \ "@keyword").text.trim
    println("making" + keyword)
    val name = (n \ "@name").text.trim
    val desc = (n \ "desc").text.trim
    val itemsList = (n \ "item").map(Item.apply).toList
    val items = new MDLList [Item]
    for (i <- itemsList) items.add(i)
    val NPCs = (n \ "NPC").map(NPC.collect).toList
    for (i <- 0 until NPCs.length){
      Main.npcManager ! NPCManager.NewNPC(NPCs(i), keyword)
    }
    val exits = (n \ "exits").text.split(",").map(_.trim)
    (keyword, () => new Room(keyword, name, desc,  items, exits))
    
   
  }
}  