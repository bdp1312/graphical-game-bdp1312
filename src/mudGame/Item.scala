

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

object item {
  /**
   * takes input from file, initialies name and effect
   */
}
  