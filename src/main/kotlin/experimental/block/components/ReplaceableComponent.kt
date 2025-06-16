package org.chorus_oss.chorus.experimental.block.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data object ReplaceableComponent : Component<ReplaceableComponent>, ComponentType<ReplaceableComponent>() {
    override fun type(): ComponentType<ReplaceableComponent> = ReplaceableComponent
}
