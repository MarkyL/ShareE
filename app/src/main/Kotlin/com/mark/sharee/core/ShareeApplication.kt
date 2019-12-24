package com.mark.sharee.core

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.util.SparseArray
import androidx.multidex.MultiDexApplication
import com.mark.sharee.di.navigatorModule
import com.mark.sharee.di.retrofitModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.*

class ShareeApplication : MultiDexApplication() {
    private val cachedComponents = SparseArray<ActivityComponent>()

    interface ActivityComponent

    override fun onCreate() {
        // Set locale has to be before super.onCreate
        setLocale(baseContext)
        super.onCreate()

        startKoin {
            androidContext(this@ShareeApplication)
            androidLogger()
            modules(listOf(retrofitModule, navigatorModule))
        }
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