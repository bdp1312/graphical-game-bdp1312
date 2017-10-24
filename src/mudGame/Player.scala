package mudGame

import akka.actor.Actor
import akka.actor.ActorRef
import java.io.PrintStream
import java.io.BufferedReader

class Player(  
    val name: String,
    //private var health: Double,
    private var inventory: List[Item],
    private var loc: ActorRef,
    out: PrintStream,
    in: BufferedReader) extends Actor { //change to an actor ref
  
  println(s"Player $name added.")
   
  private var _stillHere = true
  
  def stillHere = _stillHere
  import Player._
  def receive = {
    case CheckForInput =>
      if(in.ready()){
        val msg = in.readLine()
        command(msg.toString())        
      }
    case AddItem(itemName: String) =>
      loc ! Room.GetItem(itemName)
      case NoSuchItem =>
        println("No such Item")
      case prize: Item =>
        inventory = prize :: inventory
        println(s"${prize.name} added.")        
    case EnterRoom(room) => loc = room
      //Set loc to room arguement
    case PrintItemDesc(item) =>  item.printDesc()
    
  }
  
  
  def command(comm: String): Unit = {
    var input = comm.split(" ")
    if (input.length == 1){
      input = Array( comm, " ", " ")
    }
    input.foreach(println)
    if (input(0).contains("help")) {
      println("Controls:")
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
    else if (input(0).contains("add") || input(0) == ("ai")) {
      addItem(input(1))                             
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
        
        else if (input(1).contains("up") || input(1) == ("u") || input(0) == ("lu")) {
          lookOut(0)
        }
         else if (input(1).contains("down") || input(1) == ("d") || input(0) == ("ld")) {
          lookOut(1)
        }        
        else if (input(1).contains("east") || input(1) == ("e") || input(0) == ("le")) {
          lookOut(2)
        }
        else if (input(1).contains("west") || input(1) == ("w") || input(0) == ("lw")) {
          lookOut(3)
        }
        else if (input(1).contains("north") || input(1) == ("n") || input(0) == ("ln")) {
          lookOut(4)
        }
        else if (input(1).contains("south") || input(1) == ("s") || input(0) == ("ls")) {
          lookOut(5)
        }
        
        else {
          invalidComm()
        }
    }
    else if (input(0).contains("exit")) {
      exit()    
    }
    else{
      invalidComm()
    }
  }
  

	def exit(): Unit = {
	  if (loc.name == "The Inn #0"){
	    println("Exitning game!")
	    _stillHere = false        
    }else{
        println("No Leave")
    }
	}

/**
 * move
 */
  def move(direction: Int): Unit = {
    if (loc.mapvals(direction) == "-1") {
     println("Nothing this way") 
    }
    else{
      val newLoc = Room.rooms(loc.mapvals(direction))
      loc = newLoc
      loc.printDesc()
    }
  }
  
  /**
   * look out function
   * takes directional argument of Int
   * calls Room.rooms(loc.mapvals(direction)).printDesc()
   */
  def lookOut(direction: Int): Unit = {
    if (loc.mapvals(direction) == "-1") {
     println("Nothing this way") 
    }
    else{
      println(loc.mapvals(direction))
    }
  }  
      
        
 /**
   * getFromInventory takes String
   * searches player inventory for an item with matching name
   * if matching item is found return item. else invalid command
   */
  def dropItem(itemName: String): Unit = {
    val position = findItem(itemName, inventory)
    println(position)
    if (position != -1 && position <= inventory.length) {
      val junk = inventory(position)
      if (loc.addItem(junk)){
        val newInventory = inventory.take(position) ++ inventory.drop(position+1)
        inventory = newInventory
        println(s"$junk dropped.")
      } else {
        println("You cannot drop this item here!")
      }
    }else {
      println(s"$itemName not found.")
    }    
  }
  
  def addItem(itemName: String): Unit = {

  }

  
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
    println("Invalid Command. For full list of commands enter \"help\"")
  }   
 
 /**listInventory
  * calls Item print function for each Item in player inventory
  */
 def listInventory(): Unit = { 
   for (i <- 0 until inventory.length) {
     println(s"${inventory(0).name}: ${inventory(0).effect}")
   }
 } 

}
  
object Player {
     case object CheckForInput
     case class AddItem(itemName: String)
     case object NoSuchItem
     case class PrintItemDesc(item: Item)
 
}