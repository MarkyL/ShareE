package com.mark.sharee.navigation.operations

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentTransaction
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.navigation.Navigator
import com.mark.sharee.navigation.Screen

class ReplaceOperation internal constructor(navigator: Navigator, screen: Screen, addToBackStack: Boolean) :
        ForwardNavigationOperation(navigator, screen, addToBackStack) {

    override fun preformOperation(ft: FragmentTransaction, @IdRes containerId: Int, fragment: ShareeFragment, tag: String) {
        ft.replace(containerId, fragment, tag)
    }
}
