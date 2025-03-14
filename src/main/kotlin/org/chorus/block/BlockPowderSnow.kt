package org.chorus.block

import org.chorus.entity.Entity
import org.chorus.entity.projectile.EntitySmallFireball
import org.chorus.item.Item
import org.chorus.level.Locator
import org.chorus.math.BlockFace
import org.chorus.math.Vector3

class BlockPowderSnow : BlockTransparent {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Powder Snow"

    override val hardness: Double
        get() = 0.25

    override val resistance: Double
        get() = 0.1

    override val isSolid: Boolean
        get() = false

    override fun isSolid(side: BlockFace): Boolean {
        return false
    }

    override val isTransparent: Boolean
        get() = true

    override fun canPassThrough(): Boolean {
        return true
    }

    override fun getDrops(item: Item): Array<Item?> {
        return Item.EMPTY_ARRAY
    }

    override fun onProjectileHit(projectile: Entity, locator: Locator, motion: Vector3): Boolean {
        if (projectile is EntitySmallFireball) {
            level.useBreakOn(this.position)
            return true
        }
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.POWDER_SNOW)

    }
}
