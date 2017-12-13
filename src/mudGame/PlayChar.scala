package mudGame

import akka.actor.Actor
import akka.actor.ActorRef

object PlayChar {
  case class Hit(playerName: String, player: ActorRef, damage: Int)
  case class Damage(perpName: String, perp: ActorRef, amount: Int)
  case class EnterRoom(room: ActorRef)
  case class NoSuchPlayer(msg: String)
}