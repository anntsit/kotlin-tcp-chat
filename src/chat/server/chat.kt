package chat.server

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Chat {
    private val clients: MutableList<Client> = ArrayList()

    private val dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Synchronized
    fun onMessageFromUserReceived(username: String, text: String) {
        val message = "$username: $text"
        sendToAll(formatMessage(message))
    }

    @Synchronized
    fun onUserConnected(username: String) {
        sendToAll(formatMessage("$username connected"))
    }

    @Synchronized
    fun onUserDisconnected(username: String) {
        sendToAll(formatMessage("$username disconnected"))
    }

    fun addClient(client: Client) {
        clients.add(client)
    }

    private fun sendToAll(text: String) {
        clients.forEach { it.send(text) }
    }

    private fun formatMessage(message: String): String {
        val now = LocalDateTime.now().format(dtFormatter)
        return "[$now] $message"
    }
}