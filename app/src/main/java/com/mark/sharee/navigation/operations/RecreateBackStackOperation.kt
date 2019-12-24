package com.mark.sharee.navigation.operations

import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.example.sharee.R
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.navigation.Navigator
import com.mark.sharee.navigation.Screen

class RecreateBackStackOperation(navigator: Navigator, private val intent: Intent) : FragmentOperation(navigator) {

    override fun execute(fm: FragmentManager) {
        Screen.deserialize(intent)?.let { addScreens(fm, it) }?.let { onNext(it) }
    }

    private fun <T : Screen> addScreens(fm: FragmentManager, screens: List<T>): ShareeFragment {
        var lastFragment: ShareeFragment? = null
        val count = screens.size
        var first = true
        for (i in 0 until count) {
            val screen = screens[i]
            if (first) {
                lastFragment = addScreen(fm, screen, i + 1 == count, null)
                first = false
            } else {
                lastFragment = addScreen(fm, screen, i + 1 == count, lastFragment?.javaClass?.simpleName)
            }
        }
        return lastFragment!!
    }

    private fun addScreen(fm: FragmentManager, screen: Screen,
                          disableAnimations: Boolean, backStackTag: String?): ShareeFragment {
        val ft = fm.beginTransaction()
        if (disableAnimations) {
            ft.setCustomAnimations(
                R.anim.do_nothing, R.anim.do_nothing, getPopEnterAnimation(),
                    getPopExitAnimation())
        }

        val fragment = screen.createFragment()
        ft.replace(containerId, fragment, screen.javaClass.simpleName)
        if (backStackTag != null) {
            ft.addToBackStack(backStackTag)
        }
        ft.commit()
        return fragment
    }


}
