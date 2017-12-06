package mudGame

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import RoomManager._
import java.net.Socket

class NPCManager extends Actor{
  import NPCManager._
  def receive = {
    case NewNPC(nNpc, loc) =>
      println("NPCManager.NewNPC")
      if (nNpc._1.length != 5) println("WARNING! nNpc._1.length != 6")
      else {
        val stats = nNpc._1
        val lname = stats(0).filter(_.isLetterOrDigit) 
        val nonPlayerChar = context.actorOf(Props(new NPC(stats(0), stats(1), nNpc._2, stats(2).toDouble, stats(3).toInt, stats(4).toBoolean)), lname)
        Main.roomManager ! SendPlayerRoom(nonPlayerChar, loc)        
      }
  }
}
  
object NPCManager{
  
  case class NewNPC(NPCarg: (List[String], MDLList[Item]), loc: String)
  
}