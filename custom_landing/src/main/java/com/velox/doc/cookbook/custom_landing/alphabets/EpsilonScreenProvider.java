package com.velox.doc.cookbook.custom_landing.alphabets;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.SessionState;
import com.caelo.util.logging.Loggers;
import com.velox.doc.cookbook.custom_landing.AppScreenProvider;
import com.velox.doc.cookbook.custom_landing.EpsilonScreen;
import org.slf4j.Logger;

public class EpsilonScreenProvider extends AppScreenProvider<EpsilonScreen> {
    private final static Logger s_log = Loggers.getLogger();

    public EpsilonScreenProvider(String caption, String group, String icon, String url) {
        super(EpsilonScreen.class, caption, group, icon, url);
    }

    @Override
    public void create(SessionState state, ClientNotifier notifier) {
        s_log.info("EpsilonScreen created");
    }
}