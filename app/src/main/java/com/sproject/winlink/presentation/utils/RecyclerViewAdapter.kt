package com.sproject.winlink.presentation.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


class RecyclerViewAdapter<T : ViewBinding, I>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T,
    private val onBindView: (view: T, item: Item<I>) -> Unit,
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var items: List<Item<I>> = emptyList()
        set(newValue) {
            val diffCallback = DiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)

            field = newValue

            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = bindingInflater(
            inflater,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        onBindView(holder.binding as T, items[position])

    override fun getItemCount(): Int = items.size

    class DiffCallback<I>(
        private val oldList: List<Item<I>>,
        private val newList: List<Item<I>>,
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.details == newItem.details
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem == newItem
        }
    }

    class ViewHolder(
        val binding: ViewBinding
    ) : RecyclerView.ViewHolder(binding.root)

    data class Item<I>(
        var details: I,
        var loading: Boolean = false,
    )
}