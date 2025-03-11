package org.chorus.item

import org.chorus.Player
import org.chorus.Server
import org.chorus.level.Level
import org.chorus.math.*
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.ClientboundMapItemDataPacket
import org.chorus.plugin.InternalPlugin

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


class ItemFilledMap @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.FILLED_MAP, meta, count, "Map") {
    // not very pretty but definitely better than before.
    private var image: BufferedImage? = null

    init {
        updateName()
        if (!hasCompoundTag() || !namedTag.contains("map_uuid")) {
            val tag = CompoundTag()
            tag.putLong("map_uuid", (mapCount++).toLong())
            this.setNamedTag(tag)
        }
    }

    override var damage: Int
        get() = super.damage
        set(meta) {
            super.setDamage(meta)
            updateName()
        }

    private fun updateName() {
        when (meta) {
            3 -> this.name = "Ocean Explorer Map"
            4 -> this.name = "Woodland Explorer Map"
            5 -> this.name = "Treasure Map"
            else -> this.name = "Map"
        }
    }

    @Throws(IOException::class)
    fun setImage(file: File) {
        setImage(ImageIO.read(file))
    }

    fun setImage(image: BufferedImage) {
        try {
            if (image.height != 128 || image.width != 128) { //resize
                this.image = BufferedImage(128, 128, image.type)
                val g = this.image!!.createGraphics()
                g.drawImage(image, 0, 0, 128, 128, null)
                g.dispose()
            } else {
                this.image = image
            }

            val baos = ByteArrayOutputStream()
            ImageIO.write(this.image, "png", baos)

            this.setNamedTag(this.namedTag.putByteArray("Colors", baos.toByteArray()))
        } catch (e: IOException) {
            ItemFilledMap.log.error("Error while adding an image to an ItemMap", e)
        }
    }

    protected fun loadImageFromNBT(): BufferedImage? {
        try {
            val data = namedTag.getByteArray("Colors")
            image = ImageIO.read(ByteArrayInputStream(data))
            return image
        } catch (e: IOException) {
            ItemFilledMap.log.error("Error while loading an image of an ItemMap from NBT", e)
        }

        return null
    }

    val mapId: Long
        get() = namedTag.getLong("map_uuid")

    val mapWorld: Int
        get() = namedTag.getInt("map_level")

    val mapStartX: Int
        get() = namedTag.getInt("map_startX")

    val mapStartZ: Int
        get() = namedTag.getInt("map_startZ")

    val mapScale: Int
        get() = namedTag.getInt("map_scale")

    @JvmOverloads
    fun sendImage(player: Player, scale: Int = 1) {
        // don't load the image from NBT if it has been done before.
        val image = if (this.image != null) this.image else loadImageFromNBT()

        val pk = ClientboundMapItemDataPacket()
        pk.eids = longArrayOf(mapId)
        pk.mapId = mapId
        pk.update = 2
        pk.scale = (scale - 1).toByte()
        pk.width = 128
        pk.height = 128
        pk.offsetX = 0
        pk.offsetZ = 0
        pk.image = image

        player.dataPacket(pk)
        player.level!!.scheduler.scheduleDelayedTask(
            InternalPlugin.INSTANCE,
            { player.dataPacket(pk) }, 20
        )
    }

    fun trySendImage(p: Player): Boolean {
        val image = if (this.image != null) this.image else loadImageFromNBT()
        if (image == null) return false
        this.sendImage(p)
        return true
    }

    @JvmOverloads
    fun renderMap(level: Level, startX: Int, startZ: Int, zoom: Int = 1) {
        require(zoom >= 1) { "Zoom must be greater than 0" }
        val pixels = IntArray(128 * 128)
        try {
            var z = 0
            while (z < 128 * zoom) {
                var x = 0
                while (x < 128 * zoom) {
                    pixels[(z * 128 + x) / zoom] = level.getMapColorAt(startX + x, startZ + z).argb
                    x += zoom
                }
                z += zoom
            }
            val image = BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB)
            image.setRGB(0, 0, 128, 128, pixels, 0, 128)

            this.setNamedTag(this.namedTag.putInt("map_level", level.id))
            this.setNamedTag(this.namedTag.putInt("map_startX", startX))
            this.setNamedTag(this.namedTag.putInt("map_startZ", startZ))
            this.setNamedTag(this.namedTag.putInt("map_scale", zoom))

            setImage(image)
        } catch (ex: Exception) {
            ItemFilledMap.log.warn("There was an error while generating map image", ex)
        }
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override val maxStackSize: Int
        get() = 1

    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        if (getDamage() == 6) return false
        val server: Server = Server.instance
        renderMap(server.getLevel(mapWorld), mapStartX, mapStartZ, mapScale)
        player.getInventory().setItemInHand(this)
        sendImage(player, mapScale)
        return true
    }

    companion object {
        var mapCount: Int = 0
    }
}