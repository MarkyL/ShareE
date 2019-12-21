package com.mark.sharee.navigation.operations

import android.content.Intent
import com.mark.sharee.navigation.Navigator
import com.mark.sharee.navigation.Screen

internal class AddBackStackOperation(navigator: Navigator, screens: List<Screen>) : NewIntentOperation(navigator, screens) {
    override val intentFlags: Int
        get() = Intent.FLAG_ACTIVITY_NEW_TASK
}
