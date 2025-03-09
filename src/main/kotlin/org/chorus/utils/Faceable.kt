package org.chorus.utils

import org.chorus.math.BlockFace

interface Faceable {
    var blockFace: BlockFace?
        set(face) {
            // Does nothing by default
        }
}
