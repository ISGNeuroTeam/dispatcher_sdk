package ot.dispatcher.sdk.test

import java.io.{File, PrintWriter}
import org.apache.log4j.Logger
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterAll
import ot.dispatcher.sdk.PluginCommand

import scala.reflect.io.Directory

abstract class CommandTest extends AnyFunSuite with BeforeAndAfterAll {

  val log: Logger = Logger.getLogger("TestLogger")

  val spark = SparkSession.builder()
    .appName("Plugin Tests")
    .master("local")
    .config("spark.sql.files.ignoreCorruptFiles", true)
    .getOrCreate()

  // Default dataset value. Should be implemented as string in json format.
  val dataset: String

  val tmpPath: String =  "src/test/resources/temp"
  //Use this directory for temporal files when running tests.
  val tmpDir = new Directory(new File(f"$tmpPath"))
  val locksPath = s"$tmpDir/locks"
  val locksDir = new Directory(new File(locksPath))
  val lockFile = new File(locksPath, s"test-${this.getClass.getSimpleName}")

  /**
    * @param json1 [[String]]
    * @param json2 [[String]]
    * @return [[Boolean]] - true if both json strings contains the same data and false otherwise.
    */
  def jsonCompare(json1 : String,json2 : String): Boolean = {
    import spray.json._
    val j1 = json1.parseJson.sortedPrint
    val j2 = json2.parseJson.sortedPrint
    j1==j2
  }

  /** Creates mock of [[ot.dispatcher.sdk.PluginUtils]]
    *
    * @return[[MockPluginUtils]]
    */
  def utils = new MockPluginUtils(spark)

  /** Runs [[execute]] with initial dataframe created from [[dataset]] value.
    *
    * @param commands [[PluginCommand*]] - sequence of commands to execute.
    * @return [[String]] - result string in json format.
    */
  def execute(commands: PluginCommand*): String = {
    execute(jsonToDf(dataset), commands: _*)
  }

  /** Runs a chain of transform methods from commands on given dataframe.
    *
    * @param initialDf [[DataFrame]] - dataframe to make transformations on.
    * @param commands [[PluginCommand*]] - sequence of commands to execute.
    * @return [[String]] - result string in json format.
    */
  def execute(initialDf: DataFrame, commands: PluginCommand*): String = {
    val df = commands.foldLeft(initialDf) {(accDf, command) => command.transform(accDf)}
    df.toJSON.collect().mkString("[\n",",\n","\n]")
  }

  /** Converts json string to spark dataframe.
    * @param json [[String]] - string in json format.
    * @return [[DataFrame]]
    */
  def jsonToDf(json: String): DataFrame = {
    import spark.implicits._
    spark.read.json(Seq(json.stripMargin).toDS)
  }
  //Creates clean temporal directory for using in tests.
  override def beforeAll() {
    if(tmpDir.exists) tmpDir.deleteRecursively()
    tmpDir.createDirectory()
    locksDir.createDirectory()
    new PrintWriter(lockFile) {close()}
  }

  //Removes temporal directory.
  override def afterAll() {
    lockFile.delete()
    if(locksDir.files.isEmpty) tmpDir.deleteRecursively()
  }
}
