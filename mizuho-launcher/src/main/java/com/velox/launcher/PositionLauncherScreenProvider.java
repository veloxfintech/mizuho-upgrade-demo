package com.velox.launcher;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.SessionState;
import com.caelo.vm.workspace.WorkspaceScreenProvider;
import com.velox.launcher.api.PositionLauncherScreen;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

public class PositionLauncherScreenProvider extends WorkspaceScreenProvider<PositionLauncherScreen> {
    public PositionLauncherScreenProvider(String caption, String group, String icon) {
        super(PositionLauncherScreen.class, caption, group, icon);
    }

    @Override
    public PositionLauncherScreen createScreen(SessionState state, ClientNotifier notifier) {
        var screen = new PositionLauncherScreen(state);
        var query = encodeQuery("windowType=CHILD&provider=com.velox.position.PositionScreenProvider");
        screen.m_url.setValue("http://localhost:6063/position/?" + query);
        screen.title("Position");
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
