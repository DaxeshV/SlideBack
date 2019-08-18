package com.phantomvk.slideback;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;

public class SlideStateAdapter extends BaseSlideStateAdapter {

    protected final Activity activity;

    /**
     * {@link #onBackPressed(Activity)} will be called before calling
     * {@link #finishActivity(Activity)} if this is true.
     * Create a new instance of {@link SlideStateAdapter} and set to true, default is false.
     */
    protected boolean enableOnBackPressed = false;

    public SlideStateAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSlideOverRange() {
        onBackPressed(activity);
        finishActivity(activity);
    }

    public void onBackPressed(@Nullable Activity activity) {
        if (activity == null || !enableOnBackPressed) return;
        activity.onBackPressed();
    }

    public void finishActivity(@Nullable Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        if (SDK_INT >= JELLY_BEAN_MR1 && activity.isDestroyed()) return;

        activity.finish();
        activity.overridePendingTransition(0, 0);
    }

    /**
     * Called {@link Activity#onBackPressed()} before finishing the activity.
     *
     * @param enable true to enable
     */
    public void enableOnBackPressed(boolean enable) {
        enableOnBackPressed = enable;
    }
}
