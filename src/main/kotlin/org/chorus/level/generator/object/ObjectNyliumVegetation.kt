package org.chorus.level.generator.`object`

import org.chorus.block.BlockID
import org.chorus.math.Vector3
import org.chorus.utils.random.RandomSourceProvider

object ObjectNyliumVegetation {
    fun growVegetation(level: BlockManager, pos: Vector3, random: RandomSourceProvider) {
        for (i in 0..127) {
            var num = 0

            var x = pos.floorX
            var y = pos.floorY + 1
            var z = pos.floorZ

            var crimson = level.getBlockIdAt(x, y - 1, z) === BlockID.CRIMSON_NYLIUM

            while (true) {
                if (num >= i / 16) {
                    if (level.getBlockIdAt(x, y, z) === BlockID.AIR) {
                        if (crimson) {
                            if (random.nextInt(8) == 0) {
                                if (random.nextInt(8) == 0) {
                                    level.setBlockStateAt(x, y, z, BlockID.WARPED_FUNGUS)
                                } else {
                                    level.setBlockStateAt(x, y, z, BlockID.CRIMSON_FUNGUS)
                                }
                            } else {
                                level.setBlockStateAt(x, y, z, BlockID.CRIMSON_ROOTS)
                            }
                        } else {
                            if (random.nextInt(8) == 0) {
                                if (random.nextInt(8) == 0) {
                                    level.setBlockStateAt(x, y, z, BlockID.CRIMSON_FUNGUS)
                                } else {
                                    level.setBlockStateAt(x, y, z, BlockID.WARPED_FUNGUS)
                                }
                            } else {
                                if (random.nextBoolean()) {
                                    level.setBlockStateAt(x, y, z, BlockID.WARPED_ROOTS)
                                } else {
                                    level.setBlockStateAt(x, y, z, BlockID.NETHER_SPROUTS)
                                }
                            }
                        }
                    }

                    break
                }

                x += random.nextInt(-1, 1)
                y += random.nextInt(-1, 1) * random.nextInt(3) / 2
                z += random.nextInt(-1, 1)

                val id = level.getBlockIdAt(x, y - 1, z)
                crimson = id === BlockID.CRIMSON_NYLIUM
                if ((!crimson && id !== BlockID.WARPED_NYLIUM) || y > 255 || y < 0) {
                    break
                }

                ++num
            }
        }
    }
}
