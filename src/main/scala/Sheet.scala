import java.io.PrintWriter
import java.io.File
import scala.collection.mutable.ListBuffer
import scala.xml.XML
import org.rogach.scallop._
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Sheet which can be constructed, from user input and config.xml
 *
 */

class Sheet(sheetNumber: String, dueDate: String, exercises: Seq[String]) {

  // init vars
  var assignments: ListBuffer[String] = ListBuffer()

  // adds an assignment
  def addAssignment(number: String) = assignments += number

  for (e <- exercises) addAssignment(e)

  val sheet = createSheet

  /**
   * creates the contents of the assignment sheet
   */
  private def createSheet = {
    val preamble = createPreamble
    val inputs = assignments.foldLeft("")((x, y) => x + "\\input{" + y + "}\n")
    preamble.replace("#CONTENT#", inputs)
  }

  /**
   * parses the config.xml settings into the preamble
   */
  private def createPreamble = {
    import scala.xml._

    //read xml
    val conf = XML.load(".config.xml")
    val course = (conf \\ "course").text
    val semester = (conf \\ "semester").text
    val tutor = (conf \\ "tutor").text
    val students = for (s <- conf \\ "students" \\ "student") yield s.text

    // create the preamble
    preamble(sheetNumber, dueDate, course, semester, tutor, students)
  }

  /**
   * preamble skeleton
   */
  private def preamble(sheetNumber: String, dueDate: String, courseName: String, semester: String, tutor: String, students: Seq[String]): String = {

    // read template
    val source = scala.io.Source.fromURL(getClass.getResource("template.tex"))
    var lines = source.mkString
    source.close()

    //replace placeholders
    lines = lines.replace("#TUTOR#", tutor)
    lines = lines.replace("#DUEDATE#", dueDate)
    lines = lines.replace("#COURSE#", courseName)
    lines = lines.replace("#SEMESTER#", semester)
    lines = lines.replace("#ASSIGNMENT#", sheetNumber + ". Übungszettel")
    lines = lines.replace("#STUDENTS#", students.mkString("\\\\"))
    lines
  }

  /**
   * write assignments and exercises to disk
   */
  private def write() = {
    // create the directory for this sheet
    val dirLocation = "./" + sheetNumber
    new File("./" + sheetNumber).mkdir()

    // create sheet directory
    createFile(dirLocation, "/assignment" + sheetNumber + ".tex", sheet)
    assignments map (
      i => 
        createFile(dirLocation, "/" + i + ".tex", "\\section*{Aufgabe " + i + "}")
    )
  }

  /**
   * writes file to disk
   */
  private def createFile(directory: String, fileName: String, content: String) {
    val writer = new PrintWriter(new File(directory + fileName))
    println("Writing  " + directory + fileName)
    writer.write(content)
    writer.close()
  }
}

object Sheet {
  def make(parameters: Map[String, ScallopOption[String]]) {
    var sheetNumber, dueDate = ""
    var exercises = Array[String]()
    // this part sucks
    for (parameter <- parameters){
      parameter._1 match {
        case "-n" => parameter._2.get match{
          case None  => sheetNumber= promptParameter("-n", "What number is the assignment?")
          case Some(x) => sheetNumber = x 
        }
        case "-d" => parameter._2.get match{
          case None  => dueDate = promptParameter("-d", "What date is the assignment due?")
          case Some(x) => dueDate = x 
        } 
        case "-e" => parameter._2.get match{
          case None  => exercises= promptParameter("-e", "What exercises are on the assignment? (commma-separated)").split(",")
          case Some(x) => exercises = x.split(",") 
        } 
        case _ => ;
      }
    }  
    // construct sheet
    val sheet = new Sheet(sheetNumber, dueDate, exercises)
    // print
    sheet.write
    persistLastUsage(sheetNumber, dueDate, exercises.mkString(","))
  }

  /** 
    * prompts the user for a parameter. if the user doesn't enter a value
    * a default value is used
    */ 
  private def promptParameter (parameter:String, message:String):String = {
    // prompt for parameter
    val input = Console.readLine(message + "(default: " + lastUsage(parameter)+ ")" )
    // if user just typed enter choose the default case
    // else take the provided value
    input match{
      case "" => lastUsage(parameter)
      case x => x
    }
    
  }

  /** 
    * yields a parameter from the last usage
    */ 
  private def lastUsage(parameter:String):String =  {
    val lastUsage = XML.load(".lastUsage.xml")
    parameter match {
      case "-n" => {
        val input = (lastUsage \\ "sheetNumber").text
        (input.toInt + 1) + "" 
      }
      case "-d" => {
        val date = new SimpleDateFormat("dd.MM").parse((lastUsage \\ "dueDate").text)
        var cal = Calendar.getInstance();
        cal.setTime(date); 
        cal.add(Calendar.DAY_OF_MONTH, 7);  // number of days to add
        val d = cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH)+1)
        d 
      }
      case "-e" => (lastUsage \\ "exercises").text
    } 
  } 
/** 
  * writes the last used parameters to a file, overwriting it after each usage
  */ 
  private def persistLastUsage(sheetNumber: String, dueDate:String, exercises:String){
    val lastUsage = 
<lastUsage> 
    <sheetNumber>{sheetNumber}</sheetNumber>  
    <dueDate>{dueDate}</dueDate>
    <exercises>{exercises}</exercises> 
</lastUsage> 

   XML.saveFull(".lastUsage.xml", lastUsage, "UTF-8", true, null) 
  }
}
 
 
