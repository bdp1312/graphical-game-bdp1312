package mudGame

class Item (
    val name: String,
    val desc: String,
    val damage: Int,
    val speed : Int
    ){
  /**
   * at this point item to only have a function to get item data
   */
  
  private var _ready = true
  val ready = _ready
  
  def printDesc(): Unit = {
    println(s"$name: $desc")
  }
}

object Item {
  def apply(n: xml.Node): Item = {
    val name = (n \ "@name").text.trim
    val desc = (n \ "@desc").text.trim
    val damage = (n \ "@damage").text.trim.toInt
    val rechar = (n \ "@recharge").text.trim.toInt
    return new Item(name, desc, damage, rechar)
  }
}