package mudGame

import java.io.BufferedReader
import java.io.PrintStream

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props

import RoomManager._


class PlayerManager extends Actor {
  var rm: ActorRef = self
  var players: Map[String, Player] = null
  import PlayerManager._
  def receive = {
    case NewPlayer(name/*, sock*/, ps, br) =>
      println("PlayerManager.NewPlayer")
      val lname = name.filter(_.isLetterOrDigit)
      if(context.child(lname).isEmpty){
        val NP = context.actorOf(Props(new Player(name/*, sock*/, ps, br)), lname)
        Main.roomManager ! SendPlayerRoom(NP, "TheInn0")  
      }else {
        ps.println("This name is taken.")
        //sock.close()
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
  }
}

object PlayerManager {
  /**
   * Creates new instance of player
   */
  case class NewPlayer(name: String/*, sock: Socket*/, ps: PrintStream, br: BufferedReader)
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

}
