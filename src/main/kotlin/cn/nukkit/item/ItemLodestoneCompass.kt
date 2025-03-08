package cn.nukkit.item

import cn.nukkit.Server
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.positiontracking.NamedPosition
import java.io.IOException

class ItemLodestoneCompass @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.LODESTONE_COMPASS, meta, count, "Lodestone Compass") {
    override val maxStackSize: Int
        get() = 1

    @get:Throws(IOException::class)
    @set:Throws(IOException::class)
    var trackingPosition: NamedPosition?
        get() {
            val trackingHandle = trackingHandle
            if (trackingHandle == 0) {
                return null
            }
            return Server.getInstance().positionTrackingService.getPosition(trackingHandle)
        }
        set(position) {
            if (position == null) {
                trackingHandle = 0
                return
            }
            trackingHandle =
                Server.getInstance().positionTrackingService.addOrReusePosition(position)
        }

    var trackingHandle: Int
        get() = if (hasCompoundTag()) namedTag.getInt("trackingHandle") else 0
        set(trackingHandle) {
            var tag = namedTag
            if (tag == null) {
                tag = CompoundTag()
            }
            tag.putInt("trackingHandle", trackingHandle)
            namedTag = tag
        }
}