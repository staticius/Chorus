package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.BlockFlowerPot.FlowerPotBlock
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.BambooLeafSize
import cn.nukkit.block.property.enums.BambooStalkThickness
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.event.block.BlockGrowEvent
import cn.nukkit.item.*
import cn.nukkit.level.*
import cn.nukkit.level.particle.BoneMealParticle
import cn.nukkit.math.BlockFace
import cn.nukkit.math.MathHelper.clamp
import cn.nukkit.network.protocol.AnimatePacket
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class BlockBamboo @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockTransparent(blockState), FlowerPotBlock {
    override val name: String
        get() = "Bamboo"

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (isSupportInvalid) {
                level.scheduleUpdate(this, 0)
            }
            return type
        } else if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            level.useBreakOn(this.position, null, null, true)
        } else if (type == Level.BLOCK_UPDATE_RANDOM) {
            val up = up()
            if (age == 0 && up!!.isAir && level.getFullLight(up.position) >= BlockCrops.minimumLightLevel && ThreadLocalRandom.current()
                    .nextInt(3) == 0
            ) {
                grow(up)
            }
            return type
        }
        return 0
    }

    fun grow(up: Block): Boolean {
        val newState = BlockBamboo()
        if (isThick) {
            newState.isThick = true
            newState.bambooLeafSize = BambooLeafSize.SMALL_LEAVES
        } else {
            newState.bambooLeafSize = BambooLeafSize.SMALL_LEAVES
        }
        val blockGrowEvent = BlockGrowEvent(up, newState)
        level.server.pluginManager.callEvent(blockGrowEvent)
        if (!blockGrowEvent.isCancelled) {
            val newState1 = blockGrowEvent.newState
            newState1!!.position.x = position.x
            newState1.position.y = up.position.y
            newState1.position.z = position.z
            newState1.level = level
            newState1.place(toItem()!!, up, this, BlockFace.DOWN, 0.5, 0.5, 0.5, null)
            return true
        }
        return false
    }

    fun countHeight(): Int {
        var count = 0
        var opt: Optional<Block?>
        var down: Block = this
        while ((down.down()!!.firstInLayers { b: Block? -> b!!.id === BAMBOO }.also { opt = it }).isPresent) {
            down = opt.get()
            if (++count >= 16) {
                break
            }
        }
        return count
    }

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
        var down = down()
        val downId = down!!.id
        if (downId != BAMBOO && downId != BAMBOO_SAPLING) {
            val sampling = BlockBambooSapling()
            sampling.position.x = position.x
            sampling.position.y = position.y
            sampling.position.z = position.z
            sampling.level = level
            return sampling.place(item, block, target, face, fx, fy, fz, player)
        }

        var canGrow = true

        if (downId == BAMBOO_SAPLING) {
            if (player != null) {
                val animatePacket = AnimatePacket()
                animatePacket.action = AnimatePacket.Action.SWING_ARM
                animatePacket.eid = player.getId()
                level.addChunkPacket(player.position.chunkX, player.position.chunkZ, animatePacket)
            }
            bambooLeafSize = BambooLeafSize.SMALL_LEAVES
        }
        if (down is BlockBamboo) {
            canGrow = down.age == 0
            val thick = down.isThick
            if (!thick) {
                var setThick = true
                for (i in 2..3) {
                    if (getSide(BlockFace.DOWN, i)!!.id !== BAMBOO) {
                        setThick = false
                    }
                }
                if (setThick) {
                    isThick = true
                    bambooLeafSize = BambooLeafSize.LARGE_LEAVES
                    down.bambooLeafSize = BambooLeafSize.SMALL_LEAVES
                    down.isThick = true
                    down.age = 1
                    level.setBlock(down.position, down, false, true)
                    while ((down!!.down().also { down = it }) is BlockBamboo) {
                        down = down as BlockBamboo
                        down.isThick = true
                        down.bambooLeafSize = BambooLeafSize.NO_LEAVES
                        down.age = 1
                        level.setBlock(down.position, down, false, true)
                    }
                } else {
                    bambooLeafSize = BambooLeafSize.SMALL_LEAVES
                    down.age = 1
                    level.setBlock(down.position, down, false, true)
                }
            } else {
                isThick = true
                bambooLeafSize = BambooLeafSize.LARGE_LEAVES
                age = 0
                down.bambooLeafSize = BambooLeafSize.LARGE_LEAVES
                down.age = 1
                level.setBlock(down.position, down, false, true)
                down = down.down()
                if (down is BlockBamboo) {
                    down = down as BlockBamboo
                    down.bambooLeafSize = BambooLeafSize.SMALL_LEAVES
                    down.age = 1
                    level.setBlock(down.position, down, false, true)
                    down = down.down()
                    if (down is BlockBamboo) {
                        down = down as BlockBamboo
                        down.bambooLeafSize = BambooLeafSize.NO_LEAVES
                        down.age = 1
                        level.setBlock(down.position, down, false, true)
                    }
                }
            }
        } else if (isSupportInvalid) {
            return false
        }

        val height = if (canGrow) countHeight() else 0
        if (!canGrow || height >= 15 || height >= 11 && ThreadLocalRandom.current().nextFloat() < 0.25f) {
            age = 1
        }

        level.setBlock(this.position, this, false, true)
        return true
    }

    override fun onBreak(item: Item?): Boolean {
        val down = down()!!.firstInLayers { b: Block? -> b is BlockBamboo }
        if (down.isPresent) {
            val bambooDown = down.get() as BlockBamboo
            val height = bambooDown.countHeight()
            if (height < 15 && (height < 11 || !(ThreadLocalRandom.current().nextFloat() < 0.25f))) {
                bambooDown.age = 0
                level.setBlock(bambooDown.position, bambooDown.layer, bambooDown, false, true)
            }
        }
        return super.onBreak(item)
    }

    override fun canPassThrough(): Boolean {
        return true
    }

    private val isSupportInvalid: Boolean
        get() = when (down()!!.id) {
            BAMBOO, DIRT, GRASS_BLOCK, SAND, GRAVEL, PODZOL, BAMBOO_SAPLING, MOSS_BLOCK -> false
            else -> true
        }

    override fun toItem(): Item? {
        return ItemBlock(BlockBamboo())
    }

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 5.0

    var isThick: Boolean
        get() = bambooStalkThickness == BambooStalkThickness.THICK
        set(thick) {
            bambooStalkThickness = if (thick) BambooStalkThickness.THICK else BambooStalkThickness.THIN
        }

    var bambooStalkThickness: BambooStalkThickness
        get() = getPropertyValue(CommonBlockProperties.BAMBOO_STALK_THICKNESS)
        set(value) {
            setPropertyValue(
                CommonBlockProperties.BAMBOO_STALK_THICKNESS,
                value
            )
        }

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    var bambooLeafSize: BambooLeafSize?
        get() = getPropertyValue(CommonBlockProperties.BAMBOO_LEAF_SIZE)
        set(bambooLeafSize) {
            setPropertyValue(
                CommonBlockProperties.BAMBOO_LEAF_SIZE,
                bambooLeafSize
            )
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
        if (item.isFertilizer) {
            var top = position.y.toInt()
            var count = 1

            for (i in 1..16) {
                val id = level.getBlockIdAt(
                    position.floorX,
                    position.floorY - i, position.floorZ
                )
                if (id == BAMBOO) {
                    count++
                } else {
                    break
                }
            }

            for (i in 1..16) {
                val id = level.getBlockIdAt(
                    position.floorX,
                    position.floorY + i, position.floorZ
                )
                if (id == BAMBOO) {
                    top++
                    count++
                } else {
                    break
                }
            }

            //15格以上需要嫁接（放置竹子）
            if (count >= 15) {
                return false
            }

            var success = false

            val block = this.up(top - position.y.toInt() + 1)
            if (block!!.id === AIR) {
                success = grow(block!!)
            }

            if (success) {
                if (player != null && player.isSurvival) {
                    item.count--
                }
                level.addParticle(BoneMealParticle(this.position))
            }

            return true
        }
        return false
    }

    var age: Int
        get() = if (getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.AGE_BIT)) 1 else 0
        set(age) {
            var age = age
            age = clamp(age, 0, 1)
            setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.AGE_BIT, age == 1)
        }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override val isFertilizable: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(
            BAMBOO,
            CommonBlockProperties.AGE_BIT,
            CommonBlockProperties.BAMBOO_LEAF_SIZE,
            CommonBlockProperties.BAMBOO_STALK_THICKNESS
        )
            get() = Companion.field
    }
}
