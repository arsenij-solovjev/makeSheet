import ca.zmatrix.utils.ParseParms
import java.io.File
import scala.io.Source
object makeSheet {

  def main(args: Array[String]) {

    // init param parser
    val help = "-n[umber of assignment] 1 -d[ue date] 11.10 -e[xercises] 1,2,3 -c[onfigure] f"
    val parameters = new ParseParms(help)
    parameters.parm("-n", "1").rex("^\\d+$")
      .parm("-d", "01.01").rex("^\\d{2}\\.\\d{2}$")
      .parm("-e", "1,2,3").rex("^\\d(,\\d)*$")
      .parm("-c")
    
 
    //validate args
    val paramResult = parameters.validate(args.toList)
    println(if (paramResult._1) paramResult._3 else paramResult._2)

    //val configArgsResult = configArgs.validate(args.toList)
    //println(if (configArgsResult._1) configArgsResult._3 else configArgsResult._2)

    if (!paramResult._1)

      System.exit(1)

 
    // match on paramResult._3
    if (false){
      Configuration.make
    }
    if (paramResult._1) {
      Sheet.make(paramResult._3)
    }

    System.exit(0)
  }
  
}

