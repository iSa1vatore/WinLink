package com.sproject.winlink.presentation.screens.tabs.media

import com.bumptech.glide.Glide
import com.sproject.winlink.R
import com.sproject.winlink.common.constants.MediaAction
import com.sproject.winlink.common.constants.PowerAction
import com.sproject.winlink.databinding.FragmentMediaBinding
import com.sproject.winlink.presentation.base.BaseFragment
import com.sproject.winlink.presentation.extensions.hideViews
import com.sproject.winlink.presentation.extensions.showToast
import com.sproject.winlink.presentation.extensions.showViews
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaFragment : BaseFragment<FragmentMediaBinding, MediaViewModel>(
    R.layout.fragment_media
) {

    override fun setupViews() {
        val cover = "https://images.genius.com/17062f0c7df46ca98fde1c4340ee2d8d.1000x1000x1.jpg"

        with(binding.mediaPlayer) {
            audioSource.text = "Spotify"
            songTitle.text = "4SQUAD"
            songArtist.text = "VELIAL SQUAD"

            playButton.setImageResource(R.drawable.ic_round_play_arrow)
            playButton.setImageResource(R.drawable.ic_round_pause)

            Glide.with(thumbnail.context)
                .load(cover)
                .into(thumbnail)
        }
    }

    override fun setupListeners() {
        with(binding) {
            volumeSlider.setListener(vm::soundSetVolume)
            brightnessSlider.setListener(vm::setBrightness)

            lockButton.setOnClickListener { vm.powerAction(PowerAction.LOCK) }
            powerButton.setOnClickListener { vm.powerAction(PowerAction.OFF) }
            rebootButton.setOnClickListener { vm.powerAction(PowerAction.REBOOT) }
            sleepButton.setOnClickListener { vm.powerAction(PowerAction.SLEEP) }
            screenOffButton.setOnClickListener { vm.screenOff() }

            with(mediaPlayer) {
                prevButton.setOnClickListener { vm.mediaAction(MediaAction.PREV) }
                playButton.setOnClickListener { vm.mediaAction(MediaAction.PLAY) }
                nextButton.setOnClickListener { vm.mediaAction(MediaAction.NEXT) }
            }
        }
    }

    override fun setupObservers() {
        vm.state.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is MediaState.Loading -> {
                        showViews(progressIndicator)
                        hideViews(contentContainer)
                    }
                    is MediaState.Success -> {
                        showViews(contentContainer)
                        hideViews(progressIndicator)

                        volumeSlider.value = it.info.soundInfo.volume
                        brightnessSlider.value = 75
                    }
                    is MediaState.Error -> showToast(it.message)
                }
            }
        }
    }
}
