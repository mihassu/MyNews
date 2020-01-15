package ru.mihassu.mynews.ui.news;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.motion.widget.MotionLayout;

import static ru.mihassu.mynews.Utils.logIt;

public class AnimatedNewsCard extends MotionLayout implements VerticalMotionListener {

    private float maxOffset = minStartOffset;
    private float interpolationLength = 0f;
    private boolean isShowProgress = false;

    public AnimatedNewsCard(Context context) {
        super(context);
    }

    public AnimatedNewsCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedNewsCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, 0);
    }

    @Override
    public void onOffsetChanged(float offset) {
        int height = getHeight();

        // Для элементов списка с индексом больше 0
        if (offset > maxOffset) {
            maxOffset = offset;
        }

        interpolationLength = maxOffset + height;

        if (interpolationLength != 0f && offset >= -height) {
            setProgress((interpolationLength - ((float) height + offset)) / interpolationLength);
        }

        // Debug mode
        if (isShowProgress) {
            showProgress(offset);
        }
    }

    @Override
    public void onUpdateMaxOffset(float maxOffset) {
        this.maxOffset = maxOffset;
        this.interpolationLength = maxOffset + getHeight();
    }

    /**
     * Debug
     */
    @Override
    public void setDebugFlag(boolean flag) {
        isShowProgress = flag;
    }

    /**
     * Debug
     */
    @Override
    public void showProgress(float offset) {
        String itemHash = Integer.toHexString(hashCode());
        String interpolation = Float.toString(interpolationLength);
        String maxOff = Float.toString(maxOffset);
        String off = Float.toString(offset);
        String height = Integer.toString(getHeight());
        String progr = Float.toString(getProgress());

        String message = String
                .format("%s: interpolationLength=%s, maxOffset=%s, offset=%s, height=%s, progress=%s",
                        itemHash,
                        interpolation,
                        maxOff,
                        off,
                        height,
                        progr);

        logIt(message);
    }
}
