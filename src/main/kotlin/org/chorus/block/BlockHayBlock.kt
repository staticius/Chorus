package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.entity.Entity
import cn.nukkit.entity.effect.Effect.Companion.get
import cn.nukkit.entity.effect.EffectType
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.item.Item
import cn.nukkit.item.ItemTool
import cn.nukkit.math.BlockFace
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
            BlockProperties(HAY_BLOCK, CommonBlockProperties.DEPRECATED, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}