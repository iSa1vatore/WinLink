package com.sproject.winlink.presentation.screens.qr_scanner

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.sproject.winlink.R
import com.sproject.winlink.databinding.FragmentQrScanerBinding

class QrScannerFragment : Fragment(R.layout.fragment_qr_scaner) {
    private val binding: FragmentQrScanerBinding by viewBinding()

    private lateinit var codeScanner: CodeScanner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()
        val scannerView = binding.scannerView

        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    EXTRA_IP_ADDRESS,
                    it.text
                )
                findNavController().popBackStack()
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    companion object {
        const val EXTRA_IP_ADDRESS = "EXTRA_IP_ADDRESS"
    }
}
