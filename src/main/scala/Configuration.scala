
object Configuration {
  /**
   * is run to create or overwrite the config.xml
   */
  def make = {

    println("No existing config file found, now we will create one:")

    val conf = <makesheet>
                 println("What is the name of your course?")

    val course = Console.readLine()
    conf+=<course> + Console.readLine + </course>
    conf+=<semester> + Console.readLine("In what semester does the course take place?") + </semester>
    conf+=<tutor> + Console.readLine("What is the name of your tutor?") + </tutor>
    conf+=<students>
    while((val student = Console.readLine("Please enter the name of the student [or just type A to abort]")!="A"))
      conf+= student

    conf+= </students>
    conf += </makesheet>
  }
}
