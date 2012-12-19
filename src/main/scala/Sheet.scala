import java.io.PrintWriter
import java.io.File
import scala.collection.mutable.ListBuffer

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
    val preamble = createPreamble(sheetNumber, dueDate)
    val inputs = assignments.foldLeft("")((x, y) => x + "\\input{" + y + "}\n")
    preamble.replace("#CONTENTS#", inputs)
  }


  /**
   * parses the config.xml settings into the preamble
   */
  private def createPreamble(sheetNumber: String, dueDate: String) = {
    import scala.xml._

    //read xml
    val conf = XML.load(getClass.getResource("config.xml"))
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
    lines = lines.replace("#ASSIGNMENT#", sheetNumber)
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
    assignments map (x => createFile(dirLocation, "/exercise" + x + ".tex", "\\section*{Aufgabe " + x + "}"))
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
  def make(parameters: Map[String, String]) {
    val sheetNumber = parameters("-n")
    val dueDate = parameters("-d")
    val exercises = parameters("-e").split(",")
    // construct sheet
    val sheet = new Sheet(sheetNumber, dueDate, exercises)
    // print
    sheet.write
  }
}
