In this demo, I prepared 4 separate projects: launcher, order, position and custom_landing.

launcher is the container application which embeds iframes of other 3 applications.

The webserver config of 4 applications as follows:

launcher: http://localhost:6061/launcher

app1: http://localhost:6062/order 

app2: http://localhost:6063/position 

app3: http://localhost:6064/cookbook

![image](https://github.com/user-attachments/assets/f6a76f24-7647-4d64-8570-91c4f0d346e8)

# Order

For app1, it demonstrates the most basic setup.

**In launcher:**

OrderLauncherScreen.html

```html
<div class="OrderLauncherScreen vx-screen gap-1">
  <iframe class="vx-stretch" src="http://localhost:6062/order"></iframe>
</div>
```

**In order application:**

Application.java

```java
var root = ContextRoot.create("/order").withContentSecurityPolicy("frame-ancestors", "http://localhost:6061");
context.get(VeloxWebComponents.WebServerBuilder).addPort(6062).addContextRoot(root).start();
```

(you can use ‘*’ to allow app1 to be embedded in any other applications, here I restrict it to only embedded by launcher)

![image 1](https://github.com/user-attachments/assets/e854ec5a-1e96-49c2-a37d-01c4537690f1)

![image 2](https://github.com/user-attachments/assets/f1632812-ff5f-4885-82c6-5e0f69f603bf)

# App2

For app2, it demonstrates how to replace landing screen of App2 to actual application screen.

**In launcher:**

App2LauncherScreen.html

```html
<div class="App2LauncherScreen vx-screen gap-1">
  <iframe class="vx-stretch" src="http://localhost:6063/app2"></iframe>
</div>
```

**In app2:**

Application.java

```java
var landingScreenProvider = new App2ScreenProvider("App2", "Velox", "fa-solid fa-desktop");
var stateFactory = context.get(VeloxCoreComponents.SessionStateFactory);
stateFactory.register(VeloxCoreComponents.LandingScreenProvider, state -> landingScreenProvider);
...
var root = ContextRoot.create("/app2").withContentSecurityPolicy("frame-ancestors", "http://localhost:6061");
context.get(VeloxWebComponents.WebServerBuilder).addPort(6063).addContextRoot(root).start();
```

![image 3](https://github.com/user-attachments/assets/b2e3806b-8b35-437c-856d-4c38b5152f8c)


<aside>❕This is one way to hide the app bar, since the app bar belongs to ClassicLandingScreen, and you override the landing screen with actual application screen. But this method only applies if you only have 1 screen in app2, since you can’t switch to other screens via launchpad.</aside>

# App3

For app3, it demonstrates how to use query parameters to load application screen.

For more details you can checkout [https://portal.veloxfintech.com/doc/velox-docs/v4.0.0/velox_documentation/cookbook/integration/clickable_link.html](https://portal.veloxfintech.com/doc/velox-docs/v4.0.0/velox_documentation/cookbook/integration/clickable_link.html)

**In launcher:**

App3LauncherScreen.html

```html
<div class="App3LauncherScreen vx-screen gap-1">
  <iframe class="vx-stretch" :src="url.Value"></iframe>
</div>
```

App3LauncherScreenProvider.java

```java
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
```

**In app3:**

App3ScreenProvider.java

```java
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
```

![image 4](https://github.com/user-attachments/assets/81d5b091-5875-4db1-9f6f-6c4f57c21bc9)


<aside>❕This is another way to hide app bar. And you can open different screens by setting different provider parameters on url.</aside>
