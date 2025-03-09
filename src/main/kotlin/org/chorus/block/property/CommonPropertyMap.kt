package org.chorus.block.property

import cn.nukkit.block.property.enums.MinecraftCardinalDirection
import cn.nukkit.math.BlockFace
import com.google.common.collect.ImmutableBiMap

object CommonPropertyMap {
    @JvmField
    val EWSN_DIRECTION: ImmutableBiMap<BlockFace, Int> = ImmutableBiMap
        .builderWithExpectedSize<BlockFace, Int>(4)
        .put(BlockFace.EAST, 0)
        .put(BlockFace.WEST, 1)
        .put(BlockFace.SOUTH, 2)
        .put(BlockFace.NORTH, 3)
        .build()

    @JvmField
    val CARDINAL_BLOCKFACE: ImmutableBiMap<MinecraftCardinalDirection, BlockFace> = ImmutableBiMap
        .builderWithExpectedSize<MinecraftCardinalDirection, BlockFace>(4)
        .put(MinecraftCardinalDirection.EAST, BlockFace.EAST)
        .put(MinecraftCardinalDirection.WEST, BlockFace.WEST)
        .put(MinecraftCardinalDirection.SOUTH, BlockFace.SOUTH)
        .put(MinecraftCardinalDirection.NORTH, BlockFace.NORTH)
        .build()
}
