package com.mark.sharee.utils

import android.view.View

class Tools {
    companion object {

        public fun toggleArrow(show: Boolean, view: View): Boolean {
            return toggleArrow(show, view, true)
        }

        public fun toggleArrow(show: Boolean, view: View, delay: Boolean): Boolean {
            return if (show) {
                view.animate().setDuration((if (delay) 200 else 0).toLong()).rotation(180f)
                true
            } else {
                view.animate().setDuration((if (delay) 200 else 0).toLong()).rotation(0f)
                false
            }
        }
    }
}