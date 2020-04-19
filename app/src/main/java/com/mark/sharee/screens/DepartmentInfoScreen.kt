package com.mark.sharee.screens

import com.mark.sharee.fragments.departmentInfo.DepartmentInfoFragment
import com.mark.sharee.navigation.Screen

class DepartmentInfoScreen : Screen() {

    override fun create(): DepartmentInfoFragment {
        return DepartmentInfoFragment()
    }

}