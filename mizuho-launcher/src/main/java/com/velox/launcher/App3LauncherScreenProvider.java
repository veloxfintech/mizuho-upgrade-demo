package com.velox.launcher;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.SessionState;
import com.caelo.vm.workspace.WorkspaceScreenProvider;
import com.velox.launcher.api.App3LauncherScreen;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

public class App3LauncherScreenProvider extends WorkspaceScreenProvider<App3LauncherScreen> {
    public App3LauncherScreenProvider(String caption, String group, String icon) {
        super(App3LauncherScreen.class, caption, group, icon);
    }

    @Override
    public App3LauncherScreen createScreen(SessionState state, ClientNotifier notifier) {
        var screen = new App3LauncherScreen(state);
        var query = encodeQuery("windowType=CHILD&provider=com.velox.app3.App3ScreenProvider");
        screen.m_url.setValue("http://localhost:6064/app3/?" + query);
        return screen;
    }

    private String encodeQuery(String rawQuery) {
        var base64 = Base64.getEncoder().encodeToString(rawQuery.getBytes(StandardCharsets.UTF_8));
        var encoded = base64.replaceAll(Pattern.quote("+"), "-")
          .replaceAll(Pattern.quote("/"), "_")
          .replaceAll(Pattern.quote("="), ".");
        return String.format("p=%s", encoded);
    }
}
