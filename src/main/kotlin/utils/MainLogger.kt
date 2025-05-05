package org.chorus_oss.chorus.utils

class MainLogger : ThreadedLogger() {
    override fun emergency(message: String?) {
        MainLogger.log.error(message)
    }

    override fun alert(message: String?) {
        MainLogger.log.warn(message)
    }

    override fun critical(message: String?) {
        MainLogger.log.error(message)
    }

    override fun error(message: String?) {
        MainLogger.log.error(message)
    }

    override fun warning(message: String?) {
        MainLogger.log.warn(message)
    }

    override fun notice(message: String?) {
        MainLogger.log.warn(message)
    }

    override fun info(message: String?) {
        MainLogger.log.info(message)
    }

    override fun debug(message: String?) {
        MainLogger.log.debug(message)
    }

    fun setLogDebug(logDebug: Boolean?) {
        throw UnsupportedOperationException()
    }

    fun logException(t: Throwable?) {
        MainLogger.log.error("", t)
    }

    override fun log(level: LogLevel, message: String?) {
        level.log(this, message)
    }

    fun shutdown() {
        throw UnsupportedOperationException()
    }

    override fun emergency(message: String?, t: Throwable?) {
        MainLogger.log.error(message, t)
    }

    override fun alert(message: String?, t: Throwable?) {
        MainLogger.log.warn(message, t)
    }

    override fun critical(message: String?, t: Throwable?) {
        MainLogger.log.error(message, t)
    }

    override fun error(message: String?, t: Throwable?) {
        MainLogger.log.error(message, t)
    }

    override fun warning(message: String?, t: Throwable?) {
        MainLogger.log.warn(message, t)
    }

    override fun notice(message: String?, t: Throwable?) {
        MainLogger.log.warn(message, t)
    }

    override fun info(message: String?, t: Throwable?) {
        MainLogger.log.info(message, t)
    }

    override fun debug(message: String?, t: Throwable?) {
        MainLogger.log.debug(message, t)
    }

    override fun log(level: LogLevel, message: String?, t: Throwable?) {
        level.log(this, message, t)
    }

    companion object : Loggable {
        @JvmField
        val logger: MainLogger = MainLogger()
    }
}
