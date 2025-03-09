package org.chorus.network.protocol

import org.chorus.math.*
import org.chorus.math.Vector3.floorX
import org.chorus.math.Vector3.floorY
import org.chorus.math.Vector3.floorZ
import org.chorus.nbt.tag.ListTag.size
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.utils.*
import io.netty.util.internal.EmptyArrays
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import lombok.*
import java.awt.Color
import java.awt.image.BufferedImage

/**
 * @author CreeperFace
 * @since 5.3.2017
 */





class ClientboundMapItemDataPacket : DataPacket() {
    var eids: LongArray = EMPTY_LONGS

    var mapId: Long = 0
    var update: Int = 0
    var scale: Byte = 0
    var isLocked: Boolean = false
    var width: Int = 0
    var height: Int = 0
    var offsetX: Int = 0
    var offsetZ: Int = 0

    var dimensionId: Byte = 0
    var origin: BlockVector3 = BlockVector3()

    val trackedObjects: List<MapTrackedObject> = ObjectArrayList()
    var decorators: Array<MapDecorator?> = MapDecorator.EMPTY_ARRAY
    var colors: IntArray = EmptyArrays.EMPTY_INTS
    var image: BufferedImage? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityUniqueId(mapId)

        var update = 0
        if (image != null || colors.size > 0) {
            update = update or TEXTURE_UPDATE
        }
        if (decorators.size > 0 || !trackedObjects.isEmpty()) {
            update = update or DECORATIONS_UPDATE
        }
        if (eids.size > 0) {
            update = update or ENTITIES_UPDATE
        }

        byteBuf.writeUnsignedVarInt(update)
        byteBuf.writeByte(dimensionId.toInt())
        byteBuf.writeBoolean(this.isLocked)
        byteBuf.writeBlockVector3(this.origin)

        if ((update and ENTITIES_UPDATE) != 0) { //TODO: find out what these are for
            byteBuf.writeUnsignedVarInt(eids.size)
            for (eid in eids) {
                byteBuf.writeEntityUniqueId(eid)
            }
        }
        if ((update and (ENTITIES_UPDATE or TEXTURE_UPDATE or DECORATIONS_UPDATE)) != 0) {
            byteBuf.writeByte(scale.toInt())
        }

        if ((update and DECORATIONS_UPDATE) != 0) {
            byteBuf.writeUnsignedVarInt(trackedObjects.size())
            for (`object` in this.trackedObjects) {
                when (`object`.getType()) {
                    MapTrackedObject.Type.BLOCK -> {
                        byteBuf.writeIntLE(`object`.getType().ordinal())
                        byteBuf.writeBlockVector3(
                            `object`.getPosition().floorX,
                            `object`.getPosition().floorY,
                            `object`.getPosition().floorZ
                        )
                    }

                    MapTrackedObject.Type.ENTITY -> {
                        byteBuf.writeIntLE(`object`.getType().ordinal())
                        byteBuf.writeEntityUniqueId(`object`.getEntityId())
                    }
                }
            }

            byteBuf.writeUnsignedVarInt(decorators.size)
            for (decorator in decorators) {
                byteBuf.writeByte(decorator.rotation.toInt())
                byteBuf.writeByte(decorator.icon.toInt())
                byteBuf.writeByte(decorator.offsetX.toInt())
                byteBuf.writeByte(decorator.offsetZ.toInt())
                byteBuf.writeString(decorator.label!!)
                byteBuf.writeVarInt(decorator.color!!.rgb)
            }
        }

        if ((update and TEXTURE_UPDATE) != 0) {
            byteBuf.writeVarInt(width)
            byteBuf.writeVarInt(height)
            byteBuf.writeVarInt(offsetX)
            byteBuf.writeVarInt(offsetZ)

            if (image != null) {
                byteBuf.writeUnsignedVarInt(width * height)
                for (y in 0..<width) {
                    for (x in 0..<height) {
                        byteBuf.writeUnsignedVarInt(Utils.toABGR(image!!.getRGB(x, y)).toInt())
                    }
                }

                image!!.flush()
            } else if (colors.size > 0) {
                byteBuf.writeUnsignedVarInt(colors.size)
                for (color in colors) {
                    byteBuf.writeUnsignedVarInt(color)
                }
            }
        }
    }

    class MapDecorator {
        var rotation: Byte = 0
        var icon: Byte = 0
        var offsetX: Byte = 0
        var offsetZ: Byte = 0
        var label: String? = null
        var color: Color? = null

        companion object {
            val EMPTY_ARRAY: Array<MapDecorator?> = arrayOfNulls(0)
        }
    }

    
    class MapTrackedObject {
        private val type: Type
        private var entityId: Long = 0
        private var position: Vector3? = null

        constructor(entityId: Long) {
            this.type = Type.ENTITY
            this.entityId = entityId
        }

        constructor(position: Vector3?) {
            this.type = Type.BLOCK
            this.position = position
        }

        enum class Type {
            ENTITY,
            BLOCK
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CLIENTBOUND_MAP_ITEM_DATA_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        val EMPTY_LONGS: LongArray = LongArray(0)

        const val TEXTURE_UPDATE: Int = 0x02
        const val DECORATIONS_UPDATE: Int = 0x04
        const val ENTITIES_UPDATE: Int = 0x08
    }
}
