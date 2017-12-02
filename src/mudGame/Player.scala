package mudGame

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream

import akka.actor.Actor
import akka.actor.ActorRef
import java.net.Socket


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
  private var stillHere: Boolean = true
  
 import Player._
  def receive = {
    case CheckForInput =>   
      if(in.ready()){
        val msg = in.readLine()
        command(msg.toString())        
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
    case Print(string) => out.println("Print\n" + string)
     
    case PlaceValue(room) =>
      println("Player.PlaceValue")
      /*if(actionChoice == 0)*/ 
      sender ! RoomManager.ChangePlayerLocation(room)
     // if(actionChoice == 1) room ! Room.PrintDesc 
  
    
    case m =>
      println("Oops! Bad message to:" + self.toString() + m)  }
  
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
  def command(comm: String): Unit = {
    var input = comm.split(" ")
    if (input.length == 1){
      input = Array( comm, " ", " ")
    }
    //input.foreach(println)
    if (input(0).contains("help")) {
      println("Controls:\nup, u = move up\n down, d = move down\n east, e = move east\n west, w = move west" +
          "north, n = move north\n south, s = move south")
    }
    else if (input(0).contains("up") || input(0) == ("u")){
      move(0)
    }
    else if (input(0).contains("down") || input(0) == ("d")){
      move(1)
    }
    else if (input(0).contains("east") || input(0) == ("e")){
      move(2)
    }
    else if (input(0).contains("west") || input(0) == ("w")) {
      move(3)
    }
    else if (input(0).contains("north") || input(0) == ("n")) {
      move(4)
    }
    else if (input(0).contains("south") || input(0) == ("s")) {
      move(5)
    }
    else if (input(0).contains("get") || input(0) == ("gi")) {
      getItem(input(1))                             
    }
    else if (input(0).contains("drop") || input(0) == ("di")) {
      dropItem(input(1))
    }
    else if (input(0).contains("look") || input(0).contains("l")) {
        if (input(1).contains("inventory") || input(1).contains("inv") || input(0) == ("li")) {
          listInventory()
        }
        else if (input(1).contains("room") || input(1).contains("r") || input(0) == ("lr")) {
          loc ! Room.PrintDesc
        }
        else if (input(1).contains("around") || input(1).contains("a") || input(0).contains("la")){
          loc ! Room.PrintExits
        }
        else{
          invalidComm()
        }
    }
    else if (input(0).contains("exit")) {
      exit()    
    }
    
    else if (input(0).contains("say")) {
      loc ! Room.SendMessage(input(1))
    }
    
    else if (input(0).contains("tell")) {
      context.parent ! PlayerManager.SendMessage(input(1))
    }
    
    else {
      invalidComm()
    }
  }
  

	def exit(): Unit = {
	 loc ! Room.DropPlayer(self)
	}

/**
 * move
 */
  def move(direction: Int): Unit = {
    loc ! Room.GetExit(direction) 
  } 
  /**
   * look out function
   * takes directional argument of Int
   * Sends directional argument to loc
   */
  def lookOut(direction: Int): Unit = {
    loc ! Room.GetExit(direction) 
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
  case class EnterRoom(room: ActorRef)
  /**
   * prints message 
   */
  case class Print(string: String)
  case class CheckMove(exitVal: String)
  case class PlaceValue(room: ActorRef)
}