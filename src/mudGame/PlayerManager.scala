package mudGame

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import java.io.PrintStream
import java.io.BufferedReader
import java.net.Socket



class PlayerManager extends Actor {
  var rm: ActorRef = self
  val players: Map[String, Player] = null
  import PlayerManager._
  def receive = {
    case NewPlayer(name/*, sock*/, ps, br) =>
      val lname = name.filter(_.isLetterOrDigit)
      if(context.child(lname).isEmpty){
        context.actorOf(Props(new Player(name/*, sock*/, ps, br)), lname)
      }else {
        ps.println("This name is taken.")
        //sock.close()
      }
    case SendMessage(msg) =>
      context.children.foreach(_ ! Player.Print(msg))
    case CheckForInput =>
      println("updating players")
      context.children.foreach(_ ! CheckForInput)
    case StartNewPlayer(newPlayer) =>
       rm ! newPlayer
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
