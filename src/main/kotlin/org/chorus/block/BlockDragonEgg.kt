package org.chorus.block

import org.chorus.Player
import org.chorus.event.block.BlockFromToEvent
import org.chorus.event.player.PlayerInteractEvent
import org.chorus.item.*
import org.chorus.level.Level
import org.chorus.math.*
import org.chorus.network.protocol.LevelEventPacket
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.abs

class BlockDragonEgg : BlockFallable {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Dragon Egg"

    override val hardness: Double
        get() = 3.0

    override val resistance: Double
        get() = 45.0

    override val lightLevel: Int
        get() = 1

    override val isTransparent: Boolean
        get() = true

    override val waterloggingLevel: Int
        get() = 1

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_TOUCH) {
            this.teleport()
        }
        return super.onUpdate(type)
    }

    override fun onTouch(
        vector: Vector3,
        item: Item,
        face: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float,
        player: Player?,
        action: PlayerInteractEvent.Action
    ) {
        if (player != null && (action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)) {
            if (player.isCreative && action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
                return
            }
            onUpdate(Level.BLOCK_UPDATE_TOUCH)
        }
    }

    fun teleport() {
        val random = ThreadLocalRandom.current()
        for (i in 0..999) {
            var to =
                level.getBlock(
                    position.add(
                        random.nextInt(-16, 16).toDouble(),
                        random.nextInt(0, 16).toDouble(),
                        random.nextInt(-16, 16).toDouble()
                    )!!
                )
            if (to!!.isAir) {
                val event = BlockFromToEvent(this, to)
                Server.instance.pluginManager.callEvent(event)
                if (event.isCancelled) return
                to = event.to

                val diffX = position.floorX - to.position.floorX
                val diffY = position.floorY - to.position.floorY
                val diffZ = position.floorZ - to.position.floorZ
                val pk = LevelEventPacket()
                pk.evid = LevelEventPacket.EVENT_PARTICLE_DRAGON_EGG
                pk.data =
                    (((((abs(diffX.toDouble()).toInt() shl 16) or (abs(diffY.toDouble()).toInt() shl 8)) or abs(diffZ.toDouble()).toInt()) or ((if (diffX < 0) 1 else 0) shl 24)) or ((if (diffY < 0) 1 else 0) shl 25)) or ((if (diffZ < 0) 1 else 0) shl 26)
                pk.x = position.floorX.toFloat()
                pk.y = position.floorY.toFloat()
                pk.z = position.floorZ.toFloat()
                level.addChunkPacket(
                    position.floorX shr 4,
                    position.floorZ shr 4, pk
                )
                level.setBlock(this.position, get(AIR), true)
                level.setBlock(to.position, this, true)
                return
            }
        }
    }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun sticksToPiston(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(DRAGON_EGG)
            get() = Companion.field
    }
}
