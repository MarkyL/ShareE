package com.mark.sharee.core

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mark.sharee.navigation.ActivityNavigator
import com.mark.sharee.navigation.FragmentNavigator
import com.mark.sharee.utils.FontStyle
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import java.util.prefs.Preferences

abstract class ShareeActivity : AppCompatActivity() {

    val navigator: ActivityNavigator = get()
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // Set locale has to be before super.onCreate
        ShareeApplication.setLocale(this)
        theme.applyStyle(com.mark.sharee.utils.Preferences(this).fontStyle.resId, true)
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        ShareeApplication.setLocale(this)
        super.onStart()

    }
}
