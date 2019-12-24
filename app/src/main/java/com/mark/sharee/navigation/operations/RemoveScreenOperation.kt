package com.mark.sharee.navigation.operations


import androidx.fragment.app.FragmentManager
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.navigation.Navigator
import com.mark.sharee.navigation.Screen

class RemoveScreenOperation internal constructor(navigator: Navigator,
                                                 private val screenClass: Class<out Screen>,
                                                 private val executePendingTransactions: Boolean) :
        FragmentOperation(navigator) {

    override fun execute(fm: FragmentManager) {
        val fragment = fm.findFragmentByTag(screenClass.simpleName) as ShareeFragment? ?:
                throw IllegalArgumentException(String.format("Screen %s is not in backStack",
                        screenClass.simpleName))

        val ft = fm.beginTransaction()
        ft.setCustomAnimations(getEnterAnimation(), getExitAnimation())
        ft.remove(fragment)
        ft.commit()
        if (executePendingTransactions) {
            fm.executePendingTransactions()
        }
        fm.popBackStack()


    }
}