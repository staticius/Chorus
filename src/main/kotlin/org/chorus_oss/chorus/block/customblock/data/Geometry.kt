package org.chorus_oss.chorus.block.customblock.data

import com.google.common.base.Preconditions
import org.chorus_oss.chorus.nbt.tag.CompoundTag

class Geometry(name: String) : NBTData {
    private val geometryName: String
    private var culling = ""
    private val boneVisibilities: MutableMap<String, String> = LinkedHashMap()

    init {
        Preconditions.checkNotNull(name)
        Preconditions.checkArgument(name.isNotBlank())
        this.geometryName = name
    }

    /**
     * 控制模型对应骨骼是否显示
     *
     *
     * Control the visibility that the bone of geometry
     */
    fun boneVisibility(boneName: String, isVisibility: Boolean): Geometry {
        Preconditions.checkNotNull(boneName)
        Preconditions.checkArgument(boneName.isNotBlank())
        boneVisibilities[boneName] = if (isVisibility) "true" else "false"
        return this
    }

    /**
     * 控制模型对应骨骼是否显示
     *
     *
     * Control the visibility that the bone of geometry
     */
    fun boneVisibility(boneName: String, condition: String): Geometry {
        Preconditions.checkNotNull(boneName)
        Preconditions.checkArgument(boneName.isNotBlank())
        boneVisibilities[boneName] = condition
        return this
    }

    fun culling(cullingName: String): Geometry {
        Preconditions.checkNotNull(cullingName)
        this.culling = cullingName
        return this
    }

    override fun toCompoundTag(): CompoundTag {
        val boneVisibility = CompoundTag()
        for ((key, value) in boneVisibilities) {
            boneVisibility.putString(key, value)
        }
        val compoundTag = CompoundTag()
            .putString("identifier", geometryName)
            .putByte("legacyBlockLightAbsorption", 0)
            .putByte("legacyTopRotation", 0)
        if (boneVisibilities.isNotEmpty()) {
            compoundTag.putCompound("bone_visibility", boneVisibility)
        }
        return compoundTag
    }
}
