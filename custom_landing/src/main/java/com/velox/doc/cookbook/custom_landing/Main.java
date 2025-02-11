package com.velox.doc.cookbook.custom_landing;

import com.aralis.tools.support.SupportViewerScreenProvider;
import com.aralis.vm.ScreenProviderFactory;
import com.aralis.vm.SimpleScreenProviderFactory;
import com.caelo.application.ApplicationContext;
import com.caelo.application.ApplicationContextBuilder;
import com.caelo.application.VeloxCoreComponents;
import com.caelo.util.logging.Loggers;
import com.velox.app.api.InstanceInfoBuilder;
import com.velox.config.VeloxConfigModule;
import com.velox.doc.cookbook.custom_landing.alphabets.*;
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

public class Main {
    private final static Logger s_log = Loggers.getLogger();

    public static void main(String[] args) {
        var instanceId = "dev";
        var instance = InstanceInfoBuilder.newBuilder().instanceId(instanceId).startTime(Instant.now()).get();

        var context = ApplicationContextBuilder.create()
          .install(new VeloxWebModule())
          .install(new VeloxToolModule())
          .install(new VeloxConfigModule())
          .register(VeloxCoreComponents.InstanceInfo, ctx -> instance)
          .register(VeloxCoreComponents.ScreenProviderFactory, Main::createScreenProviderFactory)
          .register(VeloxWebComponents.Vertx, Main::createVertx)
          .get();

        var env = context.get(VeloxCoreComponents.VeloxEnvironment);
        var root = ContextRoot.create("/cookbook").withContentSecurityPolicy("frame-ancestors", "http://localhost:6061");
        var providers = context.get(VeloxCoreComponents.ScreenProviderFactory);
        context.get(VeloxCoreComponents.SessionStateFactory)
          .register(VeloxCoreComponents.LandingScreenProvider, (state) -> new LandingScreenProvider(providers));
        context.get(VeloxWebComponents.WebServerBuilder).addPort(6064).addContextRoot(root).start();

        s_log.info("started instance {}, environment {}", instance.instanceId(), env.mode());
    }

    // tag::AddScreensToScreenProviderFactory[]
    private static ScreenProviderFactory createScreenProviderFactory(ApplicationContext ctx) {
        return new SimpleScreenProviderFactory(
          new StarterScreenProvider(
            "Starter",
            "Velox",
            "fa-solid fa-desktop",
            ctx.get(VeloxCoreComponents.DataContextAccessor).getPublisher(User.class)),
          new SupportViewerScreenProvider(ctx.get(VeloxToolComponents.CachePublisherTracker),
            "Support Viewer",
            "Velox",
            "fa-solid fa-phone"),
          new UserSettingScreenProvider("User Settings", "Velox", "fa-solid fa-circle-user"),

          new AlphaScreenProvider("Messages", "Applications", "fa-solid fa-envelope", "logo/messages_icon.svg"),
          new BetaScreenProvider("Settings", "Applications", "fa-solid fa-gears", "logo/settings_icon.svg"),
          new GammaScreenProvider("OrderBook", "Applications", "fa-solid fa-book", "logo/orderbook_icon.svg"),
          new DeltaScreenProvider("Chart", "Applications", "fa-solid fa-chart-simple", "logo/chart_icon.svg"),
          new EpsilonScreenProvider("Help", "Applications", "fa-solid fa-circle-info", "logo/support_icon.svg"));
    }
    // end::AddScreensToScreenProviderFactory[]

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