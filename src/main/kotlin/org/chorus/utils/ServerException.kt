package org.chorus.utils

/**
 * @author MagicDroidX (Nukkit Project)
 */
open class ServerException : RuntimeException {
    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
