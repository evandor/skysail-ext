package io.skysail.ext.config.app

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.restlet.data.Protocol;
import io.skysail.core.app.SkysailApplication
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import io.skysail.core.app.ApplicationProvider
import io.skysail.server.menus.MenuItemProvider
import io.skysail.core.app.ApiVersion
import org.osgi.service.component.annotations.Activate
import org.osgi.service.component.ComponentContext
import io.skysail.core.app.ApplicationConfiguration
import org.restlet.data.Protocol
import io.skysail.server.restlet.RouteBuilder
import io.skysail.server.menus.MenuItem
import java.util.Arrays
import io.skysail.core.resources.SkysailServerResource
import org.osgi.service.cm.ConfigurationAdmin

import scala.collection.JavaConverters._
import io.skysail.core.app.ApplicationContextId

object ConfigApplication {
  final val APP_NAME = "configuration"
}

@Component(
  immediate = true,
  configurationPolicy = ConfigurationPolicy.OPTIONAL,
  service = Array(classOf[ApplicationProvider], classOf[MenuItemProvider]))
class ConfigApplication extends SkysailApplication(
  ConfigApplication.APP_NAME,
  new ApiVersion(int2Integer(1))) with MenuItemProvider {

  setDescription("facebook client")
  getConnectorService().getClientProtocols().add(Protocol.HTTPS)

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

  override def getMenuEntries(): java.util.List[MenuItem] = {
    val appMenu = new MenuItem(getName(), "/" + getName() + getApiVersion().getVersionPath());
    appMenu.setCategory(MenuItem.Category.ADMIN_MENU);
    return Arrays.asList(appMenu);
  }

  def getConfigs() = configAdmin.listConfigurations(null).map(x => new Config(x))
  def getConfig(pid: String): ConfigDetails = new ConfigDetails(configAdmin.getConfiguration(pid))
}