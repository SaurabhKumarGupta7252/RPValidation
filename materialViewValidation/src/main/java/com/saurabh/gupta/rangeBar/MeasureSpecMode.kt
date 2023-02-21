package com.saurabh.gupta.rangeBar

import android.view.View.MeasureSpec

/**
 * Created by Saurabh Gupta on 21/02/23.
 */
/**
 * Helper enum class for transforming a measureSpec mode integer value into a
 * human-readable String. The human-readable String is simply the name of the
 * enum value.
 */
enum class MeasureSpecMode  // Constructor /////////////////////////////////////////////////////////////
    (
    /**
     * Gets the int value associated with this mode.
     *
     * @return the int value associated with this mode
     */
    // Member Variables ////////////////////////////////////////////////////////
    val modeValue: Int
) {
    AT_MOST(MeasureSpec.AT_MOST), EXACTLY(MeasureSpec.EXACTLY), UNSPECIFIED(MeasureSpec.UNSPECIFIED);

    // Public Methods //////////////////////////////////////////////////////////
    companion object {
        /**
         * Gets the MeasureSpecMode value that corresponds with the given
         * measureSpec int value.
         *
         * @param measureSpec the measure specification passed by the platform to
         * [View.onMeasure]
         * @return the MeasureSpecMode that matches with that measure spec
         */
        fun getMode(measureSpec: Int): MeasureSpecMode? {
            val modeValue = MeasureSpec.getMode(measureSpec)
            for (mode in values()) {
                if (mode.modeValue == modeValue) {
                    return mode
                }
            }
            return null
        }
    }
}