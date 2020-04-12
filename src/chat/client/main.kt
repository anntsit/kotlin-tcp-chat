package chat.client

import chat.utils.Log
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.Socket

const val SERVER_ADDRESS = "0.0.0.0"
const val SERVER_PORT = 8080


fun writeMessage(writer: BufferedWriter, message: String) {
    writer.write(message + '\n')
    writer.flush()
}


fun readMessageFromReader(reader: BufferedReader): String {
    while (true) {
        val message = reader.readLine()
        if (!message.isNullOrEmpty()) {
            return message
        }
    }
}


class MessageReader(private val serverReader: BufferedReader): Thread() {
    override fun run() {
        while (true) {
            val message = readMessageFromReader(serverReader)
            println(message)
        }
    }
}


fun main() {
    var socket: Socket? = null
    var serverReader: BufferedReader? = null;
    var serverWriter: BufferedWriter? = null;
    var consoleReader: BufferedReader? = null;

    try {
        socket = Socket(SERVER_ADDRESS, SERVER_PORT)

        serverReader = BufferedReader(InputStreamReader(socket.getInputStream()))
        serverWriter = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
        consoleReader = BufferedReader(InputStreamReader(System.`in`))

        val messageReaderThread = MessageReader(serverReader)
        messageReaderThread.setDaemon(true)
        messageReaderThread.start()

        val userName = readMessageFromReader(consoleReader)
        writeMessage(serverWriter, userName)

        while (true) {
            val message = readMessageFromReader(consoleReader)
            writeMessage(serverWriter, message)
        }
    } catch (e: Exception) {
        Log.exception("Got exception: ", e)
    } finally {
        socket?.close()
        serverReader?.close()
        serverWriter?.close()
        consoleReader?.close()
    }
}