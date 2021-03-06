package com.mark.sharee.activities

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.sharee.R
import com.mark.sharee.core.Constants
import com.mark.sharee.core.ShareeActivity
import com.mark.sharee.core.SupportsOnBackPressed
import com.mark.sharee.fcm.NotificationUtil
import com.mark.sharee.model.User
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.screens.*
import com.mark.sharee.utils.FontManager
import com.mark.sharee.utils.Toaster
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.android.synthetic.main.poll_section_item.view.*
import timber.log.Timber


class MainActivity : ShareeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeFonts()
        initializeDrawer()

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

    //region drawer
    private fun initializeDrawer() {
        addDrawerListener()
    }

    private fun addDrawerListener() {
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {
                Timber.i("$TAG -  onDrawerOpened")
            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })

        navigationView.setNavigationItemSelectedListener { item ->
            Timber.i("$TAG -  onNavigationItemSelected - ${item.title}")

            when (item.itemId) {
                R.id.navDepartmentInfo -> {
                    navigator.replace(DepartmentInfoScreen())
                }
                R.id.navHelp -> {
                    navigator.replace(AboutScreen())
                }
                R.id.navSettings -> {
                    navigator.replace(SettingsScreen())
                }
            }
            drawerLayout.closeDrawers()

            true
        }
    }

    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun setDrawerTitle(title: String) {
        navigationView.getHeaderView(0).drawerTitle.text = title
    }

    //endregion

    override fun onBackPressed() {
        super.onBackPressed()
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (fragment != null && fragment is SupportsOnBackPressed) {
            if ((fragment as SupportsOnBackPressed).onBackPressed()) {
                return
            }
        }
        super.onBackPressed()
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}
