package com.mark.sharee.activities

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.sharee.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.mark.sharee.core.ShareeActivity
import com.mark.sharee.core.SupportsOnBackPressed
import com.mark.sharee.data.ShareeRepository
import com.mark.sharee.model.User
import com.mark.sharee.mvvm.State
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.screens.GeneralPollsScreen
import com.mark.sharee.screens.SignInScreen
import com.mark.sharee.utils.Event
import com.mark.sharee.utils.FontManager
import com.mark.sharee.utils.Toaster
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber


class MainActivity : ShareeActivity() {

    private val shareeRepository: ShareeRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeFonts()
        initializeDrawer()
        navigator.replace(SignInScreen())

        initializeFCM()
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
            Toast.makeText(applicationContext, "בחרת " + item.title.toString(), Toast.LENGTH_SHORT)
                .show()

            when (item.itemId) {
                R.id.navGeneralPolls -> {
                    navigator.replace(GeneralPollsScreen(TransferInfo(flow = TransferInfo.Flow.GeneralPolls)))
                }
                R.id.navMedicalPolls -> {
                    navigator.replace(GeneralPollsScreen(TransferInfo(flow = TransferInfo.Flow.MedicalPolls)))
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

    private fun initializeFCM() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.i("$TAG - getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = getString(R.string.fcm_token)
                Timber.i("$TAG -  FCM token retrieved: [$token]")
                Toaster.show(baseContext, msg)

                User.me()?.let {
                    // if server already has the current token, we shall not bother updating it.
                    if (it.getFcmToken() == token) {
                        Timber.i("$TAG - server already has up to date fcm token")
                        return@let
                    }

                    if (token.isNullOrEmpty()) {
                        Timber.i("$TAG - Empty fcm token")
                    } else {
                        updateFcmToken(token, it.getToken())
                    }
                } ?: Timber.i("$TAG - User is null.")
            })
    }

    private fun updateFcmToken(fcmToken: String, verificationToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                Timber.i("$TAG - updateFcmToken - runCatching, token - $fcmToken")
                shareeRepository.updateFcmToken(verificationToken, fcmToken)
            }.onSuccess {
                Timber.i("$TAG - updateFcmToken - onSuccess, response = $it")
                User.me()?.setFcmToken(fcmToken)

            }.onFailure {
                Timber.e("$TAG - updateFcmToken - onFailure $it")
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}
