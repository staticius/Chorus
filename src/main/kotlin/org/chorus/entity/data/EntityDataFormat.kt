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
                Byte::class.javaObjectType -> return BYTE
                Short::class.javaObjectType -> return SHORT
                Int::class.javaObjectType -> return INT
                Float::class.javaObjectType -> return FLOAT
                String::class.javaObjectType -> return STRING
                CompoundTag::class.javaObjectType -> return NBT
                BlockVector3::class.javaObjectType -> return VECTOR3I
                Long::class.javaObjectType -> return LONG
                Vector3::class.javaObjectType,
                Vector3f::class.javaObjectType -> return VECTOR3F

                else -> throw IllegalArgumentException("Unknown EntityDataType: $clazz")
            }
        }
    }
}
