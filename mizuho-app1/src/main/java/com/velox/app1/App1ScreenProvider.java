package com.velox.app1;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.SessionState;
import com.caelo.vm.workspace.WorkspaceScreenProvider;
import com.velox.app1.api.App1Screen;

public class App1ScreenProvider extends WorkspaceScreenProvider<App1Screen> {
    public App1ScreenProvider(String caption, String group, String icon) {
        super(App1Screen.class, caption, group, icon);
    }

    @Override
    public App1Screen createScreen(SessionState state, ClientNotifier notifier) {
        return new App1Screen(state);
    }
}
