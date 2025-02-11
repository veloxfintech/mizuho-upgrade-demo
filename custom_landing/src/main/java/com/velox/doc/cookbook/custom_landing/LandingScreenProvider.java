package com.velox.doc.cookbook.custom_landing;

import com.aralis.tools.util.OKCancelHelper;
import com.aralis.vm.ClientNotifier;
import com.aralis.vm.DependantScreenHelper;
import com.aralis.vm.ScreenProviderFactory;
import com.aralis.vm.SessionState;
import com.caelo.application.ScreenFactory;
import com.velox.web.landing.AppBarScreenProvider;
import com.velox.web.landing.LandingScreenManager;
import com.velox.web.landing.api.AppBarScreen;

public class LandingScreenProvider implements ScreenFactory<LandingScreen> {
    private final ScreenProviderFactory m_factory;

    public LandingScreenProvider(final ScreenProviderFactory factory) {
        m_factory = factory;
    }

    @Override
    public LandingScreen createScreen(SessionState state, ClientNotifier notifier, String name) {
        LandingScreenManager manager = new LandingScreenManager(state, notifier);
        LandingScreen landing = new LandingScreen(state);
        AppBarScreen appbar = new AppBarScreenProvider(manager).createScreen(state, notifier, "appbar");
        LaunchpadScreen launchpad =
          new LaunchpadScreenProvider(m_factory, manager).createScreen(state, notifier, "launchpad");
        DashboardScreen dashboard = new DashboardScreenProvider().createScreen(state, notifier, "dashboard");

        var helper = new OKCancelHelper(notifier, landing);
        landing.m_infoButton.addListener(t -> helper.show("Info Button clicked"));
        landing.m_launchpadVisible.bind(manager.launchpadVisible());

        DependantScreenHelper dsh = new DependantScreenHelper(notifier, landing);
        dsh.add(appbar);
        dsh.add(dashboard);
        dsh.add(launchpad);
        return landing;
    }
}