package org.chorus.utils

import lombok.EqualsAndHashCode
import lombok.ToString



class SkinAnimation(
    val image: SerializedImage, val type: Int, val frames: Float,
    val expression: Int
) {
}
