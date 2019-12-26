package com.mark.sharee.screens

import com.mark.sharee.fragments.signin.SignInFragment
import com.mark.sharee.navigation.Screen

class SignInScreen : Screen() {

    override fun create(): SignInFragment {
        return SignInFragment()
    }

}