package org.chorus.block

import org.chorus.Player
import org.chorus.Server.Companion.instance
import org.chorus.block.property.CommonBlockProperties
import org.chorus.event.Event.isCancelled
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.level.Level
import org.chorus.math.BlockFace
import org.chorus.math.Vector3
import org.chorus.utils.random.ChorusRandom.nextInt

class BlockMycelium : BlockDirt {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Mycelium"

    override val toolType: Int
        get() = ItemTool.TYPE_SHOVEL

    override val hardness: Double
        get() = 0.6

    override val resistance: Double
        get() = 2.5

    override fun getDrops(item: Item): Array<Item> {
        return arrayOf<Item?>(
            ItemBlock(get(BlockID.DIRT))
        )
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (level.getFullLight(add(0.0, 1.0, 0.0).position) >= BlockCrops.minimumLightLevel) {
                //TODO: light levels
                val random: NukkitRandom = NukkitRandom()
                val x: Int = random.nextInt(position.floorX - 1, position.floorX + 1)
                val y: Int = random.nextInt(position.floorY - 1, position.floorY + 1)
                val z: Int = random.nextInt(position.floorZ - 1, position.floorZ + 1)
                val block = level.getBlock(Vector3(x.toDouble(), y.toDouble(), z.toDouble()))
                if (block!!.id == Block.DIRT && block.getPropertyValue<DirtType, EnumPropertyType<DirtType>>(
                        CommonBlockProperties.DIRT_TYPE
                    ) == DirtType.NORMAL
                ) {
                    if (block.up()!!.isTransparent) {
                        val ev: BlockSpreadEvent = BlockSpreadEvent(block, this, get(BlockID.MYCELIUM))
                        instance.pluginManager.callEvent(ev)
                        if (!ev.isCancelled) {
                            level.setBlock(block.position, ev.newState)
                        }
                    }
                }
            }
        }
        return 0
    }

    override fun canSilkTouch(): Boolean {
        return true
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (!up()!!.canBeReplaced()) {
            return false
        }

        if (item.isShovel) {
            item.useOn(this)
            level.setBlock(this.position, get(BlockID.GRASS_PATH))
            if (player != null) {
                player.level!!.addSound(player.position, Sound.USE_GRASS)
            }
            return true
        }
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MYCELIUM)

    }
}
