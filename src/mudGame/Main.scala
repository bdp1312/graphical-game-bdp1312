
package mudGame

import io.StdIn._
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.duration._
import scala.concurrent.Future
import java.io.PrintStream
import java.io.BufferedReader
import java.io.InputStreamReader


object Main extends App {
  println("My Mud Game")//if you can read this than git is working fine
 
  
  val system = ActorSystem("MudSystem")
  
  val roomManager = system.actorOf(Props[PlayerManager], "rm")
  val playerManager = system.actorOf(Props[RoomManager], "pm")
  
   playerManager ! PlayerManager.RoomManager(roomManager) 
  
  import system.dispatcher
  
  println("Schedual event")
  system.scheduler.schedule(0.seconds, 0.1.seconds, playerManager, PlayerManager.CheckForInput)
  
  println("Enter your name.")
  val playerName = readLine()
  var out = new PrintStream(System.out)
  var in = new BufferedReader(new InputStreamReader(System.in))
  PlayerManager.NewPlayer(playerName, out, in)
}