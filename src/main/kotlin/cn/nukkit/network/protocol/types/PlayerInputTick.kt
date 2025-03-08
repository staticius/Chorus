package cn.nukkit.network.protocol.types

import lombok.AllArgsConstructor
import lombok.Getter

@Getter
@AllArgsConstructor
class PlayerInputTick {
    private val inputTick: Long = 0
}
