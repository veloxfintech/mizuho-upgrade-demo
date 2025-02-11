package com.velox.order;

import com.aralis.vm.BaseScreenProvider;
import com.aralis.vm.ClientNotifier;
import com.aralis.vm.SessionState;
import com.velox.order.api.Order;
import com.velox.order.api.OrderScreen;

public class OrderScreenProvider extends BaseScreenProvider<OrderScreen> {
    public OrderScreenProvider(String caption, String group, String icon) {
        super(OrderScreen.class, caption, group, icon);
    }

    @Override
    public void create(SessionState state, ClientNotifier notifier) {
        var screen = new OrderScreen(state, state.getDataContextAccessor().getTable(Order.class));
        screen.title("Order");
        notifier.created(screen);
    }
}
