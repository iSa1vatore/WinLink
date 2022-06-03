package com.sproject.winlink.presentation.tabs.files

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sproject.winlink.databinding.ItemFileBinding
import com.sproject.winlink.domain.model.FileItem

class FilesDiffCallback(
    private val oldList: List<FileItem>,
    private val newList: List<FileItem>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]

        return oldUser.path == newUser.path
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]

        return oldUser == newUser
    }
}

class FilesAdapter() : RecyclerView.Adapter<FilesAdapter.FilesViewHolder>() {
    var files: List<FileItem> = emptyList()
        set(newValue) {
            val diffCallback = FilesDiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)

            field = newValue

            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFileBinding.inflate(
            inflater,
            parent,
            false
        )

        binding.moreIcon.setOnClickListener(this::showPopupMenu)

        return FilesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilesViewHolder, position: Int) {
        val fileItem = files[position]

        with(holder.binding) {
            fileName.text = fileItem.name
            lastEdit.text = fileItem.lastEdit.toString()
        }
    }

    private fun showPopupMenu(view: View) {
        val context = view.context
        val popupMenu = PopupMenu(context, view)

        popupMenu.menu.add(
            0,
            1,
            Menu.NONE,
            "1"
        )
        popupMenu.menu.add(
            0,
            2,
            Menu.NONE,
            "2"
        )
        popupMenu.menu.add(
            0,
            3,
            Menu.NONE,
            "3"
        )

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                1 -> {}
                2 -> {}
                3 -> {}
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    override fun getItemCount(): Int = files.size

    class FilesViewHolder(
        val binding: ItemFileBinding
    ) : RecyclerView.ViewHolder(binding.root)
}
