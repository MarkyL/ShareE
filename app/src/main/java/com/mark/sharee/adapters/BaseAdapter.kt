package com.mark.sharee.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mark.sharee.network.model.responses.Message
import com.mark.sharee.utils.ItemAnimation

abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    var items: List<T>
    var listener: AdapterListener<T>? = null

    constructor(listItems: List<T>) {
        this.items = listItems
    }

    constructor(listener: AdapterListener<T>) {
        items = emptyList()
        this.listener = listener
    }

    constructor() {
        items = emptyList()
    }

    fun submitList(listItems: List<T>) {
        this.items = listItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false), viewType)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Binder<T>).bind(items[position])

        listener?.let { listener ->
            holder.itemView.setOnClickListener { listener.onItemClick(items[position]) } }

        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position, items[position])
    }

    protected abstract fun getLayoutId(position: Int, obj: T): Int

    abstract fun getViewHolder(view: View, viewType: Int):RecyclerView.ViewHolder

    internal interface Binder<T> {
        fun bind(data: T)
    }

    interface AdapterListener<T> {
        fun onItemClick(data: T)
    }

    var lastPosition = -1

    private fun setAnimation(view: View, position: Int) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, position, ItemAnimation.FADE_IN)
            lastPosition = position
        }
    }

}