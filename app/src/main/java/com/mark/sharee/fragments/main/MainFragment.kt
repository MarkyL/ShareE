package com.mark.sharee.fragments.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sharee.R
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.navigation.arguments.TransferInfo
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : ShareeFragment() {

    lateinit var transferInfo: TransferInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)

        phoneNumberTv.text = transferInfo.phoneNumber
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)


    }
}