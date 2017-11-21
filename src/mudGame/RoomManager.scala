package mudGame


import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef

class RoomManager extends Actor {
  val rooms = {
    println("Loading rooms")
    val xData = xml.XML.loadFile("rooms.xml") //exchange XML with current text file protocol
    (xData \ "Room").map(n => {
      val (key, builder) = Room.apply(n) 
      key -> context.actorOf(Props(builder()), key)
    }).toMap
  }
  
  context.children.foreach(_ ! Room.LinkExits(rooms))
  
  import RoomManager._
  
  def receive = {
    case CheckRoomStatus =>
      println(" ")
    case SendPlayerRoom(player, place) =>
      val room = rooms(place)
      player ! Player.PlaceValue(room)
      room ! Room.AddPlayer(player)
    case ChangePlayerLocation(room) =>
      val newRoom = room
      sender ! Player.EnterRoom(newRoom)
//    case StartPlayer(newPlayer) =>
//      newPlayer ! Player.EnterRoom(rooms("The Inn #0"))
      
      // player ! EnterRoom(rooms(startRoom))
    case m =>
      println("Oops! Bad message sent to RoomManager: "+m)
  }
}

object RoomManager {
  case object CheckRoomStatus
  case class SendPlayerRoom(player: ActorRef, place: String)
  case class ChangePlayerLocation(room: ActorRef)
  case class StartPlayer(newPlayer: ActorRef)
  case class TeleportPlayer(player: ActorRef, roomKey: String)
}