import java.io.File

import com.typesafe.config.ConfigFactory

import scala.util.Try
import scala.xml.{Elem, XML}

/**
  * Created by Maksim Budilovskiy on 19.09.2016.
  * XML Formatter from CSV at http://www.freeformatter.com/csv-to-xml-converter.html
  */
object Application extends App {

  val config = ConfigFactory.load()
  val xmlPath: String = config.getConfig("xmlPath").getString("path")
  val folderPath: String = config.getConfig("folderPath").getString("path")
  val xml: Elem = XML loadFile xmlPath

  val indexes: Seq[String] = (xml \\ "filenames" \\ "filename" \ "index").theSeq.map(_.text)
  val names: Seq[String] = (xml \\ "filenames" \\ "filename" \ "name").theSeq.map(_.text)

  def newNames(xs: Seq[String], ys: Seq[String]): Seq[String] =
    (xs zip ys).map(x => x._1 + "_" + x._2)

  val namesMap: Map[String, String] = (names zip newNames(indexes, names)).toMap

  def rename(fileName: String): Unit = {
    val newName = namesMap(fileName)
    val res = Try(new File(folderPath + fileName).renameTo(new File(folderPath + newName))).getOrElse(false)
    if (res) {
      println("File " + fileName + " renamed sucssessfully to " + newName)
    } else {
      println("Error while renaming file " + fileName)
    }
  }

  names.foreach(rename)

}