package com.mark.sharee.navigation.operations

import android.app.Activity
import android.content.Intent
import com.mark.sharee.navigation.Navigator
import com.mark.sharee.navigation.Screen

class ReplaceBackStackOperation : NewIntentOperation {
    override val intentFlags: Int
        get() = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    constructor(navigator: Navigator, screens: List<Screen>) : super(navigator, screens)

    constructor(navigator: Navigator, screens: List<Screen>,
                hostActivityClass: Class<out Activity>) : super(navigator, screens, hostActivityClass)

    constructor(navigator: Navigator, screen: Screen) : this(navigator, listOf(screen))

    constructor(navigator: Navigator, screen: Screen,
                hostActivityClass: Class<out Activity>) : this(navigator, listOf(screen), hostActivityClass)
}
