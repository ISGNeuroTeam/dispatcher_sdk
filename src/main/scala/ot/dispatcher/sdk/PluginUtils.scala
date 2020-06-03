package ot.dispatcher.sdk

import org.apache.log4j.Logger
import org.apache.spark.sql.{DataFrame, SparkSession}

trait PluginUtils extends PluginConfig{
  /**Retuns logger with loging level specified by [[logLevelOf]] function.
    * @param classname [[String]] - full name of class.
    * @return [[Logger]] - logger for given classname.
    */
  def getLoggerFor(classname: String): Logger
  /**
    * Get log level from loglevel.properties
    *
    * @param name [[String]] - full name of class.
    * @return [[String]] - string representation of log level taken from config.
    */
  def logLevelOf(name: String): String
  /**
    * Prints the string representation of first 10 rows of dataframe to log.
    *
    * @param df [[DataFrame]] - dataframe to be printed.
    */
  def printDfHeadToLog(log: Logger, id: Int, df: DataFrame): Unit

  /**
    * Sends an error message to UI.
    * @param message [[String]] - Error message to be shown in UI.
    */
  def sendError(id: Int, message: String): Nothing

  // Spark session witch used to create Dataframes, data reading and other spark functionality.
  def spark: SparkSession

  /**
    * Perform query commands on given dataframe.
    * Depends on main application context, can't checked in plugin tests.
    *
    * @param query [[String]] - query to be executed.
    * @param df [[DataFrame]] - input dataframe.
    * @return [[DataFrame]] - dataframe modified by query.
    */
  def executeQuery(query: String, df: DataFrame): DataFrame

  /**
    * Perform query commands with reading from index.
    * Depends on main application context, can't checked in plugin tests.
    *
    * @param query [[String]] - query to be executed
    * @param index [[DataFrame]] - index for loading events
    * @param startTime [[DataFrame]] - lower bound of time of loaded events
    * @param finishTime [[DataFrame]] - upper bound of time of loaded events
    * @return [[DataFrame]] - query result
    */
  def executeQuery(query: String, index: String, startTime: Int, finishTime: Int): DataFrame
}
