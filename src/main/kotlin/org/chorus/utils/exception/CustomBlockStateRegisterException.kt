package org.chorus.utils.exception

import cn.nukkit.utils.ServerException

class CustomBlockStateRegisterException : ServerException {
    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
