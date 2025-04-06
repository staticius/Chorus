package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.BooleanPropertyType
import org.chorus.entity.Entity
import org.chorus.entity.effect.EffectType
import org.chorus.event.block.BlockFormEvent
import org.chorus.event.entity.EntityDamageByBlockEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.item.enchantment.Enchantment
import org.chorus.level.GameRule
import org.chorus.level.Level

class BlockMagma : BlockSolid {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override val name: String
        get() = "Magma Block"

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val hardness: Double
        get() = 0.5

    override val resistance: Double
        get() = 30.0

    override val lightLevel: Int
        get() = 3

    override fun getDrops(item: Item): Array<Item> {
        return if (item.isPickaxe && item.tier >= ItemTool.TIER_WOODEN) {
            arrayOf(
                toItem()
            )
        } else {
            Item.EMPTY_ARRAY
        }
    }

    override fun onEntityCollide(entity: Entity) {
        if (entity.hasEffect(EffectType.FIRE_RESISTANCE)) {
            return
        }

        if (entity is Player) {
            if (entity.getInventory().boots.getEnchantment(Enchantment.ID_FROST_WALKER) != null || entity.isCreative || entity.isSpectator || entity.isSneaking() || !entity.level!!.gameRules.getBoolean(
                    GameRule.FIRE_DAMAGE
                )
            ) {
                return
            }
        }

        entity.attack(EntityDamageByBlockEvent(this, entity, EntityDamageEvent.DamageCause.HOT_FLOOR, 1f))
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            val up = up()
            if (up is BlockFlowingWater && (up.liquidDepth == 0 || up.liquidDepth == 8)) {
                val event = BlockFormEvent(
                    up,
                    BlockBubbleColumn().setPropertyValue<Boolean, BooleanPropertyType>(
                        CommonBlockProperties.DRAG_DOWN,
                        true
                    )
                )
                if (!event.isCancelled) {
                    if (event.newState.waterloggingLevel > 0) {
                        level.setBlock(up.position, 1, BlockFlowingWater(), true, false)
                    }
                    level.setBlock(up.position, 0, event.newState, true, true)
                }
            }
        }
        return 0
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGMA)
    }
}
