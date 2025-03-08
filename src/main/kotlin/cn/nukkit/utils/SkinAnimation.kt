package cn.nukkit.utils

import lombok.EqualsAndHashCode
import lombok.ToString

@ToString
@EqualsAndHashCode
class SkinAnimation(
    val image: SerializedImage, val type: Int, val frames: Float,
    val expression: Int
) {
}
