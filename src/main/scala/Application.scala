import java.io.File

import com.typesafe.config.ConfigFactory

import scala.util.Try
import scala.xml.{NodeSeq, XML}

/**
  * Created by Maksim Budilovskiy on 19.09.2016.
  */
object Application extends App {

  val config = ConfigFactory.load()
  val xmlPath: String = config.getConfig("xmlPath").getString("path")
  val folderPath: String = config.getConfig("folderPath").getString("path")
  val xml = XML loadFile xmlPath

  val indexes: NodeSeq = xml \\ "filenames" \\ "filename" \ "index"
  val names: NodeSeq = xml \\ "filenames" \\ "filename" \ "name"

  def makeList(xs: NodeSeq, ys: NodeSeq): Seq[(String, String)] = {
    xs.theSeq.map(_.text) zip ys.theSeq.map(_.text)
  }

  val oldNames: Seq[String] = names.map(_.text)
  val newNames: Seq[String] = makeList(indexes, names).map(x => x._1 + "_" + x._2)
  val namesMap: Map[String, String] = (oldNames zip newNames).toMap

  def rename(fileName: String): Unit = {
    val newName = namesMap(fileName)
    val res = Try(new File(folderPath + fileName).renameTo(new File(folderPath + newName))).getOrElse(false)
    if (res) {
      println("File " + fileName + " renamed sucssessfully to " + newName)
    } else {
      println("Error while renaming file " + fileName)
    }
  }

  oldNames.foreach(rename)

}