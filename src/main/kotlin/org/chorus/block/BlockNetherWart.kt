package org.chorus.block

import org.chorus.Player
import org.chorus.Server.Companion.instance
import org.chorus.block.property.CommonBlockProperties
import org.chorus.event.Event.isCancelled
import org.chorus.item.Item
import org.chorus.level.Level
import org.chorus.math.BlockFace
import java.util.*

class BlockNetherWart @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFlowable(blockstate) {
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
        val down = this.down()
        if (down!!.id == BlockID.SOUL_SAND) {
            level.setBlock(block.position, this, true, true)
            return true
        }
        return false
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (down()!!.id != BlockID.SOUL_SAND) {
                level.useBreakOn(this.position)
                return Level.BLOCK_UPDATE_NORMAL
            }
        } else if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (Random().nextInt(10) == 1) {
                if (this.age < 0x03) {
                    val block = clone() as BlockNetherWart
                    block.age = block.age + 1
                    val ev: BlockGrowEvent = BlockGrowEvent(this, block)
                    instance.pluginManager.callEvent(ev)

                    if (!ev.isCancelled) {
                        level.setBlock(this.position, ev.newState, true, true)
                    } else {
                        return Level.BLOCK_UPDATE_RANDOM
                    }
                }
            } else {
                return Level.BLOCK_UPDATE_RANDOM
            }
        }

        return 0
    }

    override val name: String
        get() = "Nether Wart Block"

    override fun getDrops(item: Item): Array<Item> {
        if (this.age == 0x03) {
            this.age = 0
            return arrayOf<Item?>(
                ItemBlock(this, 0, 2 + (Math.random() * ((4 - 2) + 1)).toInt())
            )
        } else {
            return arrayOf(
                toItem()
            )
        }
    }

    var age: Int
        get() = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.AGE_4)
        set(age) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.AGE_4, age)
        }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.NETHER_WART, CommonBlockProperties.AGE_4)

    }
}


