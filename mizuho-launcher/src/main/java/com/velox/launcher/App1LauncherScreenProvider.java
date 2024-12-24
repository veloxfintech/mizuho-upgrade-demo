package com.velox.launcher;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.SessionState;
import com.caelo.vm.workspace.WorkspaceScreenProvider;
import com.velox.launcher.api.App1LauncherScreen;

public class App1LauncherScreenProvider extends WorkspaceScreenProvider<App1LauncherScreen> {
    public App1LauncherScreenProvider(String caption, String group, String icon) {
        super(App1LauncherScreen.class, caption, group, icon);
    }

    @Override
    public App1LauncherScreen createScreen(SessionState state, ClientNotifier notifier) {
        return new App1LauncherScreen(state);
    }
}
