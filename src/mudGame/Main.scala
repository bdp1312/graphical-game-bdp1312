
package mudGame

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream


import scala.concurrent.Future
import scala.concurrent.duration.DurationDouble
import scala.concurrent.duration.DurationInt

import akka.actor.ActorSystem
import akka.actor.Props
import java.net.ServerSocket


object Main extends App {
  println("My Mud Game")//if you can read this than git is working fine
 
  
  val system = ActorSystem("MudSystem")
  
  val roomManager = system.actorOf(Props[RoomManager], "rm")
  val playerManager = system.actorOf(Props[PlayerManager], "pm")
  
  
  import system.dispatcher
  
  println("Schedual event")
  system.scheduler.schedule(0.seconds, 0.1.seconds, playerManager, PlayerManager.CheckForInput)
 
  val ss = new ServerSocket(12345)
  while(true) {
    val sock = ss.accept()
    var out = new PrintStream(sock.getOutputStream)
    var in = new BufferedReader(new InputStreamReader(sock.getInputStream))
    out.println("enter your name")
    Future {
      val playerName = in.readLine()
      println("Welcome" + playerName)
//      while(playerName == ""){
//        playerName = in.readLine()
//      }
      playerManager ! PlayerManager.NewPlayer(playerName, sock, out, in) 
    }
 
  }
}