package com.sproject.winlink.presentation.screens.tabs.media

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.sproject.winlink.R
import com.sproject.winlink.databinding.FragmentMediaBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaFragment : Fragment(R.layout.fragment_media) {

    private val binding: FragmentMediaBinding by viewBinding()

    private val vm: MediaViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        initObservations()
    }

    private fun setupViews() {
        val textCover = "https://images.genius.com/17062f0c7df46ca98fde1c4340ee2d8d.1000x1000x1.jpg"

        with(binding) {
            with(mediaPlayer) {
                audioSource.text = "Spotify"
                songTitle.text = "4SQUAD"
                songArtist.text = "VELIAL SQUAD"

                playButton.setImageResource(R.drawable.ic_round_play_arrow)
                playButton.setImageResource(R.drawable.ic_round_pause)

                Glide.with(thumbnail.context)
                    .load(textCover)
                    .into(thumbnail)
            }

            volumeSlider.setListener {
                vm.soundSetVolume(it)
            }

            brightnessSlider.setListener {
                Log.e("value", it.toString())
            }
        }
    }

    private fun initObservations() {
        vm.state.observe(viewLifecycleOwner) {
            with(binding) {
                progressIndicator.visibility = if (it.isLoading) View.VISIBLE else View.GONE
                contentContainer.visibility = if (it.isLoading) View.GONE else View.VISIBLE

                if (it.info != null) {
                    volumeSlider.value = it.info.soundInfo.volume
                    brightnessSlider.value = 75
                }
            }
        }
    }

    private fun toggleFullScreenMode() {
        val windowInsetsController = ViewCompat.getWindowInsetsController(
            requireActivity().window.decorView
        ) ?: return

        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}
