package mudGame

import java.io.BufferedReader
import java.io.PrintStream

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props



class PlayerManager extends Actor {
  var rm: ActorRef = self
  var players: Map[String, Player] = null
  import PlayerManager._
  def receive = {
    case NewPlayer(name/*, sock*/, ps, br) =>
      println("NewPlayer")
      val lname = name.filter(_.isLetterOrDigit)
      if(context.child(lname).isEmpty){
        context.actorOf(Props(new Player(name/*, sock*/, ps, br)), lname)
      }else {
        ps.println("This name is taken.")
        //sock.close()
      }
    case SendMessage(msg) =>
      println(context.children.toString)
      context.children.foreach(_ ! Player.Print(msg))
    case CheckForInput =>
      context.children.foreach(_ ! Player.CheckForInput)
    case StartNewPlayer(newPlayer) =>
       rm ! StartPlayer(newPlayer)
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
   * sets new player name, puts player into the Inn
   */
  case class StartNewPlayer(newPlayer: ActorRef)
  /**
   * gets roomManager actor ref
   */
  case class RoomManager(roomManager: ActorRef)

}
