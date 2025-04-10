package org.chorus.block

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.chorus.block.BlockEntityHolder

internal class Loggers private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        val logBlocKEntityHolder: Logger = LogManager.getLogger(
            BlockEntityHolder::class.java
        )
    }
}
