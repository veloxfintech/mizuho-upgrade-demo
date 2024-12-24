package com.velox.launcher;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.SessionState;
import com.caelo.vm.workspace.WorkspaceScreenProvider;
import com.velox.launcher.api.App2LauncherScreen;

public class App2LauncherScreenProvider extends WorkspaceScreenProvider<App2LauncherScreen> {
    public App2LauncherScreenProvider(String caption, String group, String icon) {
        super(App2LauncherScreen.class, caption, group, icon);
    }

    @Override
    public App2LauncherScreen createScreen(SessionState state, ClientNotifier notifier) {
        return new App2LauncherScreen(state);
    }
}
