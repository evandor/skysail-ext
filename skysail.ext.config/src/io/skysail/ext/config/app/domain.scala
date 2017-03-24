package io.skysail.ext.config.app

import io.skysail.domain.Entity
import io.skysail.domain.html.Field
import io.skysail.domain.html.InputType
import javax.validation.constraints.Size
import org.osgi.service.cm.Configuration
import scala.collection.JavaConversions._

class Config(c: Configuration) extends Entity {
  @Field var id: String = null
  def getId(): String = c.getPid
}

class ConfigDetails(c: Configuration) extends Config(c) {
  @Field var properties: String = ""
  def getProperties(): String = c.getProperties.keys().map {
    key => 
      if (key.contains("password")) {
         "<b>" + key + "</b>: ******"
      } else {
         "<b>" + key + "</b>: " + c.getProperties().get(key).toString()
      }
  }.mkString("<br>\n")

}