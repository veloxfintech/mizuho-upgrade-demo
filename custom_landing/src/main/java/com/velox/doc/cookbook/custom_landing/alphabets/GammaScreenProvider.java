package com.velox.doc.cookbook.custom_landing.alphabets;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.SessionState;
import com.caelo.util.logging.Loggers;
import com.velox.doc.cookbook.custom_landing.AppScreenProvider;
import com.velox.doc.cookbook.custom_landing.GammaScreen;
import org.slf4j.Logger;

public class GammaScreenProvider extends AppScreenProvider<GammaScreen> {
    private final static Logger s_log = Loggers.getLogger();

    public GammaScreenProvider(String caption, String group, String icon, String url) {
        super(GammaScreen.class, caption, group, icon, url);
    }

    @Override
    public void create(SessionState state, ClientNotifier notifier) {
        s_log.info("GammaScreen created");
    }
}