package ot.dispatcher.sdk

import com.typesafe.config.Config
import org.apache.log4j.Level
import org.apache.spark.sql.DataFrame
import ot.dispatcher.sdk.core.{BaseCommand, SimpleQuery}

/**
  *       ____  ______
  *      / __ \/_  __/____  __     __  ____  ____  __  ____  _   _     ____  ____  __ _
  *     / / / / / /  (  _ \(  )   / _\(_  _)(  __)/  \(  _ \(  \/  )  / ___)(    \(  / )
  *    / /_/ / / /   )  __// (_/\/   \ ) (  ) _) ( O ))   / / \/  \   \___ \) D ( )  (
  *    \____/ /_/   (__)  \____/\_/\_/(__) (__)  \__/(__\_)\_ )(_ /  (____/(____/(__\_)
  *
  *  ==Creating plugin==
  *  1. Append dispatcher sdk to libraryDependencies with scope __Compile__.
  *  {{{libraryDependencies += "ot.dispatcher" % dispatcher-sdk_2.11" % "sdk_version"  % Compile}}}
  *  2. Extend your command class from [[ot.dispatcher.sdk.PluginCommand]] class and realise
  * transform method.
  * {{{ MyCommand(query: SimpleQuery, utils: PluginUtils) extends PluginCommand(sq, utils, Set("from", "to")){
  *       def transform(_df: DataFrame): DataFrame = {
  *           /* some dataframe transformations*/
  *    }
  * } }}}
  *  3. Create file ''commands.properties'' in resources folder.
  *  4. Write your commandname and path to the class into file ''commands.properties''.
  * {{{commandname=mypackage.MyCommand }}}
  *  5. Create file ''plugin.conf'' in resources folder.
  *  6. Write required property ''pluginName'' to file ''plugin.conf''.
  *  {{{pluginName="my-plugin"}}}
  *  7. Build your project by sbt with target __package__.
  */

abstract class PluginCommand(query: SimpleQuery, utils: PluginUtils, seps: Set[String] = Set()) extends BaseCommand(query, seps) {

  //Separators witch used to parse simple query string
  val separators: Set[String] = seps

  //Set log level according to loglevel.properties
  log.setLevel(Level.toLevel(logLevel))

  /**
    *  Makes transformation on given dataframe. Should be implemented.
    *
    * @param _df [[DataFrame]] - dataframe to be modified
    * @return [[DataFrame]] - dataframe after modifications
    */
  def transform(_df: DataFrame): DataFrame

  //Log level witch used when writing logs by default.
  def logLevel: String = utils.logLevelOf(classname)
  /**
    * Prints the string representation of first 10 rows of dataframe to log
    *
    * @param df [[DataFrame]] - dataframe to be printed
    */
  def printDfHeadToLog(df: DataFrame): Unit =  utils.printDfHeadToLog(log, query.searchId, df)
  /**
    * Sends an error message to UI
    * @param message [[String]] - Error message to be shown in UI
    */
  def sendError(message: String): Nothing = utils.sendError(query.searchId, message)

  //Config witch used to get properties from plugin configuration (plugin.conf)
  def pluginConfig: Config = utils.pluginConfig

  //Config witch used to get properties from main application configuration (application.conf)
  def mainConfig: Config = utils.mainConfig
}
