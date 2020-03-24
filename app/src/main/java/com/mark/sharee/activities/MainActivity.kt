package com.mark.sharee.activities

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.sharee.R
import com.google.android.material.navigation.NavigationView
import com.mark.sharee.core.DrawerPresenter
import com.mark.sharee.core.ShareeActivity
import com.mark.sharee.screens.SignInScreen
import com.mark.sharee.utils.FontManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import timber.log.Timber


class MainActivity : ShareeActivity() {

    private val drawerPresenter: DrawerPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeFonts()

        drawerPresenter.initialize()
        addDrawerListener()

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
    private fun addDrawerListener() {
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {
                Timber.i("onDrawerOpened")
            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })

        navigationView.setNavigationItemSelectedListener { item ->
            Timber.i("onNavigationItemSelected - ${item.title}")
            Toast.makeText(
                applicationContext,
                "בחרת " + item.title.toString(),
                Toast.LENGTH_SHORT
            ).show()
            drawerLayout.closeDrawers()

            true
        }


        drawerPresenter.onHomeClick()
    }

    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    //endregion

    override fun onDestroy() {
        super.onDestroy()
        drawerPresenter.unInitialize()
    }
}
