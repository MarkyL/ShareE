package com.mark.sharee.fragments.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.adapters.MessagesAdapter
import com.mark.sharee.core.Action
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.fragments.generalPolls.GetGeneralPolls
import com.mark.sharee.fragments.generalPolls.GetGeneralPollsSuccess
import com.mark.sharee.fragments.generalPolls.GetMedicalPolls
import com.mark.sharee.fragments.generalPolls.PollDataState
import com.mark.sharee.mvvm.State
import com.mark.sharee.mvvm.ViewModelHolder
import com.mark.sharee.navigation.arguments.TransferInfo
import com.mark.sharee.network.model.responses.GeneralPollResponse
import com.mark.sharee.screens.MainScreen
import com.mark.sharee.screens.PollScreen
import com.mark.sharee.utils.Event
import com.mark.sharee.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_general_polls.*
import timber.log.Timber

class MessagesFragment : ShareeFragment() {

    lateinit var transferInfo: TransferInfo
    private val messagesAdapter = MessagesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_general_polls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(1, 30, true))
            this.adapter = messagesAdapter
        }
        configureScreen()
        this.messagesAdapter.submitList(transferInfo.messages)
    }

    private fun configureScreen() {
        configureToolbar()
    }

    private fun configureToolbar() {
        toolbar.addActions(arrayOf(Action.BackBlack), this)
        toolbar.setTitle(resources.getString(R.string.messages_screen_title))
    }
}
