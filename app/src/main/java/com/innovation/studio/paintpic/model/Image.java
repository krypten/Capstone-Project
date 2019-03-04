package com.innovation.studio.paintpic.model;

import android.graphics.drawable.Drawable;

/**
 * Image model to store image information.
 *
 * @author Chaitanya Agrawal
 */
public class Image {
    public Drawable mUrl;

    public Image(final Drawable url) {
        mUrl = url;
    }

    public Drawable getUrl() {
        return mUrl;
    }
}
