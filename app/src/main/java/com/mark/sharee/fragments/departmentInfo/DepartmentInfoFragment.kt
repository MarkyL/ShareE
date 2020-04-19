package com.mark.sharee.fragments.departmentInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sharee.R
import com.mark.sharee.core.Action
import com.mark.sharee.core.ShareeFragment
import kotlinx.android.synthetic.main.fragment_department_info.*
import kotlinx.android.synthetic.main.sharee_toolbar.view.*

class DepartmentInfoFragment : ShareeFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_department_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        configureScreen()
    }

    private fun configureToolbar() {
        toolbar.titleTextView.text = resources.getString(R.string.department_info_title)
        toolbar.addActions(arrayOf(Action.BackBlack), this)
    }

    private fun configureScreen() {
        //webView.loadUrl("file:///Department_information.pdf")
        pdfView.fromAsset("Department_information.pdf")
            .load()
    }
}