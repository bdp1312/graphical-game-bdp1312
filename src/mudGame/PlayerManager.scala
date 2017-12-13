package mudGame

import java.io.BufferedReader
import java.io.PrintStream
import java.net.Socket

import RoomManager._
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.pattern._
import akka.util.Timeout.durationToTimeout


class PlayerManager extends Actor {
  var rm: ActorRef = self
  var players = collection.mutable.Map[String, akka.actor.ActorRef]()
  import PlayerManager._
  def receive = {
    case NewPlayer(name, sock, ps, br) =>
      println("PlayerManager.NewPlayer")
      val lname = name.filter(_.isLetterOrDigit)
      if(context.child(lname).isEmpty){
        val NP = context.actorOf(Props(new Player(name, ps, br, sock)), lname)
        Main.roomManager ! SendPlayerRoom(name, NP, "TheInn0")
        val mapRef = (name, NP)
        players += (mapRef)
      }else {
        ps.println("This name is taken.")
        sock.close()
      }
    case SendMessage(msg) =>
      println(context.children.toString)
      context.children.foreach(_ ! Player.Print(msg))
    case CheckForInput =>
      context.children.foreach(_ ! Player.CheckForInput)
//    case StartNewPlayer(newPlayer) =>
//       main.RoomManager ! 
    case RoomManager(roomManager) =>
       rm = roomManager
       
    case RemoveChild(child) =>
      println("stoping " + child)
      context.stop(child)
      
    case Tell(playerName, msg, mesenger) =>
      if (players.contains(playerName)) players(playerName) ! Player.Print(mesenger + ": " + msg)
      else sender ! Player.Print(playerName + " not found.")
      
    case GetRef(playerName) =>
      val playerRef: Option[ActorRef] = { 
        if (players.contains(playerName)) {
          Some(players(playerName))
        }
        else None
      }
      sender ! (playerRef) 
  }

}

object PlayerManager {
  /**
   * Creates new instance of player
   */
  case class NewPlayer(name: String, sock: Socket, ps: PrintStream, br: BufferedReader)
  /**
   * sends message to every player
   */
  case class SendMessage(msg: String)
  /**
   * Tells all players to check for input
   */
  case object CheckForInput
  /**
   * gets roomManager actor ref
   */
  case class RoomManager(roomManager: ActorRef)
  
  /**
   * deletes actor
   */
  case class RemoveChild(child: ActorRef)
  
  /**
   * Takes playerName, returns ActorRef
   */
  case class Tell(playerName: String, msg: String, mesenger: String)
  
  case class GetRef(playerName: String)

}
