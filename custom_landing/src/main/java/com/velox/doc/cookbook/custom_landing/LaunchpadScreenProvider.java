package com.velox.doc.cookbook.custom_landing;

import com.aralis.vm.*;
import com.caelo.application.ScreenFactory;
import com.velox.web.landing.LandingScreenManager;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class LaunchpadScreenProvider implements ScreenFactory<LaunchpadScreen> {
    private final ScreenProviderFactory m_factory;
    private final LandingScreenManager m_manager;

    public LaunchpadScreenProvider(final ScreenProviderFactory factory, final LandingScreenManager manager) {
        m_manager = manager;
        m_factory = factory;
    }

    @Override
    public LaunchpadScreen createScreen(SessionState state, ClientNotifier notifier, String name) {
        var screen = new LaunchpadScreen(name, state);
        var dsh = new DependantScreenHelper(notifier, screen);
        var items = new ArrayList<Integer>();
        for (var provider : m_factory.get()) {
            var item = createItem(state, notifier, provider);
            items.add(item.controlId().screenId());
            dsh.add(item);
        }
        screen.m_items.setOptions(items);
        screen.m_showFavorites.setValue(true);
        return screen;
    }

    private LaunchpadItemScreen createItem(SessionState state, ClientNotifier notifier, ScreenProvider<?> provider) {
        var screen = new LaunchpadItemScreen(provider.name(), state);
        screen.m_name.setValue(provider.name());
        screen.m_icon.setValue(provider.icon());
        screen.m_group.setValue(provider.group());
        screen.m_caption.setValue(StringUtils.defaultIfEmpty(provider.caption(), provider.name()));

        if (provider instanceof AppScreenProvider<?>) {
            screen.m_imageUrl.setValue(((AppScreenProvider<?>) provider).imageUrl());
        }

        screen.m_open.addListener(() -> {
            provider.create(state, notifier);
            m_manager.launchpadVisible().set(false);
        });

        return screen;
    }
}