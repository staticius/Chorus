package cn.nukkit.event

/**
 * @author Nukkit Team.
 */
interface Cancellable {
    var isCancelled: Boolean

    fun setCancelled()
}
