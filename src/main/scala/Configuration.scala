import java.io.PrintWriter
import java.io.File
import scala.xml.XML
import scala.collection.mutable.ListBuffer
object Configuration {
  /**
   * dialog to make new .config.xml
   */
  def make = {

    println("No existing config file found, now we will create one:")

    val course = Console.readLine("What is the name of your course?")
    val semester = Console.readLine("In what semester does the course take place?")
    val tutor = Console.readLine("What is the name of your tutor?")
    var students:ListBuffer[String] = ListBuffer(Console.readLine("Please enter the name of the student "))
    
    while (students.last != "A")
      students+=Console.readLine("Please enter the name of the student [or just type A to abort]")
    students.remove(students.size-1)
    


    val conf = 
<makesheet>
  <course>{course}</course>
  <semester>{semester}</semester>
  <tutor>{tutor}</tutor>
  <students>
    {for( student <- students) yield
         <student>{student}</student>  
    }
  </students>
</makesheet>
   

   XML.saveFull(".config.xml", conf, "UTF-8", true, null)  
   println("Written .config.xml")
  }

}
