package com.mark.sharee.activities

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.sharee.R
import com.mark.sharee.core.ShareeActivity
import com.mark.sharee.screens.SignInScreen
import com.mark.sharee.utils.FontManager
import timber.log.Timber


class MainActivity : ShareeActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeFonts()

        navigator.replace(SignInScreen())
    }

    override fun onResume() {
        super.onResume()
        navigator.takeActivity(this)
    }

    override fun onPause() {
        super.onPause()
        navigator.dropActivity()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun initializeFonts() {
        FontManager.initialize(
            Typeface.createFromAsset(assets, "fonts/Orion-Regular.otf"),
            Typeface.createFromAsset(assets, "fonts/Orion-Bold.otf"),
            Typeface.createFromAsset(assets, "fonts/Orion-ExtraBold.otf"),
            Typeface.createFromAsset(assets, "fonts/Orion-Black.otf"),
            Typeface.createFromAsset(assets, "fonts/ClanOffc-Ultra.ttf"),
            Typeface.createFromAsset(assets, "fonts/ClanOT-Ultra.otf")
        )
    }
}
