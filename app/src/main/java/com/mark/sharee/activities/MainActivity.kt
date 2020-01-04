package com.mark.sharee.activities

import android.os.Bundle
import com.example.sharee.R
import com.mark.sharee.core.ShareeActivity
import com.mark.sharee.screens.SignInScreen
import timber.log.Timber

class MainActivity : ShareeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.i("mark - onCreate")
        navigator.replace(SignInScreen())
    }

    override fun onResume() {
        super.onResume()
        Timber.d("mark - onResume")
        navigator.takeActivity(this)

    }

    override fun onPause() {
        super.onPause()
        navigator.dropActivity()
    }


}
