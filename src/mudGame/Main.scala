
package mudGame

import io.StdIn._
object Main extends App {
    println("My Mud Game")
    var input = ""
    val player = new Player("bill", Nil, Room.rooms("The Inn #0"))
    while (player.stillHere == true){
      println("Enter a command")
      input = readLine()
      player.command(input)
    

    
  }
}
 /**
  * main, in this order,
  * calls room constructors
  * 	for each room
  * 		calls item constructiors 
  * 		adds room to master array of rooms
  * calls player constructor  	
  *	calls player.command
  */


//create all instances of room
//open file, get number of rooms
//for each room
//  call room constructor,
//  add room to list of rooms
//close file
//
//create instance of player
//call player constructor
//}