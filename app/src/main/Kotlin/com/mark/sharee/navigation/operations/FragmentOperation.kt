package com.mark.sharee.navigation.operations

import androidx.fragment.app.FragmentManager
import com.mark.sharee.navigation.Navigator

abstract class FragmentOperation internal constructor(navigator: Navigator) : Operation(navigator) {
    internal abstract fun execute(fm: FragmentManager)
}
