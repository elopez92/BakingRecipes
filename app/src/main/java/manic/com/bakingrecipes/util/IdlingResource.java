package manic.com.bakingrecipes.util;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource.ResourceCallback;

import com.jakewharton.espresso.OkHttp3IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.OkHttpClient;

public abstract class IdlingResource implements androidx.test.espresso.IdlingResource {

    @Nullable private volatile ResourceCallback mCallback;

    // Idleness is controlled with this boolean
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    public static void registerOkHttp(OkHttpClient client) {
        IdlingRegistry.getInstance().register(OkHttp3IdlingResource.create("okhttp", client));
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }
}
