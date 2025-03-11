package org.chorus.utils

import org.chorus.entity.data.Skin
import io.netty.util.internal.EmptyArrays
import java.util.*

class SerializedImage(val width: Int, val height: Int, val data: ByteArray) {
    companion object {
        val EMPTY: SerializedImage = SerializedImage(0, 0, EmptyArrays.EMPTY_BYTES)

        fun fromLegacy(skinData: ByteArray): SerializedImage {
            Objects.requireNonNull(skinData, "skinData")
            return when (skinData.size) {
                Skin.SINGLE_SKIN_SIZE -> SerializedImage(32, 32, skinData)
                Skin.SKIN_64_32_SIZE -> SerializedImage(64, 32, skinData)
                Skin.DOUBLE_SKIN_SIZE -> SerializedImage(64, 64, skinData)
                Skin.SKIN_128_64_SIZE -> SerializedImage(128, 64, skinData)
                Skin.SKIN_128_128_SIZE -> SerializedImage(128, 128, skinData)
                else -> throw IllegalArgumentException("Unknown legacy skin size")
            }
        }
    }
}
