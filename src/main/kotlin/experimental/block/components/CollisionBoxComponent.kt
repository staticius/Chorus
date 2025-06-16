package org.chorus_oss.chorus.experimental.block.components

import com.github.quillraven.fleks.ComponentType
import org.chorus_oss.chorus.experimental.block.BlockComponent
import org.chorus_oss.protocol.types.IVector3

data class CollisionBoxComponent(
    val origin: IVector3,
    val size: IVector3,
    val enabled: Boolean,
): BlockComponent<CollisionBoxComponent> {
    constructor(enabled: Boolean): this(IVector3(0, 0, 0), IVector3(16, 16, 16), enabled)

    constructor(origin: IVector3, size: IVector3): this(origin, size, true)

    override fun type(): ComponentType<CollisionBoxComponent> = CollisionBoxComponent

    companion object : ComponentType<CollisionBoxComponent>()
}
