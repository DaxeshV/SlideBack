package com.phantomvk.slideback;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.phantomvk.slideback.listener.SlideStateAdapter;
import com.phantomvk.slideback.listener.SlideStateListener;
import com.phantomvk.slideback.utility.ViewDragHelper;

public class SlideManager {
    /**
     * The target activity to control.
     */
    protected Activity activity;

    /**
     * SlideLayout.
     */
    protected SlideLayout slideLayout;

    /**
     * Default constructor, use default SlideStateListener to finish activity.
     *
     * @param activity Activity
     */
    public SlideManager(@NonNull Activity activity) {
        this(activity, null);
    }

    private Conductor conductor;

    /**
     * Constructor, default SlideStateListener will be used if listener is null.
     *
     * @param activity Activity
     * @param listener SlideStateListener
     */
    public SlideManager(@NonNull Activity activity, @Nullable SlideStateListener listener) {
        Conductor c = (activity instanceof Conductor) ? (Conductor) activity : null;
        if (c != null && c.slideBackDisable()) return;

        this.activity = activity;
        this.conductor = c;
        listener = (listener == null) ? new SlideStateAdapter(activity) : listener;

        slideLayout = new SlideLayout(activity);
        slideLayout.setTrackingEdge(ViewDragHelper.EDGE_LEFT);
        slideLayout.addListener(listener);
    }

    public void onPostCreate() {
        if (conductor != null && conductor.slideBackDisable()) return;
        slideLayout.attach(activity);
//        TranslucentHelper.setTranslucent(activity);
    }

    /**
     * Return the instance of {@link SlideLayout}, maybe null or caused NullPointerException
     * when the value returned from {@link Conductor#slideBackDisable()} is true.
     *
     * @return the instance of SlideLayout, null pointer or caused NullPointerException
     */
    @Nullable
    public SlideLayout getSlideLayout() {
        return slideLayout;
    }

    /**
     * Used to totally disable SlideLayout.
     * <p>
     * Usage: implemented {@link Conductor} by the subclass of Activity which is using, then
     * override method {@link Conductor#slideBackDisable()} to return true.
     * <p>
     * As a result, this class will not be initialized, any result returned from its methods
     * is null or just caused NullPointerException.
     * <p>
     * Re-initializing is illegal even changing the value returned from {@link #slideBackDisable()}
     * from true to false after invoking {@link SlideManager#onPostCreate()}.
     * <p>
     * If just temporarily disable sliding, use {@link SlideLayout#setEnable(boolean)} of the
     * instance which returned from {@link SlideManager#getSlideLayout()}, and do not need to
     * implement {@link Conductor#slideBackDisable()}, or implement but return false permanently.
     */
    public interface Conductor {
        boolean slideBackDisable();
    }

    public static void setWindowBackground(@Nullable Activity activity) {
        if (activity == null) return;
        activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        activity.getWindow().getDecorView().setBackgroundDrawable(null);
    }
}
