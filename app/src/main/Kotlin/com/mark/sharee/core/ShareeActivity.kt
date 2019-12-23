package com.mark.sharee.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mark.sharee.navigation.FragmentNavigator
import org.koin.android.ext.android.inject

open class ShareeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        // Set locale has to be before super.onCreate
        ShareeApplication.setLocale(this)
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        ShareeApplication.setLocale(this)
        super.onStart()

    }
}
