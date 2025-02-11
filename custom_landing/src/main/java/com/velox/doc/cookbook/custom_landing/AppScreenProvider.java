package com.velox.doc.cookbook.custom_landing;

import com.aralis.vm.BaseScreenProvider;
import com.aralis.vm.Screen;

// tag::AppScreenProvider[]
public abstract class AppScreenProvider<T extends Screen> extends BaseScreenProvider<T> {
    protected final String imageUrl;

    public AppScreenProvider(Class<T> screenType, String caption, String group, String icon, String imageUrl) {
        super(screenType, caption, group, icon);
        this.imageUrl = imageUrl;
    }

    public String imageUrl() {
        return imageUrl;
    }
}
// end::AppScreenProvider[]