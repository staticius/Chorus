package org.chorus.level.generator.`object`

import cn.nukkit.block.*
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.math.Vector3

class ObjectBigMushroom : ObjectGenerator {
    /**
     * The mushroom type. 0 for brown, 1 for red.
     */
    private val mushroomType: MushroomType?

    constructor(mushroomType: MushroomType?) {
        this.mushroomType = mushroomType
    }

    constructor() {
        this.mushroomType = null
    }

    override fun generate(level: BlockManager, rand: RandomSourceProvider, position: Vector3): Boolean {
        var block = this.mushroomType
        if (block == null) {
            block = if (rand.nextBoolean()) MushroomType.RED else MushroomType.BROWN
        }

        val mushroom = if (block == MushroomType.BROWN) Block.get(BROWN_MUSHROOM_BLOCK) else Block.get(
            RED_MUSHROOM_BLOCK
        )

        var i: Int = rand.nextInt(3) + 4

        if (rand.nextInt(12) == 0) {
            i *= 2
        }

        var flag = true

        if (position.getY() >= 1 && position.getY() + i + 1 < 256) {
            var j = position.floorY
            while (j <= position.getY() + 1 + i) {
                var k = 3

                if (j <= position.getY() + 3) {
                    k = 0
                }

                val pos = Vector3()

                var l = position.floorX - k
                while (l <= position.getX() + k && flag) {
                    var i1 = position.floorZ - k
                    while (i1 <= position.getZ() + k && flag) {
                        if (j >= 0 && j < 256) {
                            pos.setComponents(l.toDouble(), j.toDouble(), i1.toDouble())
                            val material = level.getBlockAt(pos.floorX, pos.floorY, pos.floorZ)

                            if (material!!.id != AIR && material !is BlockLeaves) {
                                flag = false
                            }
                        } else {
                            flag = false
                        }
                        ++i1
                    }
                    ++l
                }
                ++j
            }

            if (!flag) {
                return false
            } else {
                val pos2 = position.down()
                val block1 = level.getBlockIdAt(pos2.floorX, pos2.floorY, pos2.floorZ)

                if ((block1 != DIRT) && (block1 != GRASS_BLOCK) && (block1 != MYCELIUM)) {
                    return false
                } else {
                    var k2 = position.floorY + i

                    if (block == MushroomType.RED) {
                        k2 = position.floorY + i - 3
                    }

                    var l2 = k2
                    while (l2 <= position.getY() + i) {
                        var j3 = 1

                        if (l2 < position.getY() + i) {
                            ++j3
                        }

                        if (block == MushroomType.BROWN) {
                            j3 = 3
                        }

                        val k3 = position.floorX - j3
                        val l3 = position.floorX + j3
                        val j1 = position.floorZ - j3
                        val k1 = position.floorZ + j3

                        for (l1 in k3..l3) {
                            for (i2 in j1..k1) {
                                var j2 = 5

                                if (l1 == k3) {
                                    --j2
                                } else if (l1 == l3) {
                                    ++j2
                                }

                                if (i2 == j1) {
                                    j2 -= 3
                                } else if (i2 == k1) {
                                    j2 += 3
                                }

                                var meta = j2

                                if (block == MushroomType.BROWN || l2 < position.getY() + i) {
                                    if ((l1 == k3 || l1 == l3) && (i2 == j1 || i2 == k1)) {
                                        continue
                                    }

                                    if (l1.toDouble() == position.getX() - (j3 - 1) && i2 == j1) {
                                        meta = NORTH_WEST
                                    }

                                    if (l1 == k3 && i2.toDouble() == position.getZ() - (j3 - 1)) {
                                        meta = NORTH_WEST
                                    }

                                    if (l1.toDouble() == position.getX() + (j3 - 1) && i2 == j1) {
                                        meta = NORTH_EAST
                                    }

                                    if (l1 == l3 && i2.toDouble() == position.getZ() - (j3 - 1)) {
                                        meta = NORTH_EAST
                                    }

                                    if (l1.toDouble() == position.getX() - (j3 - 1) && i2 == k1) {
                                        meta = SOUTH_WEST
                                    }

                                    if (l1 == k3 && i2.toDouble() == position.getZ() + (j3 - 1)) {
                                        meta = SOUTH_WEST
                                    }

                                    if (l1.toDouble() == position.getX() + (j3 - 1) && i2 == k1) {
                                        meta = SOUTH_EAST
                                    }

                                    if (l1 == l3 && i2.toDouble() == position.getZ() + (j3 - 1)) {
                                        meta = SOUTH_EAST
                                    }
                                }

                                if (meta == CENTER && l2 < position.getY() + i) {
                                    meta = ALL_INSIDE
                                }

                                if (position.getY() >= position.getY() + i - 1 || meta != ALL_INSIDE) {
                                    val blockPos = Vector3(l1.toDouble(), l2.toDouble(), i2.toDouble())

                                    if (Block.get(
                                            level.getBlockIdAt(
                                                blockPos.floorX,
                                                blockPos.floorY,
                                                blockPos.floorZ
                                            )
                                        ) !is BlockSolid
                                    ) {
                                        mushroom.setPropertyValue(CommonBlockProperties.HUGE_MUSHROOM_BITS, meta)
                                        level.setBlockStateAt(blockPos, mushroom.blockState)
                                    }
                                }
                            }
                        }
                        ++l2
                    }

                    for (i3 in 0..<i) {
                        val pos = position.up(i3)
                        val identifier = level.getBlockIdAt(pos.floorX, pos.floorY, pos.floorZ)

                        if (Block.get(identifier) !is BlockSolid) {
                            mushroom.setPropertyValue(CommonBlockProperties.HUGE_MUSHROOM_BITS, STEM)
                            level.setBlockStateAt(pos, mushroom.blockState)
                        }
                    }

                    return true
                }
            }
        } else {
            return false
        }
    }

    enum class MushroomType {
        RED,
        BROWN
    }

    companion object {
        const val NORTH_WEST: Int = 1
        const val NORTH: Int = 2
        const val NORTH_EAST: Int = 3
        const val WEST: Int = 4
        const val CENTER: Int = 5
        const val EAST: Int = 6
        const val SOUTH_WEST: Int = 7
        const val SOUTH: Int = 8
        const val SOUTH_EAST: Int = 9
        const val STEM: Int = 10
        const val ALL_INSIDE: Int = 0
    }
}
