package com.mark.sharee.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.network.model.responses.Message
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.message_item.*
import timber.log.Timber
import java.text.SimpleDateFormat
import android.text.format.DateFormat
import com.mark.sharee.utils.DateTimeHelper
import java.util.*

class MessagesAdapter : RecyclerView.Adapter<MessageViewHolder>() {

    var items = listOf<Message>()
        private set


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MessageViewHolder.create(parent)

    override fun onBindViewHolder(holderAdapter: MessageViewHolder, position: Int) {
        holderAdapter.bind(holderAdapter.containerView, items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return MESSAGE_TYPE
    }

    fun submitList(list: MutableList<Message>) {
        items = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    companion object {
        const val MESSAGE_TYPE = 1
    }

}

class MessageViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(view: View, message: Message) {
        messageDate.text = DateTimeHelper.getDate(message.timeStamp)
        messageText.text = message.body
    }

    companion object {

        fun create(parent: ViewGroup): MessageViewHolder {
            return MessageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.message_item, parent, false)
            )
        }
    }
}