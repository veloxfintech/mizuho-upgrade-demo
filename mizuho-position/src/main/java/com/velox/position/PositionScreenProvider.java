package com.velox.position;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.ScreenProvider;
import com.aralis.vm.SessionState;
import com.velox.position.api.Position;
import com.velox.position.api.PositionScreen;

public class PositionScreenProvider implements ScreenProvider<PositionScreen> {
    @Override
    public String group() {
        return "Velox";
    }

    @Override
    public String caption() {
        return "Position Blotter";
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
        var screen = new PositionScreen(state, state.getDataContextAccessor().getTable(Position.class));
        notifier.created(screen);
    }
}
