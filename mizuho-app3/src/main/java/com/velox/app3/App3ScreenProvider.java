package com.velox.app3;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.ScreenProvider;
import com.aralis.vm.SessionState;
import com.velox.app3.api.App3Screen;

public class App3ScreenProvider implements ScreenProvider<App3Screen> {
    @Override
    public String group() {
        return "Velox";
    }

    @Override
    public String caption() {
        return name();
    }

    @Override
    public String icon() {
        return "fa-solid fa-phone";
    }

    @Override
    public String name() {
        return App3ScreenProvider.class.getName();
    }

    @Override
    public void create(SessionState state, ClientNotifier notifier) {
        notifier.created(new App3Screen(state));
    }
}
