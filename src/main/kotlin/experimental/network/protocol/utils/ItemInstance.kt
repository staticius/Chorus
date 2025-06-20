package org.chorus_oss.chorus.experimental.network.protocol.utils

import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.item.ItemDurable
import org.chorus_oss.chorus.nbt.NBTIO.read
import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.protocol.types.item.ItemInstance
import java.nio.ByteOrder

fun ItemInstance.Companion.from(value: Item): ItemInstance {
    return ItemInstance(
        netID = when {
            value.isNothing -> 0
            else -> value.runtimeId
        },
        metadata = value.meta.toUInt(),
        blockRuntimeID = value.blockState?.blockStateHash() ?: 0,
        count = value.count.toUShort(),
        nbtData = when {
            value is ItemDurable && value.meta != 0 -> {
                val nbt = value.compoundTag
                val tag = if (nbt.isEmpty()) {
                    CompoundTag()
                } else {
                    read(nbt, ByteOrder.LITTLE_ENDIAN)
                }
                if (tag.contains("Damage")) {
                    tag.put("__DamageConflict__", tag.removeAndGet("Damage")!!)
                }
                tag.putInt("Damage", value.meta)
                org.chorus_oss.nbt.tags.CompoundTag.from(tag)
            }
            value.hasCompoundTag() -> {
                val tag = value.namedTag!!
                org.chorus_oss.nbt.tags.CompoundTag.from(tag)
            }
            else -> org.chorus_oss.nbt.tags.CompoundTag()
        },
        canBePlacedOn = HandleByteBuf.extractStringList(value, "CanPlaceOn"),
        canBreak = HandleByteBuf.extractStringList(value, "CanDestroy"),
    )
}