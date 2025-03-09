package org.chorus.level.generator.terra.delegate

import com.dfsek.terra.api.entity.EntityType

@JvmRecord
data class PNXEntityType(val identifier: String) : EntityType {
    override fun getHandle(): String {
        return identifier
    }
}
