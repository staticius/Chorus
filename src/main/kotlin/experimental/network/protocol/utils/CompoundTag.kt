package org.chorus_oss.chorus.experimental.network.protocol.utils

import org.chorus_oss.nbt.Tag
import org.chorus_oss.nbt.tags.CompoundTag

fun CompoundTag.Companion.from(tag: org.chorus_oss.chorus.nbt.tag.CompoundTag): CompoundTag {
    return Tag.from(tag) as CompoundTag
}