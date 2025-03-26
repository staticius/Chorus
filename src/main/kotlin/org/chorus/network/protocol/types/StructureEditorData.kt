package org.chorus.network.protocol.types

import org.chorus.block.property.enums.StructureBlockType

data class StructureEditorData(
    var name: String,
    var filteredName: String,
    var dataField: String,
    var includingPlayers: Boolean,
    var boundingBoxVisible: Boolean,
    var type: StructureBlockType,
    var settings: StructureSettings,
    var redstoneSaveMode: StructureRedstoneSaveMode,
)
