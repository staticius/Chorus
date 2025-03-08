package cn.nukkit.utils

/**
 * @author MagicDroidX (Nukkit Project)
 */
class LevelException : ServerException {
    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
