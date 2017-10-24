package mudGame

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef



class PlayerManager extends Actor {  
  val players: Map[Player.name, Player]
  import PlayerManager._
  def receive = {
    case makePlayer =>
      players :: context.actorOf(Props(new Player(name = "")))
      number += 1
    case CheckForInput =>
      println("updating players")
      context.children.foreach(_ ! CheckForInput)
    case AddPlayerAtStart =>
      RoomManager ! AddPlayerAtStart(newPlayer: Player )
      //player ! Player.enterRoom(rooms(startRoom))
  }
}

object PlayerManager {
  case object makePlayer
  case object CheckForInput
  case class SettupPlayer(player: Player, startingRoom: String)

}
