package org.chorus.level.generator.`object`

import org.chorus.block.*
import org.chorus.block.property.CommonBlockProperties
import org.chorus.math.Vector3

class ObjectSavannaTree : TreeGenerator() {
    /**
     * The metadata value of the wood to use in tree generation.
     */
    private val TRUNK: BlockState =
        BlockAcaciaWood.properties.getBlockState<BlockFace.Axis, EnumPropertyType<BlockFace.Axis>>(
            CommonBlockProperties.PILLAR_AXIS,
            BlockFace.Axis.Y
        )

    /**
     * The metadata value of the leaves to use in tree generation.
     */
    private val LEAF: BlockState = BlockAcaciaLeaves.properties.getDefaultState()

    override fun generate(level: BlockManager, rand: RandomSourceProvider, position: Vector3): Boolean {
        val i: Int = rand.nextInt(3) + rand.nextInt(3) + 5
        var flag = true

        if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
            var j = position.getY().toInt()
            while (j <= position.getY() + 1 + i) {
                var k = 1

                if (j.toDouble() == position.getY()) {
                    k = 0
                }

                if (j >= position.getY() + 1 + i - 2) {
                    k = 2
                }

                val vector3 = Vector3()

                var l = position.getX().toInt() - k
                while (l <= position.getX() + k && flag) {
                    var i1 = position.getZ().toInt() - k
                    while (i1 <= position.getZ() + k && flag) {
                        if (j >= 0 && j < 256) {
                            vector3.setComponents(l.toDouble(), j.toDouble(), i1.toDouble())
                            if (!this.canGrowInto(
                                    level.getBlockIdAt(
                                        vector3.x.toInt(),
                                        vector3.y.toInt(),
                                        vector3.z.toInt()
                                    )
                                )
                            ) {
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
                val down = position.down()
                val block = level.getBlockIdAt(down.floorX, down.floorY, down.floorZ)

                if ((block == GRASS_BLOCK || block == DIRT) && position.getY() < 256 - i - 1) {
                    this.setDirtAt(level, position.down())
                    val face: BlockFace = BlockFace.Plane.HORIZONTAL.random(rand)
                    val k2: Int = i - rand.nextInt(4) - 1
                    var l2: Int = 3 - rand.nextInt(3)
                    var i3 = position.floorX
                    var j1 = position.floorZ
                    var k1 = 0

                    for (l1 in 0..<i) {
                        val i2 = position.floorY + l1

                        if (l1 >= k2 && l2 > 0) {
                            i3 += face.getXOffset()
                            j1 += face.getZOffset()
                            --l2
                        }

                        val blockpos = Vector3(i3.toDouble(), i2.toDouble(), j1.toDouble())
                        val b = level.getBlockAt(blockpos.floorX, blockpos.floorY, blockpos.floorZ)
                        val material = b!!.id
                        if (material == AIR || b is BlockLeaves) {
                            this.placeLogAt(level, blockpos)
                            k1 = i2
                        }
                    }

                    var blockpos2 = Vector3(i3.toDouble(), k1.toDouble(), j1.toDouble())

                    for (j3 in -3..3) {
                        for (i4 in -3..3) {
                            if (Math.abs(j3) != 3 || Math.abs(i4) != 3) {
                                this.placeLeafAt(level, blockpos2.add(j3.toDouble(), 0.0, i4.toDouble()))
                            }
                        }
                    }

                    blockpos2 = blockpos2.up()

                    for (k3 in -1..1) {
                        for (j4 in -1..1) {
                            this.placeLeafAt(level, blockpos2.add(k3.toDouble(), 0.0, j4.toDouble()))
                        }
                    }

                    this.placeLeafAt(level, blockpos2.east(2))
                    this.placeLeafAt(level, blockpos2.west(2))
                    this.placeLeafAt(level, blockpos2.south(2))
                    this.placeLeafAt(level, blockpos2.north(2))
                    i3 = position.floorX
                    j1 = position.floorZ
                    val face1: BlockFace = BlockFace.Plane.HORIZONTAL.random(rand)

                    if (face1 != face) {
                        val l3: Int = k2 - rand.nextInt(2) - 1
                        var k4: Int = 1 + rand.nextInt(3)
                        k1 = 0

                        var l4 = l3
                        while (l4 < i && k4 > 0) {
                            if (l4 >= 1) {
                                val j2 = position.floorY + l4
                                i3 += face1.getXOffset()
                                j1 += face1.getZOffset()
                                val blockpos1 = Vector3(i3.toDouble(), j2.toDouble(), j1.toDouble())
                                val b = level.getBlockAt(blockpos1.floorX, blockpos1.floorY, blockpos1.floorZ)
                                val material1 = b!!.id

                                if (material1 == AIR || b is BlockLeaves) {
                                    this.placeLogAt(level, blockpos1)
                                    k1 = j2
                                }
                            }

                            ++l4
                            --k4
                        }

                        if (k1 > 0) {
                            var blockpos3 = Vector3(i3.toDouble(), k1.toDouble(), j1.toDouble())

                            for (i5 in -2..2) {
                                for (k5 in -2..2) {
                                    if (Math.abs(i5) != 2 || Math.abs(k5) != 2) {
                                        this.placeLeafAt(level, blockpos3.add(i5.toDouble(), 0.0, k5.toDouble()))
                                    }
                                }
                            }

                            blockpos3 = blockpos3.up()

                            for (j5 in -1..1) {
                                for (l5 in -1..1) {
                                    this.placeLeafAt(level, blockpos3.add(j5.toDouble(), 0.0, l5.toDouble()))
                                }
                            }
                        }
                    }

                    return true
                } else {
                    return false
                }
            }
        } else {
            return false
        }
    }

    private fun placeLogAt(worldIn: BlockManager, pos: Vector3) {
        worldIn.setBlockStateAt(pos, TRUNK)
    }

    private fun placeLeafAt(worldIn: BlockManager, pos: Vector3) {
        val b = worldIn.getBlockAt(pos.floorX, pos.floorY, pos.floorZ)
        val material = b!!.id
        if (material == AIR || b is BlockLeaves) {
            worldIn.setBlockStateAt(pos, LEAF)
        }
    }
}
