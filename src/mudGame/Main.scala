
package mudGame

import io.StdIn._
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.duration._
import scala.concurrent.Future


object Main extends App {
  println("My Mud Game")
  
  val system = ActorSystem("MudSystem")
  
  val roomManager = system.actorOf(Props[PlayerManager], "rm")
  val playerManager = system.actorOf(Props[RoomManager], "pm")
  import system.dispatcher
  
  println("Schedual event")
  system.scheduler.schedule(0.seconds, 0.1.seconds, playerManager, PlayerManager.CheckForInput)
  
/* val system = ActorSystem("Mud")
 * implicit val ec = system.dispatcher
 * 
*/
  
  
//  var input = ""
//  val player = new Player("bill", Nil, Room.rooms("The Inn #0"))
//  while (player.stillHere == true){
//    println("Enter a command")
//    input = readLine()
//    player.command(input)    
//  }
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