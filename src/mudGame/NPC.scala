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
    private var inventory: MDLList[Item],
    private var health: Double,//stats(2)
    val moveRate: Int,//stats(3)
    val canSmartMove: Boolean//stats(4)
    ) extends Actor {   

  private var loc: ActorRef = null
  private var equiped: Option[Item] = None
  private var fighting = false
  //private var stillHere: Boolean = true
  val r = new Random(6)
  
  import NPC._
  import PlayChar._
  def receive = {
    
    case Move =>loc ! Room.GetExit(name, r.nextInt())
      if (canSmartMove == false) loc ! Room.GetExit(name, r.nextInt())
      
      
    case NoSuchPlayer(msg)=> 
      fighting = false
      move()
    
    case Hit(playerName, player, damage)=>      
      player ! PlayChar.Damage(playerName, player, damage)
      attack(playerName)
    
    case Damage(perpName, perp, amount) => 
      val newHealth = health - amount
      health = newHealth
      if(health <= 0){
        loc ! Room.SendMessage(s"$name had died.")
        exit()
      } else attack(perpName)

    case EnterRoom(room) => 
      println("NPC.EnterRoom")
      loc = room
      move()
    case m =>
      println("Oops! Bad message to:" + self.toString() + m)  
  }
  
  def attack(playerName: String): Unit = {
    if(fighting == false){
      fighting = true
      equip()
      if (equiped == None){
        val damage = 5
        val speed = 50
        loc ! Room.AtemptAttack(playerName, speed, damage)
      } else {
        val weapon = equiped.get 
        val damage = weapon.damage
        val speed = weapon.speed
        loc ! Room.AtemptAttack(playerName, speed, damage)
      }
    }
  }

  
  def equip(): Unit = {
    if (inventory.length != 0){
      if (inventory.length > 1){
        var chosen = inventory(0)
        for(i <- 1 until inventory.length){
          if(inventory(i).damage > chosen.damage)
            chosen = inventory(i)
        }
        equiped = Some(chosen)
      } else equiped = Some(inventory(0))
    } else equiped = None
  }
  
  def exit(): Unit = {
    for(i <- 0 until inventory.length){
      val prize = inventory(i)
      inventory.remove(i)
      loc ! Room.DropItem(prize)
    }
	  loc ! Room.DropPlayer(name, self)
	  context.parent ! NPCManager.RemoveChild(self) 
	  context.stop(self)
	}
  
  def smartMove(): Unit = {
    ???
  }
  
  def move(): Unit = {
    println(s"moving $name")
    if (fighting == false){
      Main.actManager ! ActivityManager.Schedual(self, NPC.Move, moveRate)
    }
  }
}

object NPC{
  
  case object Move
  
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