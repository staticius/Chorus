package org.chorus.utils

import org.chorus.nbt.tag.IntTag
import org.chorus.nbt.tag.ListTag
import com.google.common.base.Preconditions

@JvmRecord
data class SemVersion(major: Int, minor: Int, patch: Int, revision: Int, build: Int) {
    fun toTag(): ListTag<IntTag> {
        val tag = ListTag<IntTag>()
        tag.add(IntTag(major))
        tag.add(IntTag(minor))
        tag.add(IntTag(patch))
        tag.add(IntTag(revision))
        tag.add(IntTag(build))
        return tag
    }

    val major: Int = major
    val minor: Int = minor
    val patch: Int = patch
    val revision: Int = revision
    val build: Int = build

    companion object {
        fun from(versions: ListTag<IntTag>): SemVersion {
            if (versions.size() == 0) {
                return SemVersion(0, 0, 0, 0, 0)
            }
            Preconditions.checkArgument(versions.size() == 5)

            return SemVersion(
                versions.get(0).getData(),
                versions.get(1).getData(),
                versions.get(2).getData(),
                versions.get(3).getData(),
                versions.get(4).getData()
            )
        }
    }
}
