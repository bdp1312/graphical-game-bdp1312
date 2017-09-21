package mudGame


class Player(
    
    val name: String,
    //val health: Double,
    
    private var inventory: List[Item],
    /**
     * Thus far, my biggest holdup is finding proper way
     * to store my inventory data sets, am curantly considering 
     * Array, List, and Array Buffer
     */
    private var loc: Room) { //track location by int or room object
  /**
   * one of the most important parts of game
   * locations is a private varrible that holds an instance of room class
   * all functions of movement and moving items all require a call to 
   * player.location.funtion
   */
  
  
// watch out of single character hitting incorrect whole letter command
  private var _stillHere = true
  
  def stillHere = _stillHere
  
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
          loc.printDesc()
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
    val position = findItem(itemName, loc.loot)
     if (position != -1 && position <= loc.loot.length) {
      val prize = loc.loot(position)
      if (loc.removeItem(prize)){
        inventory = prize :: inventory
        listInventory()
        println(s"${prize.name} added.")
      } 
    
    } else {
      println(s"$itemName not found")
    }
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
 

 /*addItem
  * takes itemName: String
  * takes Item's position from findItem()
  * calls function to remove Item from Room inventory
  * adds Item to player inventory
  *  
  */
 
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

  
//  def printInventory(): Unit = {
//    inventory.foreach(_.printDesc())
//  }
  
  
  
////  /**
////   * prints whole inventory, includes names and descriptions
////   */
//  /**
//   * command handals all user input
//   * takes string
//   * splits string by spaces
//   * compares case keywords to the elements of input()
//   * to determine the proper funtion to call
//   */
// 
//
//  def player.addItem(itemName: String): item {
//  /**
//   * searches player.location.invenotry for Item with same name as aruguement
//   * if Item is found call location.removeItem, else invalid
//   * if lotion.removeItem returns Item add item to player inventory else invalid
//   * 
//   */
//    for each item in inventory
//      if itemName == Item.name 
//      
//        location.removeItem
//        return itemName
//      else 
//        return invalid command
//  }
//
//  def player.removeItem(Item); {
//  
//  }
  
  //			which item(S)?
//				specify byname
//				call currantRoom.removeItem(item)
//				  if .removeItem is false
//				    invalid command
//				  else
//				    
					    
					
  
//	 	  remove item form player inventory
//		    if player.RemoveItem is false
//		      invalid command
//		    else
//		       call currant_room.addItem(dropped item)
 def addAll(): Unit = {
    val itemNum = loc.loot.length
    for( i <- 0 until itemNum){
      val prize = loc.loot(i)
      if (!loc.removeItem(prize)){
        invalidComm()
      } else {
        inventory = prize :: inventory
      }
      
    }
  }


}
  
//object Player
///**
// * player constructor 
// * name = keyboard input
// * inventory is empty
// * location set to Inn Room
// */
//def apply() = new Player()
//  
//  name = keyboard input
//  //health = 100
//  inventory = nothing
//  location = Inn
// /**
//  * exit function
//  * lets player leave the game if location = the Inn Room 
//  */
//def exit() 
//  if player.location.name == "Inn"
//    terminate aplication 
//  else 
//    print invalid command

  
    /** 	Fuction adds all items from room inventory to player inventory
   *  -incompleate
   * 
   */





