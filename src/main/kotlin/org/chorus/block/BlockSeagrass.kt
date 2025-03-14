package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemBlock
import org.chorus.item.ItemTool
import org.chorus.level.Level
import org.chorus.level.particle.BoneMealParticle
import org.chorus.math.BlockFace
import org.chorus.utils.BlockColor.equals

class BlockSeagrass @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockFlowable(blockstate) {
    override val name: String
        get() = "Seagrass"

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
        val down = down()
        val layer1Block = block.getLevelBlockAtLayer(1)
        val waterDamage: Int
        if (down!!.isSolid && (down.id != BlockID.MAGMA) && (down.id != BlockID.SOUL_SAND) &&
            (layer1Block is BlockFlowingWater && ((layer1Block.liquidDepth.also {
                waterDamage = it
            }) == 0 || waterDamage == 8))
        ) {
            if (waterDamage == 8) {
                level.setBlock(this.position, 1, BlockFlowingWater(), true, false)
            }
            level.setBlock(this.position, 0, this, true, true)
            return true
        }
        return false
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            val blockLayer1 = getLevelBlockAtLayer(1)
            val damage: Int
            if (blockLayer1 !is BlockFrostedIce && (blockLayer1 !is BlockFlowingWater || ((blockLayer1.blockState!!.specialValue()
                    .also {
                        damage =
                            it.toInt()
                    }) != 0 && damage != 8))
            ) {
                level.useBreakOn(this.position)
                return Level.BLOCK_UPDATE_NORMAL
            }

            val down = down()
            val propertyValue: SeaGrassType = getPropertyValue(CommonBlockProperties.SEA_GRASS_TYPE)
            if (propertyValue == SeaGrassType.DEFAULT || propertyValue == SeaGrassType.DOUBLE_BOT) {
                if (!down!!.isSolid || down.id == BlockID.MAGMA || down.id == BlockID.SOUL_SAND) {
                    level.useBreakOn(this.position)
                    return Level.BLOCK_UPDATE_NORMAL
                }

                if (propertyValue == SeaGrassType.DOUBLE_BOT) {
                    val up = up()
                    if (up!!.id != id || up.getPropertyValue<SeaGrassType, EnumPropertyType<SeaGrassType>>(
                            CommonBlockProperties.SEA_GRASS_TYPE
                        ) != SeaGrassType.DOUBLE_TOP
                    ) {
                        level.useBreakOn(this.position)
                    }
                }
            } else if (down!!.id != id || down.getPropertyValue<SeaGrassType, EnumPropertyType<SeaGrassType>>(
                    CommonBlockProperties.SEA_GRASS_TYPE
                ) != SeaGrassType.DOUBLE_BOT
            ) {
                level.useBreakOn(this.position)
            }

            return Level.BLOCK_UPDATE_NORMAL
        }

        return 0
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
        if (getPropertyValue<SeaGrassType, EnumPropertyType<SeaGrassType>>(CommonBlockProperties.SEA_GRASS_TYPE) == SeaGrassType.DEFAULT && item.isFertilizer) {
            val up = this.up()
            val damage: Int
            if (up is BlockFlowingWater && ((up.liquidDepth.also { damage = it }) == 0 || damage == 8)) {
                if (player != null && (player.gamemode and 0x01) == 0) {
                    item.count--
                }

                level.addParticle(BoneMealParticle(this.position))
                level.setBlock(
                    this.position, BlockSeagrass().setPropertyValue<SeaGrassType, EnumPropertyType<SeaGrassType>>(
                        CommonBlockProperties.SEA_GRASS_TYPE, SeaGrassType.DOUBLE_BOT
                    ), true, false
                )
                level.setBlock(up.position, 1, up, true, false)
                level.setBlock(
                    up.position, 0, BlockSeagrass().setPropertyValue<SeaGrassType, EnumPropertyType<SeaGrassType>>(
                        CommonBlockProperties.SEA_GRASS_TYPE, SeaGrassType.DOUBLE_TOP
                    ), true
                )
                return true
            }
        }

        return false
    }

    override fun getDrops(item: Item): Array<Item> {
        return if (item.isShears) {
            arrayOf(toItem())
        } else {
            Item.EMPTY_ARRAY
        }
    }

    override fun canBeReplaced(): Boolean {
        return true
    }

    override val waterloggingLevel: Int
        get() = 2

    override val toolType: Int
        get() = ItemTool.TYPE_SHEARS

    override fun toItem(): Item {
        return ItemBlock(BlockSeagrass(), 0)
    }

    override val isFertilizable: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SEAGRASS, CommonBlockProperties.SEA_GRASS_TYPE)

    }
}
