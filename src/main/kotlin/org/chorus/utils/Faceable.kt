package org.chorus.utils

import cn.nukkit.math.BlockFace

interface Faceable {
    var blockFace: BlockFace?
        set(face) {
            // Does nothing by default
        }
}
