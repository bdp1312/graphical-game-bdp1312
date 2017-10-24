package mudGame

class Item (
    val name: String,
    //held: Boolean,
    val effect: String //effect to be own class in future version?
    ){
  /**
   * at this point item to only have a function to get item data
   */
  def printDesc(): Unit = {
    println(s"$name: $effect")
  }
}

object Item {
  def apply(n: xml.Node): Item = {
    val name = (n \ "@name").text.trim
    val desc = (n \ "desc").text.trim
    new Item(name, desc)
  }
}