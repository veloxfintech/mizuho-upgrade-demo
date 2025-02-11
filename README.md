In this demo, I prepared 4 separate projects: launcher, order, position and custom_landing.

launcher is the container application which embeds iframes of other 3 applications.

The webserver config of 4 applications as follows:

Launcher: http://localhost:6061/launcher

Order: http://localhost:6062/order 

Position: http://localhost:6063/position 

Custom Landing: http://localhost:6064/cookbook

![image](https://github.com/user-attachments/assets/a3473300-131a-48a1-8c7f-80614b8fc0f3)

# Order

For Order, it demonstrates the most basic setup.

**In launcher:**

OrderLauncherScreen.html

```html
<div class="OrderLauncherScreen vx-screen gap-1">
  <iframe class="vx-stretch" src="http://localhost:6062/order"></iframe>
</div>
```

**In Order application:**

Application.java

```java
var root = ContextRoot.create("/order").withContentSecurityPolicy("frame-ancestors", "http://localhost:6061");
context.get(VeloxWebComponents.WebServerBuilder).addPort(6062).addContextRoot(root).start();
```

(you can use ‘*’ to allow Order to be embedded in any other applications, here I restrict it to only embedded by launcher)

# Position

For Position, it demonstrates how to use query parameters to load application screen.

For more details you can checkout [https://portal.veloxfintech.com/doc/velox-docs/v4.0.0/velox_documentation/cookbook/integration/clickable_link.html](https://portal.veloxfintech.com/doc/velox-docs/v4.0.0/velox_documentation/cookbook/integration/clickable_link.html)

**In launcher:**

PositionLauncherScreen.html

```html
<div class="PositionLauncherScreen vx-screen gap-1">
  <iframe class="vx-stretch" :src="url.Value"></iframe>
</div>
```

CustomLandingLauncherScreenProvider.java

```java
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
```

**In Position application:**

PositionScreenProvider.java

```java
package com.velox.position;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.ScreenProvider;
import com.aralis.vm.SessionState;
import com.velox.position.api.PositionScreen;

public class PositionScreenProvider implements ScreenProvider<PositionScreen> {
    @Override
    public String group() {
        return "Velox";
    }

    @Override
    public String caption() {
        return "Position";
    }

    @Override
    public String icon() {
        return "fa-solid fa-phone";
    }

    @Override
    public String name() {
        return PositionScreenProvider.class.getName();
    }

    @Override
    public void create(SessionState state, ClientNotifier notifier) {
        notifier.created(new PositionScreen(state));
    }
}
```

<aside>❕This is one way to hide app bar. And you can open different screens by setting different provider parameters on url.</aside>

# Custom Landing

For Custom Landing, it demonstrates how to set custom landing screen.

![image](https://github.com/user-attachments/assets/19a3908f-46ce-4c60-b05f-ff33f0a350ab)

