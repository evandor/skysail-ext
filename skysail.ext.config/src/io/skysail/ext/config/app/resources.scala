package io.skysail.ext.config.app

import io.skysail.server.restlet.resources.EntityServerResource
import io.skysail.domain.GenericIdentifiable
import java.security.Principal
import io.skysail.core.app.SkysailApplication
import io.skysail.server.restlet.resources.ListServerResource
import io.skysail.api.responses.SkysailResponse
import io.skysail.server.queryfilter.filtering.Filter
import io.skysail.server.queryfilter.pagination.Pagination
import io.skysail.server.restlet.resources.{ PutEntityServerResource, PostEntityServerResource }
import io.skysail.server.ResourceContextId

import collection.JavaConversions._

class ConfigsResource extends ListServerResource[Config](classOf[ConfigResource]) { // 
  addToContext(ResourceContextId.LINK_TITLE, "list Configs");
  def getEntity(): java.util.List[Config] = getApplication().asInstanceOf[ConfigApplication].getConfigs().toSeq
  override def getLinks() = super.getLinks(classOf[ConfigsResource])
}

class ConfigResource extends EntityServerResource[ConfigDetails] {
  def getEntity(): ConfigDetails = getApplication().asInstanceOf[ConfigApplication].getConfig(getAttribute("id"))
  override def getLinks() = super.getLinks(classOf[ConfigResource])
}