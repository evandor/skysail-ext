package io.skysail.ext.config.app

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations._;
import org.restlet.data.Protocol;
import java.util.Arrays
import org.osgi.service.cm.ConfigurationAdmin
import io.skysail.core.app.SkysailApplication
import scala.collection.JavaConverters._
import io.skysail.core.app.ApplicationProvider
import io.skysail.core.ApiVersion
import io.skysail.core.app.ApplicationConfiguration
import io.skysail.restlet.RouteBuilder
import io.skysail.core.model.APPLICATION_CONTEXT_RESOURCE

object ConfigApplication {
  final val APP_NAME = "configuration"
}

@Component(
  immediate = true,
  configurationPolicy = ConfigurationPolicy.OPTIONAL,
  service = Array(classOf[ApplicationProvider]))
class ConfigApplication extends SkysailApplication(
  ConfigApplication.APP_NAME,
  new ApiVersion(int2Integer(1))) {

  setDescription("configuration application")
  getConnectorService().getClientProtocols().add(Protocol.HTTPS)
  
  addAssociatedResourceClasses(List((APPLICATION_CONTEXT_RESOURCE, classOf[ConfigsResource])))


  @Reference
  var configAdmin: ConfigurationAdmin = null

  @Activate
  override def activate(appConfig: ApplicationConfiguration, componentContext: ComponentContext) = {
    super.activate(appConfig, componentContext);
  }

  override def attach() = {
    router.attach(new RouteBuilder("", classOf[ConfigsResource]));
    router.attach(new RouteBuilder("/configs", classOf[ConfigsResource]));
    router.attach(new RouteBuilder("/configs/{id}", classOf[ConfigResource]));
  }

//  override def getMenuEntries(): java.util.List[MenuItem] = {
//    val appMenu = new MenuItem(getName(), "/" + getName() + getApiVersion().getVersionPath());
//    appMenu.setCategory(MenuItem.Category.ADMIN_MENU);
//    return Arrays.asList(appMenu);
//  }

  def getConfigs() = configAdmin.listConfigurations(null).map(x => new Config(x))
  def getConfig(pid: String): ConfigDetails = new ConfigDetails(configAdmin.getConfiguration(pid))
}