package cn.nukkit.level.generator.terra.mappings

import com.google.common.collect.BiMap
import kotlin.collections.set

/**
 * Allay Project 2023/10/28
 *
 * @author daoge_cmd
 */
interface BiMappedRegistry<LEFT, RIGHT> :
    Registry<BiMap<LEFT, RIGHT>?> {
    fun getByLeft(left: LEFT): RIGHT? {
        return content.get(left)
    }

    fun getByRight(right: RIGHT): LEFT? {
        return content.inverse()[right]
    }

    fun getByLeftOrDefault(left: LEFT, defaultValue: RIGHT): RIGHT {
        return content.getOrDefault(left, defaultValue)
    }

    fun getByRightOrDefault(right: RIGHT, defaultValue: LEFT): LEFT {
        return content.inverse().getOrDefault(right, defaultValue)
    }

    fun register(left: LEFT, right: RIGHT) {
        content[left] = right
    }
}
