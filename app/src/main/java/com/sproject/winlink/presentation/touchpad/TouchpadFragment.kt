package com.sproject.winlink.presentation.touchpad

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sproject.winlink.R
import com.sproject.winlink.databinding.FragmentTouchpadBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TouchpadFragment : Fragment(R.layout.fragment_touchpad) {

    private val binding: FragmentTouchpadBinding by viewBinding()
    private val vm: TouchpadViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.connectToPc()

        binding.touchpad.setOnTouchListener { _, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> vm.resetPrevMousePosition()
                MotionEvent.ACTION_MOVE -> vm.mouseMove(
                    event.x.toInt(), event.y.toInt()
                )
            }

            true
        }
    }
}
