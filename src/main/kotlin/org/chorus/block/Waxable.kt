package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.item.ItemID
import cn.nukkit.level.Locator
import cn.nukkit.level.particle.WaxOffParticle
import cn.nukkit.level.particle.WaxOnParticle
import cn.nukkit.math.BlockFace

/**
 * @author joserobjr
 * @since 2021-06-14
 */
interface Waxable {
    val locator: Locator

    fun onActivate(item: Item, player: Player?, blockFace: BlockFace?, fx: Float, fy: Float, fz: Float): Boolean {
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
