package cn.nukkit.inventory.fake

import cn.nukkit.Player
import cn.nukkit.entity.Entity.getLocator
import cn.nukkit.math.Vector3

/**
 * 假方块API，通过发送数据包欺骗客户端在指定位置存在某方块及其方块实体
 *
 *
 * Fake block API, which tricks the client into believing that a block and its BlockEntity exist at a specified location by sending packets
 */
interface FakeBlock {
    /**
     * 给某玩家发送该假方块及其对应方块实体.
     *
     *
     * Send the fake block and its corresponding BlockEntity to a player
     *
     * @param player the player
     */
    fun create(player: Player)

    /**
     * 给某玩家发送该假方块及其对应方块实体.
     *
     *
     * Send the fake block and its corresponding BlockEntity to a player
     *
     * @param player    the player
     * @param titleName 该方块实体的名称<br></br>the title name for blockentity
     */
    fun create(player: Player, titleName: String) {
        this.create(player)
    }

    /**
     * Remove it.
     *
     * @param player the player
     */
    fun remove(player: Player)

    fun getLastPositions(player: Player): HashSet<Vector3?>

    fun getPlacePositions(player: Player): List<Vector3?> {
        return java.util.List.of(getOffset(player))
    }

    /**
     * Get the place position of this fake block
     */
    fun getOffset(player: Player): Vector3 {
        val offset = player.getDirectionVector()
        offset.south *= -(1 + player.getWidth()).toDouble()
        offset.up *= -(1 + player.getHeight()).toDouble()
        offset.west *= -(1 + player.getWidth()).toDouble()
        return player.getLocator().position.add(offset)
    }
}
