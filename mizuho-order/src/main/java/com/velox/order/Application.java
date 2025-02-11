package com.velox.order;

import com.aralis.tools.support.SupportViewerScreenProvider;
import com.aralis.vm.ScreenProviderFactory;
import com.aralis.vm.SimpleScreenProviderFactory;
import com.caelo.application.ApplicationContext;
import com.caelo.application.ApplicationContextBuilder;
import com.caelo.application.VeloxCoreComponents;
import com.caelo.util.logging.Loggers;
import com.velox.app.api.InstanceInfoBuilder;
import com.velox.config.VeloxConfigModule;
import com.velox.order.api.Order;
import com.velox.tools.VeloxToolComponents;
import com.velox.tools.VeloxToolModule;
import com.velox.tools.ui.UserSettingScreenProvider;
import com.velox.web.VeloxWebComponents;
import com.velox.web.VeloxWebModule;
import com.velox.web.vertx.ContextRoot;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.file.FileSystemOptions;
import org.slf4j.Logger;
import java.time.Instant;

public class Application {
    private final static Logger s_log = Loggers.getLogger();

    public static void main(String[] args) {
        var instanceId = "dev";
        var instance = InstanceInfoBuilder.newBuilder().instanceId(instanceId).startTime(Instant.now()).get();

        var context = ApplicationContextBuilder.create()
          .install(new VeloxWebModule())
          .install(new VeloxToolModule())
          .install(new VeloxConfigModule())
          .register(VeloxCoreComponents.InstanceInfo, ctx -> instance)
          .register(VeloxCoreComponents.ScreenProviderFactory, Application::createScreenProviderFactory)
          .register(VeloxWebComponents.Vertx, Application::createVertx)
          .register(OrderInjector.class, Application::createOrderInjector, true)
          .get();

        var env = context.get(VeloxCoreComponents.VeloxEnvironment);

        var root = ContextRoot.create("/order").withContentSecurityPolicy("frame-ancestors", "http://localhost:6061");
        if (env.isDevelopment()) {
            root.addWebRoot("build/extracted-included-webapp/src/main/webapp");
        }

        context.get(VeloxWebComponents.WebServerBuilder).addPort(6062).addContextRoot(root).start();

        s_log.info("started instance {}, environment {}", instance.instanceId(), env.mode());
    }

    private static ScreenProviderFactory createScreenProviderFactory(ApplicationContext ctx) {
        return new SimpleScreenProviderFactory(
          new OrderScreenProvider("Order", "Velox", "fa-solid fa-desktop"),
          new SupportViewerScreenProvider(ctx.get(VeloxToolComponents.CachePublisherTracker),
            "Support Viewer",
            "Support",
            "fa-solid fa-phone"),
          new UserSettingScreenProvider("User Settings", "Configuration", "fa-solid fa-circle-user"));
    }

    private static Vertx createVertx(ApplicationContext ctx) {
        var env = ctx.get(VeloxCoreComponents.VeloxEnvironment);
        if (env.isDevelopment()) {
            var options = new VertxOptions();
            options.setFileSystemOptions(new FileSystemOptions().setClassPathResolvingEnabled(false));
            return Vertx.vertx(options);
        } else {
            return Vertx.vertx();
        }
    }

    private static OrderInjector createOrderInjector(ApplicationContext ctx) {
        var dc = ctx.get(VeloxCoreComponents.DataContextAccessor);
        return new OrderInjector(dc.getPublisher(Order.class), "order_za.csv");
    }
}
