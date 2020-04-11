package chat.server

import chat.utils.Log
import java.io.*
import java.lang.Exception


class NullMessageException: Exception()


class Client(input: InputStream, output: OutputStream, private val chat: Chat): Thread() {
    private val buffInput = BufferedReader(InputStreamReader(input))
    private val buffOutput = BufferedWriter(OutputStreamWriter(output))
    private var username: String = "";

    private fun readMessageFromInput(): String {
        while(true) {
            val message = buffInput.readLine()

            if (message == null) {
                throw NullMessageException()
            }

            if (!message.isNullOrEmpty()) {
                return message
            }
        }
    }

    private fun initClient() {
        send("Enter your username:")
        username = readMessageFromInput()
        chat.onUserConnected(username)
        chat.addClient(this)
        Log.info("User `$username` is initited")
    }

    private fun formatText(text: String): String {
        if (text.last() != '\n') {
            return text + '\n'
        }

        return text
    }

    fun send(text: String) {
        if (text.isNotEmpty()) {
            buffOutput.write(formatText(text))
            buffOutput.flush()
        }
    }

    override fun run() {
        try {
            initClient()

            while (true) {
                val message = readMessageFromInput()
                chat.onMessageFromUserReceived(username, message)
            }
        } catch (e: Exception) {
            Log.exception("ClientError", e)
            chat.removeClient(this)
            chat.onUserDisconnected(username)
        } finally {
            buffInput.close()
            buffOutput.close()
        }
    }
}