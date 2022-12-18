package com.fikri.netplix.core.helper

import android.content.Context

object DimensManipulation {
    fun dpToPx(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}