package org.chorus.utils

class InvalidIdentifierException : RuntimeException {
    constructor(message: String?) : super(message)

    constructor(message: String?, throwable: Throwable?) : super(message, throwable)
}

