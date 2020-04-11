package chat.server

import java.io.*


class Client(input: InputStream, output: OutputStream, private val chat: Chat): Thread() {
    private val buffInput = BufferedReader(InputStreamReader(input))
    private val buffOutput = BufferedWriter(OutputStreamWriter(output))
    private var username: String = "";

    private fun readMessageFromInput(): String {
        while(true) {
            val message = buffInput.readLine()
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
        initClient()

        while (true) {
            val message = readMessageFromInput()
            chat.onMessageFromUserReceived(username, message)
        }
    }
}