package com.mark.sharee.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mark.sharee.navigation.ActivityNavigator
import com.mark.sharee.navigation.FragmentNavigator
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

abstract class ShareeActivity : AppCompatActivity() {

    val navigator: ActivityNavigator = get()

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
