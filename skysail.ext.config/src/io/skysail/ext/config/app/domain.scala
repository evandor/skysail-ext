package io.skysail.ext.config.app

import javax.validation.constraints.Size
import org.osgi.service.cm.Configuration
import scala.collection.JavaConversions._
import io.skysail.core.html.Field
import io.skysail.core.domain.ScalaEntity

class Config(c: Configuration) extends ScalaEntity[String] {
  @Field var id: Option[String] = None
  override def getId() = c.getPid
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