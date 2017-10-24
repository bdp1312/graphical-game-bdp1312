
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
  PlayerManager.makePlayer
}