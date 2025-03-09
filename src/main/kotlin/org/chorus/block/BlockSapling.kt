package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.BlockFlower.Companion.isSupportValid
import cn.nukkit.block.BlockFlowerPot.FlowerPotBlock
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.event.Event.isCancelled
import cn.nukkit.event.level.StructureGrowEvent.blockList
import cn.nukkit.item.Item
import cn.nukkit.level.Level
import cn.nukkit.level.Locator.floorX
import cn.nukkit.level.Locator.floorY
import cn.nukkit.level.generator.`object`.BlockManager.applySubChunkUpdate
import cn.nukkit.level.generator.`object`.BlockManager.blocks
import cn.nukkit.level.generator.`object`.HugeTreesGenerator.ensureGrowable
import cn.nukkit.level.generator.`object`.ObjectGenerator.generate
import cn.nukkit.level.generator.`object`.legacytree.LegacyBigSpruceTree.placeObject
import cn.nukkit.level.generator.`object`.legacytree.LegacyBigSpruceTree.setRandomTreeHeight
import cn.nukkit.level.generator.`object`.legacytree.LegacyTreeGenerator.treeHeight
import cn.nukkit.level.particle.BoneMealParticle
import cn.nukkit.math.BlockFace
import cn.nukkit.math.Vector2
import cn.nukkit.math.Vector2.floorX
import cn.nukkit.math.Vector2.floorY
import cn.nukkit.math.Vector3
import cn.nukkit.math.Vector3.floorX
import cn.nukkit.math.Vector3.floorY
import cn.nukkit.utils.random.RandomSourceProvider.Companion.create
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * @author Angelic47 (Nukkit Project)
 */
abstract class BlockSapling(blockstate: BlockState?) : BlockFlowable(blockstate), FlowerPotBlock {
    abstract val woodType: WoodType?

