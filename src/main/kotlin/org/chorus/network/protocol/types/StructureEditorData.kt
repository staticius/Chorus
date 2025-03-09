package org.chorus.network.protocol.types

import org.chorus.block.property.enums.StructureBlockType
import lombok.Value


class StructureEditorData {
    var name: String? = null
    var filteredName: String? = null
    var dataField: String? = null
    var includingPlayers: Boolean = false
    var boundingBoxVisible: Boolean = false
    var type: StructureBlockType? = null
    var settings: StructureSettings? = null
    var redstoneSaveMode: StructureRedstoneSaveMode? = null
}
