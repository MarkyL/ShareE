package com.mark.sharee.navigation

import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import bolts.Task
import com.example.sharee.R
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.navigation.operations.AddOperation
import com.mark.sharee.navigation.operations.GoBackToScreenOperation
import com.mark.sharee.navigation.operations.Operation
import com.mark.sharee.navigation.operations.ReplaceOperation
import rx.Observable
import rx.subjects.PublishSubject

abstract class Navigator @JvmOverloads protected constructor(@IdRes val containerId: Int,
                                                             @AnimRes var enterAnimation: Int = R.anim.fade_in,
                                                             @AnimRes var exitAnimation: Int = R.anim.fade_out,
                                                             @AnimRes var popEnterAnimation: Int = R.anim.fade_in,
                                                             @AnimRes var popExitAnimation: Int = R.anim.fade_out) {
    val subject: PublishSubject<ShareeFragment> = PublishSubject.create<ShareeFragment>()

    val navigationObservable: Observable<ShareeFragment>
        get() = subject.asObservable()

    fun onNext(fragment: ShareeFragment) {
        subject.onNext(fragment)
    }

    fun createAddOperation(screen: Screen): Operation =
        AddOperation(this, screen, true)

    fun createReplaceOperation(screen: Screen): Operation = createReplaceOperation(screen, true)

    fun createReplaceOperation(screen: Screen, addToBackStack: Boolean): Operation =
        ReplaceOperation(this, screen, addToBackStack)

    fun createGoBackToOperation(screenClass: Class<out Screen>, revertedAnimation: Boolean): Operation =
        GoBackToScreenOperation(
            this,
            screenClass,
            revertedAnimation
        )

    val topFragment: Task<Fragment>
        get() = fragmentManager.onSuccess { task -> task.result.findFragmentById(containerId) }

    abstract fun execute(operation: Operation)
    internal abstract val fragmentManager: Task<FragmentManager>
}
