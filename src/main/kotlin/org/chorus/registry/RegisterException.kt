package org.chorus.registry

import org.chorus.utils.exception.FormativeException

class RegisterException : FormativeException {
    constructor(msg: String?) : super(msg)

    constructor(format: String?, vararg arguments: Any?) : super(format, *arguments)

    constructor(e: Exception?) : super(e)
}
