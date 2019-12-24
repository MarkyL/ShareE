package com.mark.sharee.navigation.operations

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.navigation.Navigator
import com.mark.sharee.navigation.Screen

abstract class ForwardNavigationOperation(navigator: Navigator,
                                          private val screen: Screen,
                                          private val addToBackStack: Boolean) : FragmentOperation(navigator) {

    protected abstract fun preformOperation(ft: FragmentTransaction, @IdRes containerId: Int, fragment: ShareeFragment, tag: String)

    override fun execute(fm: FragmentManager) {
        val fragment = screen.createFragment()
        val ft = fm.beginTransaction()
        ft.setCustomAnimations(getEnterAnimation(), getExitAnimation(),
                getPopEnterAnimation(), getPopExitAnimation())

        preformOperation(ft, containerId, fragment, screen.javaClass.simpleName)

        val oldFragment = fm.findFragmentById(containerId)
        if (oldFragment != null && addToBackStack) {
            ft.addToBackStack(oldFragment.javaClass.simpleName)
        }

        ft.commit()
        onNext(fragment)
    }
}
