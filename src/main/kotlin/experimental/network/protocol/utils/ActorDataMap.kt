package org.chorus_oss.chorus.experimental.network.protocol.utils

import org.chorus_oss.chorus.entity.data.EntityDataMap
import org.chorus_oss.protocol.types.actor_data.ActorDataKey
import org.chorus_oss.protocol.types.actor_data.ActorDataMap

fun ActorDataMap.Companion.from(value: EntityDataMap): ActorDataMap {
    return ActorDataMap().also {
        val transformed = value.entries.map { (key, data) ->
            val actual = ((key.getTransformer() as? java.util.function.Function<Any, *>?)?.apply(data) ?: data)
            ActorDataKey.entries[key.getValue()] to actual
        }
        it.putAll(transformed)
    }
}