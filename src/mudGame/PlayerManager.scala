package mudGame

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef



class PlayerManager extends Actor {  
  val players: Map[String, Player]
  import PlayerManager._
  def receive = {
    case makePlayer =>
      players :: context.actorOf(Props(new Player(name = "")))
      number += 1
    case CheckForInput =>
      println("updating players")
      context.children.foreach(_ ! CheckForInput)
    case AddPlayerAtStart =>
      RoomManager ! RoomManager.AddPlayerAtStart(newPlayer: Player )
      //player ! Player.enterRoom(rooms(startRoom))
  }
}

object PlayerManager {
  /**
   * Creates new instance of player
   */
  case object makePlayer
  /**
   * Tells all players to check for input
   */
  case object CheckForInput
  /**
   * sets new player name, puts player into the Inn
   */
  case class SettupPlayer(player: Player, startingRoom: String)

}
