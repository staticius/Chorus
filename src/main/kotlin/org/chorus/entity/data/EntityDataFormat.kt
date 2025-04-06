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
        fun from(clazz: Class<*>): EntityDataFormat {
            when (clazz) {
                Byte::class.java -> return BYTE
                Short::class.java -> return SHORT
                Int::class.java -> return INT
                Float::class.java -> return FLOAT
                String::class.java -> return STRING
                CompoundTag::class.java -> return NBT
                BlockVector3::class.java -> return VECTOR3I
                Long::class.java -> return LONG
                Vector3::class.java,
                Vector3f::class.java -> return VECTOR3F
                else -> throw IllegalArgumentException("Unknown EntityDataType: $clazz")
            }
        }
    }
}
