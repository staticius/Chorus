package org.chorus.registry

import cn.nukkit.utils.exception.FormativeException

class RegisterException : FormativeException {
    constructor(msg: String?) : super(msg)

    constructor(format: String?, vararg arguments: Any?) : super(format, *arguments)

    constructor(e: Exception?) : super(e)
}
