package org.chorus.block

import org.chorus.Player
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.level.Locator
import org.chorus.level.particle.WaxOffParticle
import org.chorus.level.particle.WaxOnParticle
import org.chorus.math.BlockFace

/**
 * @author joserobjr
 * @since 2021-06-14
 */
interface Waxable {
    val locator: Locator

    fun onActivate(item: Item, player: Player?, blockFace: BlockFace, fx: Float, fy: Float, fz: Float): Boolean {
        var waxed = isWaxed
        if ((item.id !== ItemID.HONEYCOMB || waxed) && (!item.isAxe || !waxed)) {
            return false
        }

        waxed = !waxed
        if (!setWaxed(waxed)) {
            return false
        }

        val location = if (this is Block) this else locator
        if (player == null || !player.isCreative) {
            if (waxed) {
                item.count--
            } else {
                item.useOn((if (this is Block) this else location.levelBlock)!!)
            }
        }
        location.level.addParticle(if (waxed) WaxOnParticle(location.position) else WaxOffParticle(location.position))
        return true
    }

    val isWaxed: Boolean

    fun setWaxed(waxed: Boolean): Boolean
}
