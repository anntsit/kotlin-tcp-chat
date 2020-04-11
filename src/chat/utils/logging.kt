package chat.utils

import java.lang.Exception
import java.util.logging.Logger
import java.util.logging.Level

class Log {
    companion object {
        private val logger = Logger.getLogger("main")

        fun info(message: String) {
            logger.info(message)
        }

        fun exception(message: String, exception: Exception) {
            logger.log(Level.WARNING, message)
            exception.printStackTrace()
        }
    }
}