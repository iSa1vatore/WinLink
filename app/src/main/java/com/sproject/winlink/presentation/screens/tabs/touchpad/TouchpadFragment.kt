package com.sproject.winlink.presentation.screens.tabs.touchpad

import android.view.KeyEvent
import androidx.core.widget.doOnTextChanged
import com.sproject.winlink.R
import com.sproject.winlink.common.constants.MouseButton
import com.sproject.winlink.databinding.FragmentTouchpadBinding
import com.sproject.winlink.presentation.base.BaseFragment
import com.sproject.winlink.presentation.extensions.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TouchpadFragment : BaseFragment<FragmentTouchpadBinding, TouchpadViewModel>(
    R.layout.fragment_touchpad
) {

    override fun setupViews() {
        binding.touchpad.setOnTouchListener(object : TouchpadGestureDetector() {

            override fun onLeftButtonClick() = vm.mouseButtonClick(MouseButton.LEFT)

            override fun onRightButtonClick() = vm.mouseButtonClick(MouseButton.RIGHT)

            override fun onMove(x: Int, y: Int) = vm.mouseMove(x, y)

            override fun onScroll(x: Int, y: Int) = vm.mouseScroll(x, y)
        })
    }

    override fun setupListeners() {
        super.setupListeners()

        with(binding) {
            gyroscopeModeButton.setOnClickListener {
                vm.toggleGyroscopeMode()
            }
            keyboardButton.setOnClickListener {
                keyboard.requestFocus()
                keyboard.showKeyboard()
            }

            keyboard.doOnTextChanged { text, s, b, c ->
                if (text?.isNotEmpty() == true) {
                    vm.keyboardPress(text.toString())
                    keyboard.setText("")
                }
            }

            keyboard.setOnKeyListener { v, keyCode, event ->
                if (
                    event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_DEL
                ) {
                    vm.keyboardPress("backspace")
                }

                false
            }
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        vm.state.observe(viewLifecycleOwner) {
            if (it.gyroscopeIsEnabled) {
                enableGyroscopeListener()

                binding.gyroscopeModeButton.text = getString(R.string.gyroscope_mode_on)
            } else {
                disableGyroscopeListener()

                binding.gyroscopeModeButton.text = getString(R.string.gyroscope_mode_off)
            }
        }
    }

    override fun onGyroscopeChanged(x: Float, y: Float, z: Float) {
        val mouseX = -z * 20
        val mouseY = -x * 20

        vm.mouseMove(mouseX.toInt(), mouseY.toInt())
    }
}
