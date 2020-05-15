package com.mark.sharee.core

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.util.SparseArray
import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.mark.sharee.data.ShareeRepository
import com.mark.sharee.di.shareeApp
import com.mark.sharee.fcm.TokenManager
import com.mark.sharee.model.User
import com.mark.sharee.navigation.Arguments
import com.mark.sharee.navigation.Screen
import com.mark.sharee.navigation.arguments.ExercisesInfo
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.screens.MainScreen
import com.mark.sharee.screens.SignInScreen
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.util.*

class ShareeApplication : MultiDexApplication() {
    private val cachedComponents = SparseArray<ActivityComponent>()

    interface ActivityComponent

    override fun onCreate() {
        // Set locale has to be before super.onCreate
        setLocale(baseContext)
        super.onCreate()

        context = this

        startKoin {
            androidContext(this@ShareeApplication)
            androidLogger()
            modules(shareeApp)
        }

        Timber.plant(Timber.DebugTree())

        FirebaseAuth.getInstance().setLanguageCode("He")

        registerFragmentArguments()
        registerScreens()
        User.register(this)
    }

    private fun registerFragmentArguments() {
        Arguments.registerSubclass(TransferInfo::class.java)
        Arguments.registerSubclass(ExercisesInfo::class.java)
    }

    private fun registerScreens() {
        Screen.registerSubclass(MainScreen::class.java)
        Screen.registerSubclass(SignInScreen::class.java)
    }

    companion object {
        private var debug: Boolean? = null

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @JvmStatic
        fun registerActivityComponent(activity: Activity, component: ActivityComponent) {
            val application = activity.applicationContext as ShareeApplication
            application.cachedComponents.put(activity.hashCode(), component)
        }

        @JvmStatic
        fun unregisterActivityComponent(activity: Activity) {
            val application = activity.applicationContext as ShareeApplication
            application.cachedComponents.remove(activity.hashCode())
        }

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <T : ActivityComponent> getActivityComponent(activity: Activity): T? {
            val application = activity.applicationContext as ShareeApplication
            return application.cachedComponents.get(activity.hashCode()) as T?
        }

        @JvmStatic
        var isDebug: Boolean
            get() = debug!!
            protected set(debug) {
                ShareeApplication.debug = debug
            }

        @Suppress("DEPRECATION")
        @JvmStatic
        fun setLocale(context: Context) {
            val locale = Locale("iw", "IL")
            Locale.setDefault(locale)
            val configuration = Configuration()
            configuration.setLocale(locale)
            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        }
    }
}