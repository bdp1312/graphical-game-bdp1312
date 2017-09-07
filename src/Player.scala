

class Player(
    val name: String,
    //val health: Double, 
    val inventory: List[Item] = Nil,
    private var location: Room) {
  
  def getFromInventory(itemName: String): Option[Item]{
    for each item in inventory
      if itemName == Item.name
        remove Item
        return itemName
      else 
         return ""
  }
  def addItem(Item): Unit{
    ???
    }
  
  def listInventory(){ 
    for (i <- 0 unitl inventory.length)
      println(inventory(i).name + ": " + inventory(i).effect)
  }    
  
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

def player.addItem(Item): {
  
}

def player.removeItem(Item); {
  
}
  
object Player

def constructPlayer{
  name = keyboard input
  //health = 100
  inventory = nothing
  location = defualt room (Inn)
  
def exit() 
  if player.location.name == "Inn"
    terminate aplication 
  else 
    print invalid command

  
}




  }