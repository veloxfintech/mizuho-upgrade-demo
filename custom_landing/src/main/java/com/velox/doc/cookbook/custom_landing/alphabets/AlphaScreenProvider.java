package com.velox.doc.cookbook.custom_landing.alphabets;

import com.aralis.vm.ClientNotifier;
import com.aralis.vm.SessionState;
import com.caelo.util.logging.Loggers;
import com.velox.doc.cookbook.custom_landing.AppScreenProvider;
import com.velox.doc.cookbook.custom_landing.AlphaScreen;
import org.slf4j.Logger;

// tag::EnhancedScreenProviderExample[]
public class AlphaScreenProvider extends AppScreenProvider<AlphaScreen> {
    private final static Logger s_log = Loggers.getLogger();

    public AlphaScreenProvider(String caption, String group, String icon, String url) {
        super(AlphaScreen.class, caption, group, icon, url);
    }

    @Override
    public void create(SessionState state, ClientNotifier notifier) {
        s_log.info("AlphaScreen created");
    }
}
// end::EnhancedScreenProviderExample[]