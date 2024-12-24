package com.velox.app2;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.SessionState;
import com.caelo.vm.workspace.WorkspaceScreenProvider;
import com.velox.app2.api.App2Screen;

public class App2ScreenProvider extends WorkspaceScreenProvider<App2Screen> {
    public App2ScreenProvider( String caption, String group, String icon) {
        super(App2Screen.class, caption, group, icon);
    }

    @Override
    public App2Screen createScreen(SessionState state, ClientNotifier notifier) {
        return new App2Screen(state);
    }
}
