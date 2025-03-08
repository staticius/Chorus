package cn.nukkit.network.protocol.types

import cn.nukkit.math.BlockVector3
import cn.nukkit.math.Vector3f
import lombok.AllArgsConstructor
import lombok.Value

@Value
@AllArgsConstructor
class StructureSettings {
    var paletteName: String? = null
    var ignoringEntities: Boolean = false
    var ignoringBlocks: Boolean = false
    var nonTickingPlayersAndTickingAreasEnabled: Boolean = false
    var size: BlockVector3? = null
    var offset: BlockVector3? = null
    var lastEditedByEntityId: Long = 0
    var rotation: StructureRotation? = null
    var mirror: StructureMirror? = null
    var animationMode: StructureAnimationMode? = null
    var animationSeconds: Float = 0f
    var integrityValue: Float = 0f
    var integritySeed: Int = 0
    var pivot: Vector3f? = null
}
