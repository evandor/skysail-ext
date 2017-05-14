package io.skysail.ext.config.app

import java.security.Principal
import io.skysail.core.app.SkysailApplication
import io.skysail.api.responses.SkysailResponse

import collection.JavaConversions._
import io.skysail.restlet.resources.ListServerResource
import io.skysail.restlet.ResourceContextId
import io.skysail.restlet.resources.EntityServerResource
import io.skysail.restlet.resources.EntityServerResource
import io.skysail.api.doc._

class ConfigsResource extends ListServerResource[Config](classOf[ConfigResource]) { // 
  addToContext(ResourceContextId.LINK_TITLE, "list Configs");
  setDescription("provides the list of configurations for this installation")

  @ApiSummary("returns the list of OSGi-Admin-Configurations")
  @ApiDescription("some description")
  @ApiTags(Array("Config","Admin"))
  def getEntity(): List[Config] = getApplication().asInstanceOf[ConfigApplication].getConfigs().toList
  //override def getLinks() = super.getLinks(classOf[ConfigsResource])
}

class ConfigResource extends EntityServerResource[ConfigDetails] {
  override def getEntity(): ConfigDetails = getSkysailApplication().asInstanceOf[ConfigApplication].getConfig(getAttribute("id"))
  //override def getLinks() = super.getLinks(classOf[ConfigResource])
}