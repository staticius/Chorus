package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.entity.Entity
import cn.nukkit.entity.effect.EffectType
import cn.nukkit.event.block.BlockFormEvent
import cn.nukkit.event.entity.EntityDamageByBlockEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.item.Item
import cn.nukkit.item.ItemTool
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.level.GameRule
import cn.nukkit.level.Level

class BlockMagma : BlockSolid {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

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

    override fun getDrops(item: Item): Array<Item?>? {
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
            if (entity.getInventory().boots.getEnchantment(Enchantment.ID_FROST_WALKER) != null || entity.isCreative || entity.isSpectator || entity.isSneaking() || !entity.level!!.gameRules!!.getBoolean(
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
                    if (event.newState!!.waterloggingLevel > 0) {
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

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGMA)
            get() = Companion.field
    }
}
