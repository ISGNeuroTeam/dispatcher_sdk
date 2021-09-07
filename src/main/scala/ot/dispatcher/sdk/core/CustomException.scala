package ot.dispatcher.sdk.core

import scala.io.Source
import scala.util.{Failure, Success, Try}


class CustomException (errorCode :Integer, searchId:Integer, message: String, msgArgs: List[Any])
  extends Exception(message){
  def this (errorCode :Integer, searchId:Integer, message: String, cause: Throwable, msgArgs: List[Any]) {
    this(errorCode, searchId, message, msgArgs)
    initCause(cause)
  }
  override def getMessage: String = f"[SearchId:$searchId] " + super.getMessage
  override def getLocalizedMessage: String = Try(getErrorDescription(errorCode).format(msgArgs : _*)) match { //"Ошибка запуска процедуры %s : %s"
    case Success(msg) => f"[SearchId:$searchId] "+ msg
    case Failure(_) => f"[SearchId:$searchId] "+ message
  }
  def getErrorDescription(code: Integer) = {
    val strCode = code + ": "
    val dictionary = scala.util.Properties.propOrElse("files.log_localisation", "123")
    val file = Source.fromFile(dictionary)
    val res = file.getLines.toList
      .find(l => l.startsWith(strCode)).map(l => l.substring(strCode.length)).getOrElse(message)
    file.close()
    res
  }
}

object CustomException{
  def apply(errorCode: Integer, searchId: Integer, message: String, msgArgs: List[Any] = List()): CustomException =
    new CustomException(errorCode, searchId, message,msgArgs)
  def apply(errorCode: Integer, searchId: Integer, message: String, cause: Throwable, msgArgs: List[Any]): CustomException =
    new CustomException(errorCode, searchId, message,cause , msgArgs)

  /**
   * Initial exceptions defined for the Dispatcher
   */
  val E00001 = () =>                                                                        CustomException(1, -1, "pluginName variable not found in plugin.conf inside jar", List())
  val E00002 = (searchId: Integer, message: String) =>                                      CustomException(2, searchId, message, List())
  val E00003 = (searchId: Integer, message: String) =>                                      CustomException(3, searchId, f"Error in 'read' command", List("read", message))
  val E00004 = (searchId: Integer, index: String) =>                                        CustomException(4, searchId, f"Index not found: $index", List(index))
  val E00005 = (searchId: Integer, index: String, message: String, exception: Throwable) => CustomException(5, searchId, f"Error in 'read' command for index=$index", exception, List(index, message))
  val E00006 = (searchId: Integer) =>                                                       CustomException(6, searchId, "Time window is in cache_duration", List())
  val E00007 = (searchId: Integer, message: String, exception: Throwable) =>                CustomException(7, searchId, f"Runtime error: $message", List("udf function", exception))
  val E00008 = (searchId: Integer) =>                                                       CustomException(8, searchId, "Subsearch failed. Check logs.", List())
  val E00009 = (searchId: Integer) =>                                                       CustomException(9, searchId, "Subsearch was canceled. Check logs.", List())
  val E00010 = (searchId: Integer) =>                                                       CustomException(10, searchId, "Unknown status of finished subsearch. Check logs.", List())
  val E00011 = (searchId: Integer) =>                                                       CustomException(11, searchId, "Bad format of cache _SCHEME.", List())
  val E00012 = (searchId: Integer, commandName: String, notFoundString: String) =>          CustomException(12, searchId, f"Required argument(s) $notFoundString not found", List(commandName, notFoundString))
  val E00013 = (searchId: Integer, commandName: String, unknownKeysString: String) =>       CustomException(13, searchId, f"Unknown argument(s) $unknownKeysString for command '$commandName'",List(commandName, unknownKeysString))
  val E00014 = (searchId: Integer, commandName: String, exception: Throwable) =>            CustomException(14, searchId, f"Error in  '$commandName' command", exception, List(commandName, exception.getMessage))
  val E00015 = (searchId: Integer, className: String) =>                                    CustomException(15, searchId, s"Class with name '$className' is not found in classpath", List())
  val E00016 = (searchId: Integer, commandName: String) =>                                  CustomException(16, searchId, s"Command with name '$commandName' is not found", List())
  val E00017 = (searchId: Integer) =>                                                       CustomException(17, searchId, "Search was canceled because of timeout.", List())
  val E00018 = (searchId: Integer, commandName: String) =>                                  CustomException(18, searchId, s"Command $commandName requires 'bins' or 'span' argument", List(commandName,"'bins','span'"))
  val E00019 = (searchId: Integer, commandName: String) =>                                  CustomException(19, searchId, "Required argument 'index' not found",List(commandName, "index"))
  val E00020 = (searchId: Integer, commandName: String) =>                                  CustomException(20, searchId, s"Command $commandName shoud have at least one argument", List(commandName))
  val E00021 = (searchId: Integer) =>                                                       CustomException(21, searchId, "Parameter 'center' should have one of values: [true,false]", List())
  val E00022 = (searchId: Integer, commandName: String) =>                                  CustomException(22, searchId, s"Command $commandName shoud have at least one expression", List(commandName))

  /**
   * Exceptions defined for Interpreted Inlines Plugin
   */
  val E00023 = (searchId: Integer) =>                                                       CustomException(23, searchId, "Execution of Scala inline exceeded time limit", List())
  val E00024 = (searchId: Integer) =>                                                       CustomException(24, searchId, "Either Spark session could not be obtained or NullPointerException", List())
  val E00025 = (searchId: Integer) =>                                                       CustomException(25, searchId, "Returned value was not of DataFrame type", List())
  val E00026 = (searchId: Integer, message: String) =>                                      CustomException(26, searchId, s"Exception in Scala inline. Message:\n$message", List())
  val E00027 = (searchId: Integer, message: String) =>                                      CustomException(27, searchId, s"Unknown exception in Scala inline.\n$message", List())
  val E00028 = (searchId: Integer) =>                                                       CustomException(28, searchId, "collect_list, collect_set and java_method are not allowed", List())
  val E00029 = (searchId: Integer, message: String) =>                                      CustomException(29, searchId, s"Spark inline execution error:\n$message", List())
}
