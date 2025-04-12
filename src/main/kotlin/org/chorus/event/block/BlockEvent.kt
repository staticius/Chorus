package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.event.Event


abstract class BlockEvent(open val block: Block) : Event()
