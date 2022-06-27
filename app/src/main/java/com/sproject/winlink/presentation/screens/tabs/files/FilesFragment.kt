package com.sproject.winlink.presentation.screens.tabs.files

import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.sproject.winlink.R
import com.sproject.winlink.databinding.FragmentFilesBinding
import com.sproject.winlink.databinding.ItemFileBinding
import com.sproject.winlink.domain.model.FileItem
import com.sproject.winlink.presentation.base.BaseFragment
import com.sproject.winlink.presentation.extensions.visibleIf
import com.sproject.winlink.presentation.utils.RecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilesFragment : BaseFragment<FragmentFilesBinding, FilesViewModel>(
    R.layout.fragment_files
) {

    private lateinit var devicesAdapter: RecyclerViewAdapter<ItemFileBinding, FileItem>

    override fun setupViews() {
        devicesAdapter = RecyclerViewAdapter(
            bindingInflater = ItemFileBinding::inflate,
            onBindView = { v, cell ->
                v.root.setOnClickListener {
                    vm.getFiles(cell.details.path)
                }

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

            currentPathTextView.text = "PC"
        }
    }

    override fun setupObservers() {
        vm.state.observe(viewLifecycleOwner) {
            binding.progressBar.visibleIf(it.isLoading)

            devicesAdapter.items = it.files
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
}
