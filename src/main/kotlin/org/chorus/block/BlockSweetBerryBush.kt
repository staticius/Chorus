package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.Server.Companion.instance
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.entity.Entity
import cn.nukkit.event.Event.isCancelled
import cn.nukkit.item.Item
import cn.nukkit.level.Level
import cn.nukkit.level.particle.BoneMealParticle
import cn.nukkit.math.BlockFace
import cn.nukkit.math.MathHelper.clamp
import java.util.concurrent.ThreadLocalRandom

class BlockSweetBerryBush @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockFlowable(blockstate) {
    override val name: String
        get() = "Sweet Berry Bush"

    override val burnChance: Int
        get() = 30

    override val burnAbility: Int
        get() = 60

    override fun canBeActivated(): Boolean {
        return true
    }

    override val hardness: Double
        get() = if (growth == 0) 0.0 else 0.25

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        val age: Int = MathHelper.clamp(growth, 0, 3)

        if (age < 3 && item.isFertilizer) {
            val block = clone() as BlockSweetBerryBush
            block.setGrowth(block.growth + 1)
            if (block.growth > 3) {
                block.setGrowth(3)
            }
            val ev: BlockGrowEvent = BlockGrowEvent(this, block)
            instance!!.pluginManager.callEvent(ev)

            if (ev.isCancelled) {
                return false
            }

            level.setBlock(this.position, ev.newState, false, true)
            level.addParticle(BoneMealParticle(this.position))

            if (player != null && (player.gamemode and 0x01) == 0) {
                item.count--
            }

            return true
        }

        if (age < 2) {
            return true
        }

        var amount = 1 + ThreadLocalRandom.current().nextInt(2)
        if (age == 3) {
            amount++
        }

        val event: BlockHarvestEvent = BlockHarvestEvent(
            this,
            BlockSweetBerryBush().setGrowth(1),
            arrayOf<Item>(ItemSweetBerries(0, amount))
        )

        level.server.pluginManager.callEvent(event)
        if (!event.isCancelled) {
            level.setBlock(this.position, event.newState, true, true)
            val drops: Array<Item> = event.drops
            if (drops != null) {
                val dropPos = add(0.5, 0.5, 0.5)
                for (drop in drops) {
                    if (drop != null) {
                        level.dropItem(dropPos.position, drop)
                    }
                }
            }
        }

        return true
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!isSupportValid(down()!!)) {
                level.useBreakOn(this.position)
                return Level.BLOCK_UPDATE_NORMAL
            }
        } else if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (growth < 3 && ThreadLocalRandom.current().nextInt(5) == 0 && level.getFullLight(
                    position.add(0.0, 1.0, 0.0)!!
                ) >= BlockCrops.minimumLightLevel
            ) {
                val event: BlockGrowEvent = BlockGrowEvent(
                    this, get(id).setPropertyValue<Int, IntPropertyType>(
                        CommonBlockProperties.GROWTH, growth + 1
                    )
                )
                if (!event.isCancelled) {
                    level.setBlock(this.position, event.newState, true, true)
                }
            }
            return type
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
        if (target.id == BlockID.SWEET_BERRY_BUSH || !block.isAir) {
            return false
        }
        if (isSupportValid(down()!!)) {
            level.setBlock(block.position, this, true)
            return true
        }
        return false
    }

    override fun hasEntityCollision(): Boolean {
        return growth > 0
    }

    override fun onEntityCollide(entity: Entity) {
        if (growth > 0) {
            if (entity.positionChanged && !entity.isSneaking() && ThreadLocalRandom.current().nextInt(20) == 0) {
                if (entity.attack(EntityDamageByBlockEvent(this, entity, EntityDamageEvent.DamageCause.CONTACT, 1f))) {
                    level.addSound(entity.position, Sound.BLOCK_SWEET_BERRY_BUSH_HURT)
                }
            }
        }
    }

    override val collisionBoundingBox: AxisAlignedBB?
        get() = if (growth > 0) this else null

    override fun getDrops(item: Item): Array<Item?>? {
        val age: Int = MathHelper.clamp(growth, 0, 3)

        var amount = 1
        if (age > 1) {
            amount = 1 + ThreadLocalRandom.current().nextInt(2)
            if (age == 3) {
                amount++
            }
        }

        return arrayOf<Item?>(ItemSweetBerries(0, amount))
    }

    val growth: Int
        get() = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.GROWTH)

    fun setGrowth(growth: Int): Block {
        return setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.GROWTH, growth)
    }

    override fun toItem(): Item? {
        return ItemSweetBerries()
    }

    override val isFertilizable: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SWEET_BERRY_BUSH, CommonBlockProperties.GROWTH)
            get() = Companion.field

        fun isSupportValid(block: Block): Boolean {
            return block.`is`(BlockTags.DIRT)
        }
    }
}
