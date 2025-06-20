package org.chorus_oss.chorus.experimental.network.protocol.utils

import org.chorus_oss.chorus.math.Vector3
import org.chorus_oss.protocol.types.BlockPos
import kotlin.math.floor

fun BlockPos.Companion.from(vec: Vector3): BlockPos {
    return BlockPos(
        floor(vec.x).toInt(),
        floor(vec.y).toInt(),
        floor(vec.z).toInt(),
    )
}