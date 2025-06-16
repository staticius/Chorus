package org.chorus_oss.chorus.experimental.block.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import org.chorus_oss.chorus.experimental.block.BlockComponent
import org.chorus_oss.chorus.utils.BlockColor

data class MapColorComponent(
    val r: Int,
    val g: Int,
    val b: Int,
    val a: Int,
) : BlockComponent, Component<MapColorComponent> {
    override fun type(): ComponentType<MapColorComponent> = MapColorComponent

    companion object : ComponentType<MapColorComponent>()
}
