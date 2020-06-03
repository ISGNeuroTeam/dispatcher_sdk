package ot.dispatcher.sdk.core

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.DataFrame
import ot.dispatcher.sdk.core.parsers.DefaultParser
import ot.dispatcher.sdk.core.extensions.StringExt._

abstract class BaseCommand(sq: SimpleQuery, seps: Set[String] = Set.empty) extends DefaultParser {
  val args = sq.args

  val keywords = keywordsParser(args)
  val keywordsMap = fieldsToMap(keywords)
  val positionals = positionalsParser(args, seps)
  val positionalsMap = fieldsToMap(positionals)
  val returns = returnsParser(args, seps)

  def fieldsUsed = getFieldsUsed(returns)
  def fieldsGenerated = getFieldsGenerated(returns)

  var fieldsUsedInFullQuery = Seq[String]()

  def setFieldsUsedInFullQuery(fs: Seq[String]) = {
    fieldsUsedInFullQuery = fs
    this
  }

  val classname = this.getClass.getSimpleName
  def commandname = this.getClass.getSimpleName.toLowerCase

  def loggerName = this.getClass.getName
  def log: Logger = Logger.getLogger(loggerName)

  def getKeyword(label: String): Option[String] = {
    keywordsMap.get(label).map {
      case Keyword(k, v) => v
    }
  }

  def getPositional(label: String): Option[List[String]] = {
    positionalsMap.get(label).map {
      case Positional(k, v) => v
    }
  }

  def getKeywords(): Map[String, String] = keywords.map{case Keyword(k,v) => k -> v}.toMap

  def mainArgs: List[String] = fieldsUsed.map(_.stripBackticks())

}
