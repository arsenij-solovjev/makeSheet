import java.io.File
import scala.io.Source
import org.rogach.scallop._

object makeSheet {
  /**
   *
   *  if no configuration for this directory launch config  no matter what parameters are passed, then quit and ask for a run with params
   * create the config file as an invisible one
   */
  def main(args: Array[String]) {

    object Parameters extends ScallopConf(args) { 

      val configure = toggle("configure", default = Some(false))

      val sheetNumber = opt[String]("number", required = (configure == Some(false)))
      val dueDate = opt[String]("dueDate", required = (configure == Some(false)))
      val exercises = opt[String]("exercises", required = (configure == Some(false)))
      verify
    }

    if (Parameters.configure.get == Some(true) 
        || !(new File(".config.xml")).exists) {
      Configuration.make
    } else {
      Sheet.make(
        Map(
          "-n" -> Parameters.sheetNumber,
          "-d" -> Parameters.dueDate,
          "-e" -> Parameters.exercises
        )
      )
    }

    System.exit(0)
  }

}

