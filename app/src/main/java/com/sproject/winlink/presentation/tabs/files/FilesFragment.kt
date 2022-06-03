package com.sproject.winlink.presentation.tabs.files

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sproject.winlink.R
import com.sproject.winlink.databinding.FragmentFilesBinding
import com.sproject.winlink.domain.model.FileItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilesFragment : Fragment(R.layout.fragment_files) {

    private val binding: FragmentFilesBinding by viewBinding()
    private val vm: FilesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filesAdapter = FilesAdapter()

        with(binding) {
            filesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            filesRecyclerView.adapter = filesAdapter

            val itemAnimator = filesRecyclerView.itemAnimator

            if (itemAnimator is DefaultItemAnimator) {
                itemAnimator.supportsChangeAnimations = false
            }
        }

        filesAdapter.files = arrayListOf(
            FileItem(name = "OS (C:)", lastEdit = 1, isFolder = true, path = "C:\\"),
            FileItem(name = "HDD Data I (D:)", lastEdit = 1, isFolder = true, path = "D:\\"),
        )
    }
}
