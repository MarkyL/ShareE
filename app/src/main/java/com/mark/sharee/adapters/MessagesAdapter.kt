package com.mark.sharee.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.network.model.responses.Message
import com.mark.sharee.utils.DateTimeHelper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.message_item.*

class MessagesAdapter : BaseAdapter<Message>() {

    override fun getLayoutId(position: Int, obj: Message): Int {
        return R.layout.message_item
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(view)
    }
}

class MessageViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer, BaseAdapter.Binder<Message> {

    override fun bind(data: Message) {
        messageDate.text = DateTimeHelper.getDate(data.timeStamp)
        messageText.text = data.body
    }
}