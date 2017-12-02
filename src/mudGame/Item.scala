package mudGame

class Item (
    val name: String,
    val desc: String
    //val damage: Int
    //val recharge : Int
    ){
  /**
   * at this point item to only have a function to get item data
   */
  def printDesc(): Unit = {
    println(s"$name: $desc")
  }
}

object Item {
  def apply(n: xml.Node): Item = {
    val name = (n \ "@name").text.trim
    val desc = (n \ "@desc").text.trim
    return new Item(name, desc)
  }
}