
package com.example.uhc;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class MyGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(
            @NonNull Context context,
            @NonNull GlideBuilder builder) {

        // Limit Glide's memory cache to 20 MB
        builder.setMemoryCache(new LruResourceCache(20 * 1024 * 1024));

        // Limit Glide's bitmap pool to 10 MB
        builder.setBitmapPool(new LruBitmapPool(10 * 1024 * 1024));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}

