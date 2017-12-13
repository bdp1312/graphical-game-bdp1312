package mudGame

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import RoomManager._
import java.net.Socket

class NPCManager extends Actor{
  
  private var npcs = collection.mutable.Buffer[(String, akka.actor.ActorRef)]()
  private var count = 0
  import NPCManager._
  def receive = {
    case NewNPC(nNpc, loc) =>
      println("NPCManager.NewNPC")
      if (nNpc._1.length != 5) println("WARNING! nNpc._1.length != 6")
      else {
        val stats = nNpc._1
        var lname = (stats(0).filter(_.isLetterOrDigit) + count)
        while(!context.child(lname).isEmpty){
          count += 1
          lname = (stats(0).filter(_.isLetterOrDigit) + count)
        }
        val nonPlayerChar = context.actorOf(Props(new NPC(stats(0), stats(1), nNpc._2, stats(2).toDouble, stats(3).toInt, stats(4).toBoolean)), lname)
        Main.roomManager ! SendPlayerRoom(stats(0), nonPlayerChar, loc)        
      }
    case RemoveChild(child) =>
      println("stoping " + child)
      context.stop(child)
  }
}
  
object NPCManager{
  
  case class NewNPC(NPCarg: (List[String], MDLList[Item]), loc: String)
  case class RemoveChild(child: ActorRef)
  
}