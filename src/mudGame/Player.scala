package mudGame

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream

import scala.concurrent.duration._

import scala.util.matching._

import akka.actor.Actor
import akka.actor.ActorRef
import akka.util.Timeout.durationToTimeout

import java.net.Socket
import scala.util.Random


class Player(  
    val name: String,
    var out: PrintStream,
    var in: BufferedReader,
    var sock: Socket    
    
    ) extends Actor { 
  
//  override def  preStart = {
//    println(s"Player $name added.")
//    
//  }
  
  private var inventory = new MDLList[Item]()
  private var loc: ActorRef = null
  private var health: Double = 100
  //private var stillHere: Boolean = true
  private var equiped: Option[Item] = None
  private var fighting = false
  private var target: Option[ActorRef] = None
  val r = new Random(7)
  
 import Player._
 import PlayChar._
 
 private val fleeCom = """flee(.*)""".r
 private val attack = """kill (.*)""".r
 private val unequip = """unequip(.*)""".r
 private val equip = """equip (.*)""".r
 private val say = """say (.*)""".r
 private val tell = """tell (.*):(.*)""".r
 private val get = """get (.*)""".r
 private val drop = """drop (.*)""".r
 private val exitComm = """exit(.*)""".r
 private val lookAround =  """la( *)""".r
 private val lookRoom = """lr( *)""".r
 private val lookInventory = """li( *)""".r
 private val east = """e(.*)""".r
 private val west = """w(.*)""".r
 private val north = """n(.*)""".r
 private val south = """s(.*)""".r
 private val up = """u(.*)""".r
 private val down = """d(.*)""".r
 private val help = """help(.*)""".r
 
  def processComand(comm: String): Unit = {
     comm match{ 
       case fleeCom(_*) => flee()
       case attack(playerName) => attack(playerName)
       case exitComm(_*) => exit()
       case unequip() => unequiping()
       case equip(itemName) => equip(itemName)
       case say(msg) => loc ! Room.SendMessage(msg)
       case tell(playerName, msg) => context.parent ! PlayerManager.Tell(playerName, msg, name)
       case get(itemName) => getItem(itemName)
       case drop(itemName) => dropItem(itemName)
       case lookAround(_*) => loc ! Room.PrintExits
       case lookRoom(_*) => loc ! Room.PrintDesc
       case lookInventory(_*) => listInventory()
       case help(_*) => println("Controls:\nup, u = move up\n down, d = move down\n east, e = move east\n west, w = move west" +
          "north, n = move north\n south, s = move south\n")
       
       case up(_*) => move(0)
       case down(_*) => move(1)
       case east(_*) => move(2)
       case west(_*) => move(3)
       case north(_*) => move(4)
       case south(_*) => move(5)
       case _ => invalidComm()
     }
  }
  def flee(): Unit = {
    out.println("Atempting to flee.")
    val d = r.nextInt()
    loc ! Room.GetExit(name, d)
  }
  
  def attack(playerName: String): Unit = {
    if(fighting == false){
      fighting = true
      if (equiped == None){
        val damage = 5
        val speed = 5
        loc ! Room.AtemptAttack(playerName, speed, damage)
      } else {
        val weapon = equiped.get 
        val damage = weapon.damage
        val speed = weapon.speed
        loc ! Room.AtemptAttack(playerName, speed, damage)
      }    
    }
  }
  
  def unequiping(): Unit = equiped = None
  
  def equip(itemName: String): Unit = { //you may equip and unequip while attacking 
    var found = false
    for (i <- 0 until inventory.length){
      while(found == false){
        if(itemName == inventory(0).name){
          equiped = Some(inventory(i))
          out.println(inventory(i).name + " equiped.")
          found = true
        }
      }
    }
    if (found == false) out.println(itemName + " not found.")
  }

  def receive = {
    case CheckForInput =>   
      if(in.ready()){
        val msg = in.readLine()
        processComand(msg.toString())        
      }
    case AddItem(prize: Item) =>
      if (prize == null){
        println("Warning! Player.prize == null")
      }
      println(prize.name)
      inventory.add(prize)
      out.println(s"${prize.name} added.")
    case RemoveItem(item) => 
      val i = inventory.find(item)
      if (i == -1) out.println(item.name + "not in inventory")
      else{
        inventory.remove(i)
      }
    case NoSuchItem =>
        out.println("No such Item")
               
    case EnterRoom(room) => 
      println("Player.EnterRoom")
      loc = room
    /**
     * prints message 
     */
    case Print(string) => out.println(string)
     
    case PlaceValue(room) =>
      println("Player.PlaceValue")
      /*if(actionChoice == 0)*/ 
      sender ! RoomManager.ChangePlayerLocation(room)
     // if(actionChoice == 1) room ! Room.PrintDesc 
  
    case Hit(playerName, player, damage)=>      
      out.println(s"You hit $playerName for $damage damage.")
      player ! PlayChar.Damage(playerName, player, damage)
      fighting = false
      attack(playerName)
      
    case Damage(perpName, perp, amount) => 
      out.println(s"$perpName has hit you for $amount damage!")
      val newHealth = health - amount
      health = newHealth
      if(health <= 0){
        out.println("You have been killed")
        context.parent ! PlayerManager.SendMessage(s"$name had died.")
        exit()
      }
  
    case NoSuchPlayer(msg) =>
      out.println(msg)
      fighting = false
      
    case m =>
      println("Oops! Bad message to:" + self.toString() + m)  
  }
  
  def getItem(itemName: String): Unit = {
    loc ! Room.GetItem(itemName)
  }
  
  /**
   * Take Arguement from comm
   * find item in inventory
   * if item is found tells room to add item
   */
  def dropItem(itemName: String): Unit = {
    if (inventory.length == 0) {
        println("Error, your inventory is empty")
      } else {     
        var found = false
        for (i <- 0 until inventory.length) {
          if(found == false){
            println(i)
            if (itemName == inventory(i).name.toString()){
              println("Found " + inventory(i).name.toString())
              found = true
              val prize = inventory(i)
              inventory.remove(i)
              loc ! Room.DropItem(prize)
              sender ! Player.Print(s"${prize.name}: ${prize.desc}")
            }
          }
        }
        if (found == false) println("") 
      }    
  }



  /**
   * Takes String comm
   * calls function corresponding to the String entered
   */
  

	def exit(): Unit = {
	  for(i <- 0 until inventory.length){
      val prize = inventory(i)
      inventory.remove(i)
      loc ! Room.DropItem(prize)
    }
	  loc ! Room.DropPlayer(name, self)
	  context.parent ! PlayerManager.RemoveChild(self) 
	  context.stop(self)
	  sock.close()
	}

/**
 * move
 */
  def move(direction: Int): Unit = {
    loc ! Room.GetExit(name, direction) 
  } 
  /**
   * look out function
   * takes directional argument of Int
   * Sends directional argument to loc
   */
  def lookOut(direction: Int): Unit = {
    loc ! Room.GetExit(name, direction) 
  }  
      
        
 /**
   * getFromInventory takes String
   * searches player inventory for an item with matching name
   * if matching item is found return item. else invalid command
   */
  

  
 /**FindItem
  * takes input of item name
  * searches room invenotry for item
  * If a matching item is found, rertuns Items position in invenory list
  * Return must be checked as returns -1 if item not found
  */
 def findItem(itemName: String, cache: List[Item]): Int = {
   var prize = -1
   for ( i <- 0 until cache.length) {
      if (itemName == cache(i).name) {
         return i
      }         
   }
    return prize
 }
 
 /** invalidComm
  *  prints Invalid Command message
  */
 def invalidComm(): Unit = {
    out.println("Invalid Command. For full list of commands enter \"help\"")
  }   
 
 /**listInventory
  * calls Item print function for each Item in player inventory
  */
 def listInventory(): Unit = { 
   if (inventory == null) out.println("inventory is empty")
   else{
     for (i <- 0 until inventory.length) {
       out.println(s"${inventory(0).name}: ${inventory(0).desc}")
     }
   }
 } 

}
  
object Player {
  /**
   * checks in for input,
   * if there is input, sends it to command()
   */
  case object CheckForInput
  /**
   * adds Item to player inventory
   * not to be confused with getItem()
   */
  case class AddItem(item: Item)
  /**
   * removes item from player inventory
   */
  case class RemoveItem(item: Item)
  case object NoSuchItem
  
  /**
   * prints message 
   */
  case class Print(string: String)
  case class CheckMove(exitVal: String)
  case class PlaceValue(room: ActorRef)
  
  
}