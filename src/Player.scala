

class Player(
    val name: String,
    val health: Double = Nil 
    val inventory: List[Item] = Nil
    var location: Room
  
  def getFromInventory(itemName: String): Option[Item]{
    for each item in inventory
      if itemName == Item.name
        remove Item
        return itemName
      else 
         return ""
  }
  def addToInventory(item: Item): Unit{
    ???
    }
  def listInventory() 
    for each item in inventory
      print item name
  
}

object Player

def constructPlayer{
  name = keyboard input
  health = 100
  inventory = nothing
  location = defualt room (Inn)
  
  
  
}

def command(String){
  prompt user
  take string input
types of commands
	move
		move up
		  get currantroom.mapValue
		    if .mapValue is false
		      invalid
		    else
		      change player.location
		      curantRoom.
		move down
		move east
		move west
		move north
		move south
	inventory
		add item
			which item(S)?
				specify byname
				call currantRoom.removeItem(item)
				  if .removeItem is false
				    invalid command
				  else
				    player.addItem(item)
					    
					
		drop item
		  remove item form player inventory
		    if player.RemoveItem is false
		      invalid command
		    else
		       call currant_room.addItem(dropped item)
		  
		look at inventory
		  call player.printInventory
		look at room
		 call currantRoom.PrintRoomInfo
		exit
		
  
}

def exit(exitcommand) -> terminate aplication //use a whole game while loop
  if room.name == inn
      exit room
  else 
    print invalid command
def processCommand(command: String){
    println("Enter a command")
    val command = readLine
  }