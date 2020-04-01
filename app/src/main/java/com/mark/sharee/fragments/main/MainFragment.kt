package com.mark.sharee.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.sharee.R
import com.mark.sharee.activities.MainActivity
import com.mark.sharee.core.AbstractAction
import com.mark.sharee.core.Action
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.core.SupportsOnBackPressed
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.screens.GeneralPollsScreen
import com.mark.sharee.widgets.ShareeToolbar
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.sharee_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class MainFragment : ShareeFragment(), ShareeToolbar.ActionListener, SupportsOnBackPressed {

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

        configureToolbar()

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
    }

    private fun configureToolbar() {
        homeToolbar.titleTextView.text = "Sharee"
        homeToolbar.addActions(arrayOf(Action.Drawer), this)
    }

    override fun onActionSelected(action: AbstractAction): Boolean {
        if (action == Action.Drawer) {
            Timber.i("onActionSelected - Drawer")
            (activity as MainActivity).openDrawer()
            return true
        }


        return false
    }

    override fun onBackPressed(): Boolean {
        requireActivity().finishAffinity()
        return true
    }

}