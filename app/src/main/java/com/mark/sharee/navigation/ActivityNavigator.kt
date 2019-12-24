package com.mark.sharee.navigation

import android.app.Activity
import android.net.Uri
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import bolts.Continuation
import bolts.Task
import com.mark.sharee.core.ShareeActivity
import com.mark.sharee.navigation.operations.*
import com.mark.sharee.navigation.operations.AddBackStackOperation
import com.mark.sharee.utils.ObjectDelegate
import timber.log.Timber

class ActivityNavigator : FragmentNavigator {
    private val activityDelegate = ObjectDelegate<ShareeActivity>()

    constructor(@IdRes containerId: Int) : super(containerId)

    constructor(@IdRes containerId: Int, @AnimRes enterAnimation: Int, @AnimRes exitAnimation: Int,
                @AnimRes popEnterAnimation: Int, @AnimRes popExitAnimation: Int) :
            super(containerId, enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)

    fun takeActivity(activity: ShareeActivity) {
        super.takeFragmentManager(activity.supportFragmentManager)
        activityDelegate.takeObject(activity)
    }

    fun dropActivity() {
        super.dropFragmentManager()
        activityDelegate.dropObject()
    }

    fun addBackStack(screens: List<Screen>) {
        execute(AddBackStackOperation(this, screens))
    }

    fun <T : Screen> replaceBackStackWith(screen: T) {
        execute(ReplaceBackStackOperation(this, screen))
    }

    fun <T : Screen> replaceBackStackWith(screen: T,
                                          hostActivityClass: Class<out Activity>) {
        execute(
            ReplaceBackStackOperation(
                this,
                screen,
                hostActivityClass
            )
        )
    }

    fun replaceBackStackWith(screens: List<Screen>) {
        execute(ReplaceBackStackOperation(this, screens))
    }

    fun replaceBackstackWithData(screens: List<Screen>, data: Uri?) {
        execute(
            ReplaceBackStackWithDataOperation(
                this,
                screens,
                data
            )
        )
    }

    fun replaceBackStackWith(screens: List<Screen>,
                             hostActivityClass: Class<out Activity>) {
        execute(
            ReplaceBackStackOperation(
                this,
                screens,
                hostActivityClass
            )
        )
    }

    override fun execute(operation: Operation) {
        if (operation is ActivityOperation) {
            activityDelegate.waitForObject().onSuccess(Continuation<ShareeActivity, Void> { task ->
                operation.execute(task.result)
                null
            }, Task.UI_THREAD_EXECUTOR).continueWith { task ->
                if (task.isFaulted) {
                    Timber.e(task.error, "navigation failed")
                }
                null
            }
        } else {
            super.execute(operation)
        }
    }
}
