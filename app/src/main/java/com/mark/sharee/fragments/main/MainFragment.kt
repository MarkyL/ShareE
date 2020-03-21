package com.mark.sharee.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.sharee.R
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.screens.GeneralPollsScreen
import com.mark.sharee.screens.PollScreen
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment : ShareeFragment() {

    lateinit var transferInfo: TransferInfo

    private val viewModel : MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)

        phoneNumberTv.text = "שלום " + transferInfo.phoneNumber

        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            val dataState = it ?: return@Observer
            progressBar.visibility = if (dataState.showProgress) View.VISIBLE else View.GONE
            if (dataState.response != null && !dataState.response.consumed) {
                dataState.response.consume()?.let { response ->
                    Toast.makeText(context, response.name, Toast.LENGTH_LONG).show()
                }
            }
            if (dataState.error != null && !dataState.error.consumed) {
                dataState.error.consume()?.let { errorResource ->
                    Toast.makeText(context, resources.getString(errorResource), Toast.LENGTH_SHORT).show()
                    // handle error state
                }
            }

        })

        generalPollsBtn.setOnClickListener { onGeneralPollsBtnClick() }
    }

    private fun onGeneralPollsBtnClick() {
        navigator.replace(GeneralPollsScreen(TransferInfo()))
    }
}