package com.sproject.winlink.presentation.screens.tabs.touchpad

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sproject.winlink.R
import com.sproject.winlink.common.constants.MouseButton
import com.sproject.winlink.databinding.FragmentTouchpadBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TouchpadFragment : Fragment(R.layout.fragment_touchpad) {

    private val binding: FragmentTouchpadBinding by viewBinding()
    private val vm: TouchpadViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val MAX_CLICK_DURATION = 300
        var startClickTime: Long = 0

        binding.touchpad.setOnTouchListener { _, event ->
            when (event?.action) {
                MotionEvent.ACTION_POINTER_DOWN -> {
                    Log.i("mouse", "wtf 1")
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    if (event.pointerCount == 2) {
                        vm.mouseButtonClick(MouseButton.RIGHT)
                    }
                }
                MotionEvent.ACTION_DOWN -> {
                    startClickTime = Calendar.getInstance().timeInMillis
                    vm.resetPrevMousePosition()
                }
                MotionEvent.ACTION_UP -> {
                    val clickDuration = Calendar.getInstance().timeInMillis - startClickTime
                    if (clickDuration < MAX_CLICK_DURATION) {
                        vm.mouseButtonClick(MouseButton.LEFT)
                    }
                }
                MotionEvent.ACTION_MOVE -> vm.mouseMove(
                    event.x.toInt(), event.y.toInt()
                )
            }

            true
        }
    }
}
