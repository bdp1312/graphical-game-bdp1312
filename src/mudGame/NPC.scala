package mudGame

import java.io.PrintStream
import java.io.BufferedReader
import akka.actor.Actor
import akka.actor.ActorRef
import java.net.Socket
import scala.util.Random

class NPC (
    val name: String,//stats(0)
    val desc: String,//stats(1)
    private var stash: MDLList[Item],
    private var health: Double,//stats(2)
    val moveRate: Int,//stats(3)
    val canSmartMove: Boolean//stats(4)
    ) extends Actor {   

  private var loc: ActorRef = null
  private var stillHere: Boolean = true
  val r = new Random(6)
  
  import NPC._
  def receive = {
    ???
  }
  
  def smartMove {
    ???
  }
  
  def dumbMove {
    loc ! Room.GetExit(r.nextInt()) 
  }

  
}

object NPC{
  def collect (n: xml.Node): (List[String], MDLList[Item]) = {
    val name = (n \ "@name").text.trim
    val desc = (n \ "@desc").text.trim
    val health = (n \ "@health").text.trim
    val moveRate = (n \ "@moveRate").text.trim
    val smart = (n \ "@smart").text.trim
    val itemsList = (n \ "item").map(Item.apply).toList
    val stash = new MDLList [Item]
    for (i <- itemsList) stash.add(i)
    val stats = List(name, desc, health, moveRate, smart)
    val NPCArg = (stats, stash)
    return NPCArg 
  }
}