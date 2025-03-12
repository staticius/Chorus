package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.entity.Entity
import org.chorus.entity.effect.EffectType
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.math.BlockFace
import kotlin.math.floor

class BlockHayBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val hardness: Double
        get() = 0.5

    override val resistance: Double
        get() = 0.5

    override val burnChance: Int
        get() = 60

    override val burnAbility: Int
        get() = 20

    override val toolType: Int
        get() = ItemTool.TYPE_HOE

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
        this.pillarAxis = face.axis
        level.setBlock(block.position, this, true)
        return true
    }

    var pillarAxis: BlockFace.Axis?
        get() = getPropertyValue(
            CommonBlockProperties.PILLAR_AXIS
        )
        set(axis) {
            setPropertyValue(
                CommonBlockProperties.PILLAR_AXIS,
                axis
            )
        }

    override fun useDefaultFallDamage(): Boolean {
        return false
    }

    override fun onEntityFallOn(entity: Entity, fallDistance: Float) {
        val jumpBoost = if (entity.hasEffect(EffectType.JUMP_BOOST)) get(EffectType.JUMP_BOOST).getLevel() else 0
        var damage = floor((fallDistance - 3 - jumpBoost).toDouble()).toFloat()

        damage *= 0.2f

        if (damage > 0) {
            entity.attack(EntityDamageEvent(entity, EntityDamageEvent.DamageCause.FALL, damage))
        }
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.HAY_BLOCK, CommonBlockProperties.DEPRECATED, CommonBlockProperties.PILLAR_AXIS)

    }
}