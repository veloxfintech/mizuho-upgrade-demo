package com.velox.launcher;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.SessionState;
import com.caelo.vm.workspace.WorkspaceScreenProvider;
import com.velox.launcher.api.CustomLandingLauncherScreen;

public class CustomLandingLauncherScreenProvider extends WorkspaceScreenProvider<CustomLandingLauncherScreen> {
    public CustomLandingLauncherScreenProvider(String caption, String group, String icon) {
        super(CustomLandingLauncherScreen.class, caption, group, icon);
    }

    @Override
    public CustomLandingLauncherScreen createScreen(SessionState state, ClientNotifier notifier) {
        var screen = new CustomLandingLauncherScreen(state);
        screen.title("Custom Landing");
        return screen;
    }
}
