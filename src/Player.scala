

class Player(
    val name: String,
    //val health: Double, 
    val inventory: List[Item] = Nil,
    /**
     * Thus far, my biggest holdup is finding proper way
     * to store my inventory data sets, am curantly considering 
     * Array, List, and Array Buffer
     */
    private var location: Room) {
  /**
   * one of the most important parts of game
   * locations is a private varrible that holds an instance of room class
   * all functions of movement and moving items all require a call to 
   * player.location.funtion
   */
  
  
  /**
   * getFromInventory takes String
   * searches player inventory for an item with matching name
   * if matching item is found return item. else invalid command
   */
  def getFromInventory(itemName: String): Option[Item]{

  }
  
  /**
   * 
   */
  def addItem(Item): Unit{
    inventory += 
    }
  /**
   * prints whole inventory, includes names and descriptions
   */
  def listInventory(){ 
    for (i <- 0 unitl inventory.length)
      println(inventory(i).name + ": " + inventory(i).effect)
  }    
  /**
   * command handals all user input
   * takes string
   * splits string by spaces
   * compares case keywords to the elements of input()
   * to determine the proper funtion to call
   */
 
  def command(String){
    println("Enter a command")
    val input = readLine()
    input.split(" ")
    (input(0)) match{
      case("move") => input(1) match{
                        case("up") => player.move(0)
                        case("down") => player.move(1)
                        case("east") => player.move(2)
                        case("west") => player.move(3)
                        case("north") => player.move(4)
                        case("south") => player.move(5)
                        }
      case("add") => player.addItem(input(1))
        
//			which item(S)?
//				specify byname
//				call currantRoom.removeItem(item)
//				  if .removeItem is false
//				    invalid command
//				  else
//				    
					    
					
       case("drop") => player.dropItem(input(1))
//	 	  remove item form player inventory
//		    if player.RemoveItem is false
//		      invalid command
//		    else
//		       call currant_room.addItem(dropped item)
		  
       case("look") => input(1) match{
                         case("inventory") => player.printInventory()
                         case("room") => player.location.printDscrp()
                         }         
       case("exit") => player.exit
       case(_) => invalid command
       } 
  }

def player.addItem(itemName: String): item {
  /**
   * searches player.location.invenotry for Item with same name as aruguement
   * if Item is found call location.removeItem, else invalid
   * if lotion.removeItem returns Item add item to player inventory else invalid
   * 
   */
  for each item in inventory
    if itemName == Item.name 
      
      location.removeItem
      return itemName
    else 
      return invalid command

def player.removeItem(Item); {
  
}
  
object Player
/**
 * player constructor 
 * name = keyboard input
 * inventory is empty
 * location set to Inn Room
 */
def constructPlayer{
  name = keyboard input
  //health = 100
  inventory = nothing
  location = defualt room (Inn)
 /**
  * exit function
  * lets player leave the game if location = the Inn Room 
  */
def exit() 
  if player.location.name == "Inn"
    terminate aplication 
  else 
    print invalid command

  
}




  }