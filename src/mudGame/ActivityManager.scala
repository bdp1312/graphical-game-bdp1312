package mudGame

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props

class ActivityManager extends Actor{
  var currantTime = 0;
  class Event (asender: ActorRef, atime: Int, amessage: Any){
    var time = atime
    val sender = asender
    val message = amessage
  }
  
  def isHigherPriority(i: Event, j: Event): Boolean = {
    if (i.time < j.time) return true
    else return false
  }
  
  def execute(theTime: Int): Unit = {
    val event = activityQ.peek
    if(event.time == theTime){
      event.sender ! event.message
      activityQ.dequeue()
      if (!activityQ.isEmpty) execute(theTime)
    }
  }
  
  var activityQ = new PriorityQueue[Event](isHigherPriority)
  //var activityQ = new BHPQ[Event](isHigherPriority) 
  
  import ActivityManager._
  def receive = {
    case Update =>
      currantTime += 1
      if (!activityQ.isEmpty) execute(currantTime)
    
    case Schedual(theSender, msg, delay) =>
      val instant = currantTime + delay
      val event = new Event(theSender, instant, msg)
      activityQ.enqueue(event)
    
    case m =>
      println("Oops! Bad message sent to ActivityManager: "+m)
  }
}
object ActivityManager{
  case object Update
  case class Schedual(theSender: ActorRef, msg: Any, delay: Int)
}