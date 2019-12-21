package com.mark.sharee.navigation.operations

import androidx.fragment.app.FragmentManager
import com.example.sharee.R
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.navigation.Navigator
import com.mark.sharee.navigation.Screen
import java.util.*

class GoBackToScreenOperation @JvmOverloads constructor(navigator: Navigator,
                                                        private val screenClass: Class<out Screen>,
                                                        private val revertAnimations: Boolean = false) :
        FragmentOperation(navigator) {

    override fun execute(fm: FragmentManager) {
        val fragment = fm.findFragmentByTag(screenClass.simpleName) as ShareeFragment? ?:
                throw IllegalArgumentException(String.format("Screen %s is not in backStack", screenClass.simpleName))

        val fragments = ArrayList(fm.fragments)
        fragments.reverse()
        var first = true

        for ((index, backStackFragment) in fragments.withIndex()) {
            if (backStackFragment !is ShareeFragment) {
                continue
            }

            if (revertAnimations) {
                if (fragment == backStackFragment) {
                    backStackFragment.overrideNextEnterAnimation(getEnterAnimation())
                    break
                }

                if (first) {
                    backStackFragment.overrideNextExitAnimation(getExitAnimation())
                    first = false
                    continue
                }
            }

            if (index != fragments.lastIndex) {
                backStackFragment.overrideNextExitAnimation(R.anim.do_nothing)
            }
        }

        fm.popBackStack(fragment.javaClass.simpleName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        onNext(fragment)
    }
}
