package com.velox.doc.cookbook.custom_landing.alphabets;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.SessionState;
import com.caelo.util.logging.Loggers;
import com.velox.doc.cookbook.custom_landing.AppScreenProvider;
import com.velox.doc.cookbook.custom_landing.BetaScreen;
import org.slf4j.Logger;

public class BetaScreenProvider extends AppScreenProvider<BetaScreen> {
    private final static Logger s_log = Loggers.getLogger();

    public BetaScreenProvider(String caption, String group, String icon, String url) {
        super(BetaScreen.class, caption, group, icon, url);
    }

    @Override
    public void create(SessionState state, ClientNotifier notifier) {
        s_log.info("BetaScreen created");
    }
}