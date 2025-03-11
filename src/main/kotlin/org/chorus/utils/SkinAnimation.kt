package org.chorus.utils

data class SkinAnimation(
    val image: SerializedImage,
    val type: Int,
    val frames: Float,
    val expression: Int
)
