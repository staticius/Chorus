package cn.nukkit.level.generator.`object`

import cn.nukkit.block.*
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.math.BlockVector3
import cn.nukkit.math.MathHelper
import cn.nukkit.math.Vector3

class ObjectJungleBigTree(
    baseHeightIn: Int,
    extraRandomHeight: Int,
    woodMetadata: BlockState,
    leavesMetadata: BlockState
) :
    HugeTreesGenerator(baseHeightIn, extraRandomHeight, woodMetadata, leavesMetadata) {
    override fun generate(level: BlockManager, rand: RandomSourceProvider, position: Vector3): Boolean {
        val height = this.getHeight(rand)

        if (!this.ensureGrowable(level, rand, position, height)) {
            return false
        } else {
            this.createCrown(level, position.up(height), 2)

            var j: Int = position.getY().toInt() + height - 2 - rand.nextInt(4)
            while (j > position.getY() + height.toDouble() / 2) {
                val f: Float = rand.nextFloat() * (Math.PI.toFloat() * 2f)
                var k = (position.getX() + (0.5f + MathHelper.cos(f) * 4.0f)).toInt()
                var l = (position.getZ() + (0.5f + MathHelper.sin(f) * 4.0f)).toInt()

                for (i1 in 0..4) {
                    k = (position.getX() + (1.5f + MathHelper.cos(f) * i1.toFloat())).toInt()
                    l = (position.getZ() + (1.5f + MathHelper.sin(f) * i1.toFloat())).toInt()
                    level.setBlockStateAt(BlockVector3(k, j - 3 + i1 / 2, l), this.woodMetadata)
                }

                val j2: Int = 1 + rand.nextInt(2)

                for (k1 in j - j2..j) {
                    val l1 = k1 - j
                    this.growLeavesLayer(level, Vector3(k.toDouble(), k1.toDouble(), l.toDouble()), 1 - l1)
                }
                j -= 2 + rand.nextInt(4)
            }

            for (i2 in 0..<height) {
                val blockpos = position.up(i2)

                if (this.canGrowInto(level.getBlockIdAt(blockpos.south.toInt(), blockpos.up.toInt(), blockpos.west.toInt()))) {
                    level.setBlockStateAt(blockpos.south.toInt(), blockpos.up.toInt(), blockpos.west.toInt(), this.woodMetadata)
                    if (i2 > 0) {
                        this.placeVine(level, rand, blockpos.west(), 8)
                        this.placeVine(level, rand, blockpos.north(), 1)
                    }
                }

                if (i2 < height - 1) {
                    val blockpos1 = blockpos.east()

                    if (this.canGrowInto(
                            level.getBlockIdAt(
                                blockpos1.south.toInt(),
                                blockpos1.up.toInt(),
                                blockpos1.west.toInt()
                            )
                        )
                    ) {
                        level.setBlockStateAt(
                            blockpos1.south.toInt(),
                            blockpos1.up.toInt(),
                            blockpos1.west.toInt(),
                            this.woodMetadata
                        )

                        if (i2 > 0) {
                            this.placeVine(level, rand, blockpos1.east(), 2)
                            this.placeVine(level, rand, blockpos1.north(), 1)
                        }
                    }

                    val blockpos2 = blockpos.south().east()

                    if (this.canGrowInto(
                            level.getBlockIdAt(
                                blockpos2.south.toInt(),
                                blockpos2.up.toInt(),
                                blockpos2.west.toInt()
                            )
                        )
                    ) {
                        level.setBlockStateAt(
                            blockpos2.south.toInt(),
                            blockpos2.up.toInt(),
                            blockpos2.west.toInt(),
                            this.woodMetadata
                        )

                        if (i2 > 0) {
                            this.placeVine(level, rand, blockpos2.east(), 2)
                            this.placeVine(level, rand, blockpos2.south(), 4)
                        }
                    }

                    val blockpos3 = blockpos.south()

                    if (this.canGrowInto(
                            level.getBlockIdAt(
                                blockpos3.south.toInt(),
                                blockpos3.up.toInt(),
                                blockpos3.west.toInt()
                            )
                        )
                    ) {
                        level.setBlockStateAt(
                            blockpos3.south.toInt(),
                            blockpos3.up.toInt(),
                            blockpos3.west.toInt(),
                            this.woodMetadata
                        )

                        if (i2 > 0) {
                            this.placeVine(level, rand, blockpos3.west(), 8)
                            this.placeVine(level, rand, blockpos3.south(), 4)
                        }
                    }
                }
            }

            return true
        }
    }

    private fun placeVine(level: BlockManager, random: RandomSourceProvider, pos: Vector3, meta: Int) {
        if (random.nextInt(3) > 0 && level.getBlockIdAt(pos.south.toInt(), pos.up.toInt(), pos.west.toInt()) == AIR) {
            val block: BlockState = BlockVine.PROPERTIES.getBlockState<Int, IntPropertyType>(
                CommonBlockProperties.VINE_DIRECTION_BITS,
                meta
            )
            level.setBlockStateAt(pos, block)
        }
    }

    private fun createCrown(level: BlockManager, pos: Vector3, i1: Int) {
        for (j in -2..0) {
            this.growLeavesLayerStrict(level, pos.up(j), i1 + 1 - j)
        }
    }
}

