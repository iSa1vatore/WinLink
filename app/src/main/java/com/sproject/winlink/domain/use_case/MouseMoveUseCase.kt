package com.sproject.winlink.domain.use_case

import com.sproject.winlink.data.remote.PcSocketService
import javax.inject.Inject

class MouseMoveUseCase @Inject constructor(
    private val pcSocketService: PcSocketService
) {
    suspend operator fun invoke(x: Int, y: Int) {
        pcSocketService.mouseMove(x, y)
    }
}
