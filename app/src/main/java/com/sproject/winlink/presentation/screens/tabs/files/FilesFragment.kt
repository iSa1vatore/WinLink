package com.sproject.winlink.presentation.screens.tabs.files

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sproject.winlink.R
import com.sproject.winlink.databinding.FragmentFilesBinding
import com.sproject.winlink.databinding.ItemFileBinding
import com.sproject.winlink.domain.model.FileItem
import com.sproject.winlink.presentation.utils.RecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilesFragment : Fragment(R.layout.fragment_files) {

    private val binding: FragmentFilesBinding by viewBinding()
    private val vm: FilesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val devicesAdapter = RecyclerViewAdapter<ItemFileBinding, FileItem>(
            bindingInflater = ItemFileBinding::inflate,
            onBindView = { v, cell ->
                v.fileName.text = cell.details.name
                v.lastEdit.text = cell.details.lastEdit.toString()
                v.moreIcon.setOnClickListener(this::showPopupMenu)
            }
        )

        with(binding) {
            filesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            filesRecyclerView.adapter = devicesAdapter

            val itemAnimator = filesRecyclerView.itemAnimator

            if (itemAnimator is DefaultItemAnimator) {
                itemAnimator.supportsChangeAnimations = false
            }
        }

        devicesAdapter.items = listOf(
            RecyclerViewAdapter.Item(
                FileItem(
                    name = "OS (C:)", lastEdit = 1, isFolder = true, path = "C:\\"
                )
            ),
            RecyclerViewAdapter.Item(
                FileItem(
                    name = "HDD Data I (D:)",
                    lastEdit = 1,
                    isFolder = true,
                    path = "D:\\"
                )
            ),
        )
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
}
