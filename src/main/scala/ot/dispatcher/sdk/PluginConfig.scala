package ot.dispatcher.sdk

import com.typesafe.config.Config

trait PluginConfig {
  //Config witch used to get properties from plugin configuration (plugin.conf)
  def pluginConfig: Config

  //Config witch used to get properties from main application configuration (application.conf)
  def mainConfig: Config
}
