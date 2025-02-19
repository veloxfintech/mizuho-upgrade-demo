package com.velox.launcher;

import com.aralis.tools.support.SupportViewerScreenProvider;
import com.aralis.vm.ScreenProviderFactory;
import com.aralis.vm.SimpleScreenProviderFactory;
import com.caelo.application.ApplicationContext;
import com.caelo.application.ApplicationContextBuilder;
import com.caelo.application.VeloxCoreComponents;
import com.caelo.util.logging.Loggers;
import com.velox.app.api.InstanceInfoBuilder;
import com.velox.config.VeloxConfigModule;
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
          .get();

        var env = context.get(VeloxCoreComponents.VeloxEnvironment);

        var root = ContextRoot.create("/launcher");
        if (env.isDevelopment()) {
            root.addWebRoot("build/extracted-included-webapp/src/main/webapp");
        }

        context.get(VeloxWebComponents.WebServerBuilder).addPort(6061).addContextRoot(root).start();

        s_log.info("started instance {}, environment {}", instance.instanceId(), env.mode());
    }

    private static ScreenProviderFactory createScreenProviderFactory(ApplicationContext ctx) {
        return new SimpleScreenProviderFactory(new OrderLauncherScreenProvider("Order", "Mizuho", "fa-solid fa-desktop"),
          new PositionLauncherScreenProvider("Position", "Mizuho", "fa-solid fa-desktop"),
          new CustomLandingLauncherScreenProvider("Custom Landing", "Mizuho", "fa-solid fa-desktop"),
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
}
