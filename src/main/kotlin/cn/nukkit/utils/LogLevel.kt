package cn.nukkit.utils

import org.apache.logging.log4j.util.TriConsumer
import java.util.function.BiConsumer

/**
 * @author MagicDroidX (Nukkit Project)
 */
enum class LogLevel(
    private val logTo: BiConsumer<MainLogger, String?>,
    private val logThrowableTo: TriConsumer<MainLogger, String?, Throwable?>
) {
    NONE(
        BiConsumer { logger: MainLogger?, message: String? -> },
        TriConsumer { mainLogger: MainLogger?, s: String?, throwable: Throwable? -> }),
    EMERGENCY(
        BiConsumer { obj: MainLogger, message: String? -> obj.emergency(message) },
        TriConsumer { obj: MainLogger, message: String?, t: Throwable? -> obj.emergency(message, t) }),
    ALERT(
        BiConsumer { obj: MainLogger, message: String? -> obj.alert(message) },
        TriConsumer { obj: MainLogger, message: String?, t: Throwable? -> obj.alert(message, t) }),
    CRITICAL(
        BiConsumer { obj: MainLogger, message: String? -> obj.critical(message) },
        TriConsumer { obj: MainLogger, message: String?, t: Throwable? -> obj.critical(message, t) }),
    ERROR(
        BiConsumer { obj: MainLogger, message: String? -> obj.error(message) },
        TriConsumer { obj: MainLogger, message: String?, t: Throwable? -> obj.error(message, t) }),
    WARNING(
        BiConsumer { obj: MainLogger, message: String? -> obj.warning(message) },
        TriConsumer { obj: MainLogger, message: String?, t: Throwable? -> obj.warning(message, t) }),
    NOTICE(
        BiConsumer { obj: MainLogger, message: String? -> obj.notice(message) },
        TriConsumer { obj: MainLogger, message: String?, t: Throwable? -> obj.notice(message, t) }),
    INFO(
        BiConsumer { obj: MainLogger, message: String? -> obj.info(message) },
        TriConsumer { obj: MainLogger, message: String?, t: Throwable? -> obj.info(message, t) }),
    DEBUG(
        BiConsumer { obj: MainLogger, message: String? -> obj.debug(message) },
        TriConsumer { obj: MainLogger, message: String?, t: Throwable? -> obj.debug(message, t) });

    fun log(logger: MainLogger, message: String?) {
        logTo.accept(logger, message)
    }

    fun log(logger: MainLogger, message: String?, throwable: Throwable?) {
        logThrowableTo.accept(logger, message, throwable)
    }

    val level: Int
        get() = ordinal

    companion object {
        val DEFAULT_LEVEL: LogLevel = INFO
    }
}
