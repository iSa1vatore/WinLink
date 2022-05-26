package com.sproject.winlink.domain.use_case

import com.sproject.winlink.data.remote.PcSocketService
import javax.inject.Inject

class MouseClickUseCase @Inject constructor(
    private val pcSocketService: PcSocketService
) {
    suspend operator fun invoke(button: Int) {
        pcSocketService.mouseClick(button)
    }
}
