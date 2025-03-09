package org.chorus.entity.data

import cn.nukkit.math.BlockVector3
import cn.nukkit.math.Vector3
import cn.nukkit.math.Vector3f
import cn.nukkit.nbt.tag.CompoundTag
import lombok.RequiredArgsConstructor

@RequiredArgsConstructor
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
            if (clazz == Byte::class.java) {
                return BYTE
            } else if (clazz == Short::class.java) {
                return SHORT
            } else if (clazz == Int::class.java) {
                return INT
            } else if (clazz == Float::class.java) {
                return FLOAT
            } else if (clazz == String::class.java) {
                return STRING
            } else if (clazz == CompoundTag::class.java) {
                return NBT
            } else if (clazz == BlockVector3::class.java) {
                return VECTOR3I
            } else if (clazz == Long::class.java) {
                return LONG
            } else if (clazz == Vector3::class.java || clazz == Vector3f::class.java) {
                return VECTOR3F
            }
            throw IllegalArgumentException("Unknown EntityDataType: " + clazz)
        }
    }
}
