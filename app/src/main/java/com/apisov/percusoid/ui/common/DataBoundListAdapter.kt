package com.apisov.percusoid.ui.common

import android.databinding.ViewDataBinding
import android.support.annotation.MainThread
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * A generic RecyclerView adapter that uses Data Binding
 *
 * @param <T> Type of the items in the list
 * @param <V> The type of the ViewDataBinding
</V></T> */
abstract class DataBoundListAdapter<in T, V : ViewDataBinding> :
    RecyclerView.Adapter<DataBoundViewHolder<V>>() {

    private var items: List<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<V> {
        val binding = createBinding(parent)
        return DataBoundViewHolder(binding)
    }

    protected abstract fun createBinding(parent: ViewGroup): V

    override fun onBindViewHolder(holder: DataBoundViewHolder<V>, position: Int) {
        bind(holder.binding, items!![position])
        holder.binding.executePendingBindings()
    }

    @MainThread
    fun replace(update: List<T>?) {
        items = update
        notifyDataSetChanged()
    }

    protected abstract fun bind(binding: V, item: T)

    protected abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    protected abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    override fun getItemCount(): Int {
        return if (items == null) 0 else items!!.size
    }
}