    var isAged: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.AGE_BIT)
        set(aged) {
            setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.AGE_BIT, aged)
        }

    override val name: String
        get() = woodType!!.name + " Sapling"

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        if (isSupportValid(down()!!)) {
            level.setBlock(block.position, this, true, true)
            return true
        }

        return false
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (item.isFertilizer) { // BoneMeal
            if (player != null && !player.isCreative) {
                item.count--
            }

            level.addParticle(BoneMealParticle(this.position))
            if (ThreadLocalRandom.current().nextFloat() >= 0.45) {
                return true
            }

            this.grow()

            return true
        }
        return false
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!isSupportValid(down()!!)) {
                level.useBreakOn(this.position)
                return Level.BLOCK_UPDATE_NORMAL
            }
        } else if (type == Level.BLOCK_UPDATE_RANDOM) { //Growth
            if (ThreadLocalRandom.current().nextInt(1, 8) == 1 && level.getFullLight(
                    position.add(0.0, 1.0, 0.0)!!
                ) >= BlockCrops.minimumLightLevel
            ) {
                if (isAged) {
                    this.grow()
                } else {
                    isAged = true
                    level.setBlock(this.position, this, true)
                    return Level.BLOCK_UPDATE_RANDOM
                }
            } else {
                return Level.BLOCK_UPDATE_RANDOM
            }
        }
        return Level.BLOCK_UPDATE_NORMAL
    }

    private fun grow() {
        var generator: ObjectGenerator? = null
        var bigTree = false

        var vector3: Vector3? = Vector3(
            position.x,
            position.y - 1, position.z
        )

        when (woodType) {
            WoodType.JUNGLE -> {
                val vector2: Vector2?
                if ((findSaplings(WoodType.JUNGLE).also { vector2 = it }) != null) {
                    vector3 = position.add(vector2!!.floorX.toDouble(), 0.0, vector2.floorY.toDouble())
                    generator = ObjectJungleBigTree(
                        10, 20,
                        BlockJungleLog.properties.getBlockState(CommonBlockProperties.PILLAR_AXIS, BlockFace.Axis.Y),
                        BlockJungleLeaves.properties.getDefaultState()
                    )
                    bigTree = true
                }

                if (!bigTree) {
                    generator = NewJungleTree(4, 7)
                    vector3 = position.add(0.0, 0.0, 0.0)
                }
            }

            WoodType.ACACIA -> {
                generator = ObjectSavannaTree()
                vector3 = position.add(0.0, 0.0, 0.0)
            }

            WoodType.DARK_OAK -> {
                if ((findSaplings(WoodType.DARK_OAK).also { vector2 = it }) != null) {
                    vector3 = position.add(vector2.floorX.toDouble(), 0.0, vector2.floorY.toDouble())
                    generator = ObjectDarkOakTree()
                    bigTree = true
                }

                if (!bigTree) {
                    return
                }
            }

            WoodType.PALE_OAK -> {
                if ((findSaplings(WoodType.PALE_OAK).also { vector2 = it }) != null) {
                    vector3 = position.add(vector2.floorX.toDouble(), 0.0, vector2.floorY.toDouble())
                    generator = ObjectPaleOakTree()
                    bigTree = true
                }

                if (!bigTree) {
                    generator = ObjectSmallPaleOakTree(4, 7)
                    vector3 = position.add(0.0, 0.0, 0.0)
                }
            }

            WoodType.SPRUCE -> {
                if ((findSaplings(WoodType.SPRUCE).also { vector2 = it }) != null) {
                    vector3 = position.add(vector2.floorX.toDouble(), 0.0, vector2.floorY.toDouble())
                    generator = object : HugeTreesGenerator(0, 0, null, null) {
                        override fun generate(
                            level: BlockManager?,
                            rand: RandomSourceProvider?,
                            position: Vector3
                        ): Boolean {
                            val `object`: LegacyBigSpruceTree = LegacyBigSpruceTree(0.75f, 4)
                            `object`.setRandomTreeHeight(rand)
                            if (!this.ensureGrowable(level, rand, position, `object`.treeHeight)) {
                                return false
                            }
                            `object`.placeObject(level, position.floorX, position.floorY, position.floorZ, rand)
                            return true
                        }
                    }
                    bigTree = true
                }

                if (bigTree) {
                    break
                }
                val blockManager: BlockManager = BlockManager(this.level)
                LegacyTreeGenerator.growTree(
                    blockManager,
                    position.floorX,
                    position.floorY, position.floorZ, RandomSourceProvider.create(),
                    woodType, false
                )
                val ev: StructureGrowEvent = StructureGrowEvent(this, blockManager.blocks)
                level.server.pluginManager.callEvent(ev)
                if (ev.isCancelled) {
                    return
                }
                if (level.getBlock(vector3!!)!!.id == BlockID.DIRT_WITH_ROOTS) {
                    level.setBlock(vector3, get(BlockID.DIRT))
                }
                blockManager.applySubChunkUpdate(ev.blockList)
                //                for (Block block : ev.getBlockList()) {
//                    this.level.setBlock(block, block);
//                }
                return
            }

            else -> {
                val blockManager: BlockManager = BlockManager(this.level)
                LegacyTreeGenerator.growTree(
                    blockManager,
                    position.floorX,
                    position.floorY, position.floorZ, RandomSourceProvider.create(),
                    woodType, false
                )
                val ev: StructureGrowEvent = StructureGrowEvent(this, blockManager.blocks)
                level.server.pluginManager.callEvent(ev)
                if (ev.isCancelled) {
                    return
                }
                if (level.getBlock(vector3!!)!!.id == BlockID.DIRT_WITH_ROOTS) {
                    level.setBlock(vector3, get(BlockID.DIRT))
                }
                blockManager.applySubChunkUpdate(ev.blockList)
                return
            }
        }

        if (bigTree) {
            level.setBlock(vector3!!, get(BlockID.AIR), true, false)
            level.setBlock(vector3.add(1.0, 0.0, 0.0)!!, get(BlockID.AIR), true, false)
            level.setBlock(vector3.add(0.0, 0.0, 1.0)!!, get(BlockID.AIR), true, false)
            level.setBlock(vector3.add(1.0, 0.0, 1.0)!!, get(BlockID.AIR), true, false)
        } else {
            level.setBlock(this.position, get(BlockID.AIR), true, false)
        }

        val blockManager: BlockManager = BlockManager(this.level)
        val success: Boolean = generator.generate(blockManager, RandomSourceProvider.create(), vector3)
        val ev: StructureGrowEvent = StructureGrowEvent(this, blockManager.blocks)
        level.server.pluginManager.callEvent(ev)
        if (ev.isCancelled || !success) {
            if (bigTree) {
                level.setBlock(vector3!!, this, true, false)
                level.setBlock(vector3.add(1.0, 0.0, 0.0)!!, this, true, false)
                level.setBlock(vector3.add(0.0, 0.0, 1.0)!!, this, true, false)
                level.setBlock(vector3.add(1.0, 0.0, 1.0)!!, this, true, false)
            } else {
                level.setBlock(this.position, this, true, false)
            }
            return
        }

        if (level.getBlock(vector3!!)!!.id == BlockID.DIRT_WITH_ROOTS) {
            level.setBlock(vector3, get(BlockID.DIRT))
        }
        blockManager.applySubChunkUpdate(ev.blockList)
    }

    private fun findSaplings(type: WoodType): Vector2? {
        val validVectorsList: MutableList<List<Vector2>> = ArrayList()
        validVectorsList.add(Arrays.asList(Vector2(0.0, 0.0), Vector2(1.0, 0.0), Vector2(0.0, 1.0), Vector2(1.0, 1.0)))
        validVectorsList.add(
            Arrays.asList(
                Vector2(0.0, 0.0),
                Vector2(-1.0, 0.0),
                Vector2(0.0, -1.0),
                Vector2(-1.0, -1.0)
            )
        )
        validVectorsList.add(
            Arrays.asList(
                Vector2(0.0, 0.0),
                Vector2(1.0, 0.0),
                Vector2(0.0, -1.0),
                Vector2(1.0, -1.0)
            )
        )
        validVectorsList.add(
            Arrays.asList(
                Vector2(0.0, 0.0),
                Vector2(-1.0, 0.0),
                Vector2(0.0, 1.0),
                Vector2(-1.0, 1.0)
            )
        )
        for (validVectors in validVectorsList) {
            var correct = true
            for (vector2 in validVectors) {
                if (!this.isSameType(position.add(vector2.x, 0.0, vector2.y)!!, type)) correct = false
            }
            if (correct) {
                var lowestX = 0
                var lowestZ = 0
                for (vector2 in validVectors) {
                    if (vector2.floorX < lowestX) lowestX = vector2.floorX
                    if (vector2.floorY < lowestZ) lowestZ = vector2.floorY
                }
                return Vector2(lowestX.toDouble(), lowestZ.toDouble())
            }
        }
        return null
    }

    fun isSameType(pos: Vector3, type: WoodType): Boolean {
        val block = level.getBlock(pos)
        return block!!.id == this.id && (block as BlockSapling).woodType == type
    }

    override val isFertilizable: Boolean
        get() = true
}
