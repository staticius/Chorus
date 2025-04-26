package org.chorus.entity.data

import org.chorus.math.BlockVector3
import org.chorus.math.Vector3
import org.chorus.math.Vector3f
import org.chorus.nbt.tag.CompoundTag

enum class EntityDataFormat {
    BYTE,
    SHORT,
    INT,
    FLOAT,
    STRING,
    NBT,
    VECTOR3I,
    LONG,
    VECTOR3F;

    companion object {
        fun from(value: Any): EntityDataFormat {
            return when (value) {
                is Byte -> BYTE
                is Short -> SHORT
                is Int -> INT
                is Float -> FLOAT
                is Long -> LONG
                is String -> STRING
                is CompoundTag -> NBT
                is BlockVector3 -> VECTOR3I
                is Vector3,
                is Vector3f -> VECTOR3F

                else -> throw IllegalArgumentException("Unknown EntityDataType: $value")
            }
        }
    }
}
