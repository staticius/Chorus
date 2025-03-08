package cn.nukkit.item

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.Entity.Companion.createEntity
import cn.nukkit.event.entity.CreatureSpawnEvent
import cn.nukkit.event.entity.CreatureSpawnEvent.SpawnReason
import cn.nukkit.level.Level
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.BlockFace
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.registry.Registries
import java.util.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
open class ItemSpawnEgg : Item {
    @JvmOverloads
    constructor(meta: Int = 0, count: Int = 1) : super(ItemID.Companion.SPAWN_EGG, meta, count, "Spawn Egg") {
        updateName()
    }

    constructor(id: String) : super(id, 0, 1)

    override var damage: Int
        get() = super.damage
        set(meta) {
            super.setDamage(meta)
            updateName()
        }

    protected fun updateName() {
        val entityName = entityName
        name = if (entityName == null) {
            "Spawn Egg"
        } else {
            "$entityName Spawn Egg"
        }
    }

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
        if (player.isAdventure) {
            return false
        }

        val chunk = level.getChunk(block.x.toInt() shr 4, block.z.toInt() shr 4) ?: return false

        val nbt = CompoundTag()
            .putList(
                "Pos", ListTag<FloatTag>()
                    .add(FloatTag(block.x + 0.5))
                    .add(FloatTag(if (target.boundingBox == null) block.y else target.boundingBox.maxY + 0.0001f))
                    .add(FloatTag(block.z + 0.5))
            )
            .putList(
                "Motion", ListTag<FloatTag>()
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
            )
            .putList(
                "Rotation", ListTag<FloatTag>()
                    .add(FloatTag(Random().nextFloat() * 360))
                    .add(FloatTag(0f))
            )

        if (this.hasCustomName()) {
            nbt.putString("CustomName", this.customName)
        }

        val networkId = entityNetworkId
        val ev = CreatureSpawnEvent(networkId, block, nbt, SpawnReason.SPAWN_EGG)
        level.server.pluginManager.callEvent(ev)

        if (ev.isCancelled) {
            return false
        }

        val entity = createEntity(networkId, chunk, nbt)

        if (entity != null) {
            if (player.isSurvival) {
                player.getInventory().decreaseCount(player.getInventory().heldItemIndex)
            }
            entity.spawnToAll()

            level.vibrationManager.callVibrationEvent(
                VibrationEvent(
                    player,
                    entity.position.clone(),
                    VibrationType.ENTITY_PLACE
                )
            )

            return true
        }

        return false
    }

    open val entityNetworkId: Int
        get() = this.meta

    val entityName: String?
        get() {
            val entityIdentifier =
                Registries.ENTITY.getEntityIdentifier(entityNetworkId)
            val path =
                entityIdentifier.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val result = StringBuilder()
            val parts =
                path.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (part in parts) {
                if (!part.isEmpty()) {
                    result.append(part[0].uppercaseChar()).append(part.substring(1)).append(" ")
                }
            }
            return result.toString().trim { it <= ' ' }
        }
}
