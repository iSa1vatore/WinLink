package com.sproject.winlink.presentation.screens.tabs.files

import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.sproject.winlink.R
import com.sproject.winlink.common.util.TimeUtils
import com.sproject.winlink.databinding.FragmentFilesBinding
import com.sproject.winlink.databinding.ItemFileBinding
import com.sproject.winlink.domain.model.FileItem
import com.sproject.winlink.presentation.base.BaseFragment
import com.sproject.winlink.presentation.extensions.showToast
import com.sproject.winlink.presentation.extensions.visibleIf
import com.sproject.winlink.presentation.utils.RecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilesFragment : BaseFragment<FragmentFilesBinding, FilesViewModel>(
    R.layout.fragment_files
) {

    @Inject
    lateinit var timeUtils: TimeUtils

    private var devicesAdapter: RecyclerViewAdapter<ItemFileBinding, FileItem>? = null

    override fun setupViews() {
        devicesAdapter = RecyclerViewAdapter(
            bindingInflater = ItemFileBinding::inflate,
            onBindView = { v, cell ->
                v.moreIcon.tag = cell.details

                if (cell.details.isFolder) {
                    v.root.setOnClickListener {
                        vm.getFiles(cell.details.path)
                    }
                } else {
                    v.root.setOnClickListener {
                        this.showPopupMenu(v.moreIcon)
                    }
                }

                v.fileIcon.setImageResource(
                    if (cell.details.isFolder)
                        R.drawable.ic_outline_folder
                    else
                        R.drawable.ic_outline_file
                )

                v.fileName.text = cell.details.name

                var lastEdit = getString(R.string.no_data)

                if (cell.details.lastEdit != 0) {
                    lastEdit = timeUtils.getTimeAgo(cell.details.lastEdit)
                }

                if (cell.details.isFolder) {
                    v.desc.text = "$lastEdit · ${
                    getString(
                        R.string.files_count,
                        cell.details.filesCount
                    )
                    }"
                } else {
                    v.desc.text = lastEdit
                }

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

            currentPathTextView.text = "/"
        }
    }

    override fun setupObservers() {
        vm.state.observe(viewLifecycleOwner) {
            binding.progressBar.visibleIf(it.isLoading)

            binding.currentPathTextView.text = it.path

            devicesAdapter?.items = it.files
        }

        lifecycleScope.launch {
            vm.events.collect { event ->
                when (event) {
                    is FilesEvent.DownloadFile -> {
                        downloadFile(
                            fileName = event.name,
                            url = event.url,
                        )
                    }
                    is FilesEvent.FileRemoved -> {
                        if (event.file.isFolder) {
                            showToast(getString(R.string.folder_removed))
                        } else {
                            showToast(getString(R.string.file_removed))
                        }
                    }
                }
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val context = view.context
        val popupMenu = PopupMenu(context, view)

        val file = view.tag as FileItem

        popupMenu.menu.add(
            0,
            1,
            Menu.NONE,
            "Download"
        ).apply {
            isEnabled = !file.isFolder
        }

        popupMenu.menu.add(
            0,
            2,
            Menu.NONE,
            "Delete"
        )

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                1 -> {
                    updateOrRequestPermission()
                    vm.downloadFile(file)
                }
                2 -> vm.deleteFile(file)
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }
}
