package org.chorus.utils.exception

import org.chorus.utils.ServerException

class CustomBlockStateRegisterException : ServerException {
    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
