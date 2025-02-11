package com.velox.launcher;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.SessionState;
import com.caelo.vm.workspace.WorkspaceScreenProvider;
import com.velox.launcher.api.OrderLauncherScreen;

public class OrderLauncherScreenProvider extends WorkspaceScreenProvider<OrderLauncherScreen> {
    public OrderLauncherScreenProvider(String caption, String group, String icon) {
        super(OrderLauncherScreen.class, caption, group, icon);
    }

    @Override
    public OrderLauncherScreen createScreen(SessionState state, ClientNotifier notifier) {
        var screen = new OrderLauncherScreen(state);
        screen.title("Order");
        return screen;
    }
}
