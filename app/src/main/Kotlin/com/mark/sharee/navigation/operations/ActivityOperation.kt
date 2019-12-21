package com.mark.sharee.navigation.operations

import com.mark.sharee.core.ShareeActivity
import com.mark.sharee.navigation.Navigator

abstract class ActivityOperation(navigator: Navigator) : Operation(navigator) {
    abstract fun execute(activity: ShareeActivity)
}
