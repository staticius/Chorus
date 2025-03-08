package cn.nukkit.item

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.Entity.Companion.createEntity
import cn.nukkit.entity.item.EntityMinecart
import cn.nukkit.level.Level
import cn.nukkit.math.BlockFace
import cn.nukkit.nbt.tag.*
import cn.nukkit.utils.Rail

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ItemMinecart @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.MINECART, meta, count, "Minecart") {
    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        level: Level,
        player: Player,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double
    ): Boolean {
        if (Rail.isRailBlock(target)) {
            val type = (target as BlockRail).orientation
            var adjacent = 0.0
            if (type.isAscending) {
                adjacent = 0.5
            }
            val minecart = createEntity(
                Entity.MINECART,
                level.getChunk(target.position.floorX shr 4, target.position.floorZ shr 4), CompoundTag()
                    .putList(
                        "Pos", ListTag<Tag>()
                            .add(FloatTag(target.getX() + 0.5))
                            .add(FloatTag(target.getY() + 0.0625 + adjacent))
                            .add(FloatTag(target.getZ() + 0.5))
                    )
                    .putList(
                        "Motion", ListTag<Tag>()
                            .add(FloatTag(0f))
                            .add(FloatTag(0f))
                            .add(FloatTag(0f))
                    )
                    .putList(
                        "Rotation", ListTag<Tag>()
                            .add(FloatTag(0f))
                            .add(FloatTag(0f))
                    )
            ) as EntityMinecart?

            if (minecart == null) {
                return false
            }

            if (player.isAdventure || player.isSurvival) {
                val item = player.getInventory().itemInHand
                item.setCount(item.getCount() - 1)
                player.getInventory().setItemInHand(item)
            }

            minecart.spawnToAll()
            return true
        }
        return false
    }

    override val maxStackSize: Int
        get() = 1
}
