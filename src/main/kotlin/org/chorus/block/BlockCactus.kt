package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.Server.Companion.instance
import cn.nukkit.block.BlockFlowerPot.FlowerPotBlock
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.entity.Entity
import cn.nukkit.event.block.BlockGrowEvent
import cn.nukkit.event.entity.EntityDamageByBlockEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.item.*
import cn.nukkit.item.Item.Companion.get
import cn.nukkit.level.Level
import cn.nukkit.math.*
import cn.nukkit.math.BlockFace.Companion.fromIndex
import cn.nukkit.tags.ItemTags
import cn.nukkit.tags.ItemTags.getItemSet

class BlockCactus @JvmOverloads constructor(state: BlockState? = Companion.properties.defaultState) :
    BlockTransparent(state), FlowerPotBlock, Natural {
    override val hardness: Double
        get() = 0.4

    override val resistance: Double
        get() = 2.0

    override val waterloggingLevel: Int
        get() = 1

    override fun hasEntityCollision(): Boolean {
        return true
    }

    override var minX: Double
        get() = position.x + 0.0625
        set(minX) {
            super.minX = minX
        }

    override var minY: Double
        get() = position.y
        set(minY) {
            super.minY = minY
        }

    override var minZ: Double
        get() = position.z + 0.0625
        set(minZ) {
            super.minZ = minZ
        }

    override var maxX: Double
        get() = position.x + 0.9375
        set(maxX) {
            super.maxX = maxX
        }

    override var maxY: Double
        get() = position.y + 0.9375
        set(maxY) {
            super.maxY = maxY
        }

    override var maxZ: Double
        get() = position.z + 0.9375
        set(maxZ) {
            super.maxZ = maxZ
        }

    override fun recalculateCollisionBoundingBox(): AxisAlignedBB? {
        return SimpleAxisAlignedBB(
            position.x,
            position.y,
            position.z, position.x + 1, position.y + 1, position.z + 1
        )
    }

    override fun onEntityCollide(entity: Entity) {
        entity.attack(EntityDamageByBlockEvent(this, entity, DamageCause.CONTACT, 1f))
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            val down = down()
            if (!getItemSet(ItemTags.SAND).contains(down!!.id)
                && down !is BlockCactus
            ) {
                level.useBreakOn(this.position)
                return 0
            }
            for (side in 2..5) {
                val block = getSide(fromIndex(side)!!)
                if (!block!!.canBeFlowedInto()) {
                    level.useBreakOn(this.position)
                }
            }
            return 0
        }
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (down() is BlockCactus) {
                return 0
            }
            if (this.age < maxAge) {
                this.age = this.age + 1
                level.setBlock(this.position, this)
                return 0
            }
            for (y in 1..2) {
                val b = level.getBlock(
                    Vector3(
                        position.x,
                        position.y + y, position.z
                    )
                )
                if (!b!!.isAir) {
                    continue
                }
                val event = BlockGrowEvent(b, get(CACTUS))
                instance!!.pluginManager.callEvent(event)
                if (!event.isCancelled) {
                    level.setBlock(b.position, event.newState!!, true)
                }
                break
            }
            this.age = minAge
        }

        return 0
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
        val down = this.down()
        if (!getItemSet(ItemTags.SAND.toString()).contains(down!!.id)
            && down !is BlockCactus
        ) {
            return false
        }
        val block0 = north()
        val block1 = south()
        val block2 = west()
        val block3 = east()
        if (block0!!.canBeFlowedInto() && block1!!.canBeFlowedInto() && block2!!.canBeFlowedInto() && block3!!.canBeFlowedInto()) {
            level.setBlock(this.position, this, true)
            return true
        }
        return false
    }

    override val name: String
        get() = "Cactus"

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(
            get(CACTUS, 0, 1)
        )
    }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun sticksToPiston(): Boolean {
        return false
    }

    var age: Int
        get() = this.getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.AGE_16)
        set(age) {
            this.setPropertyValue<Int, IntPropertyType>(
                CommonBlockProperties.AGE_16,
                age
            )
        }

    companion object {
        val properties: BlockProperties = BlockProperties(
            CACTUS,
            CommonBlockProperties.AGE_16
        )
            get() = Companion.field

        val maxAge: Int
            get() = CommonBlockProperties.AGE_16.getMax()

        val minAge: Int
            get() = CommonBlockProperties.AGE_16.getMin()
    }
}
