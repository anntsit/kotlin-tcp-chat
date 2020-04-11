package chat.server

import java.net.ServerSocket

import kotlin.system.exitProcess

import chat.utils.Log
import java.net.Socket

const val SERVER_PORT = 8080

fun main() {
    var serverSocket: ServerSocket? = null;

    try {
        serverSocket = ServerSocket(SERVER_PORT)
        Log.info("Server started on 0.0.0.0:$SERVER_PORT")

        val chat = Chat()
        while (true) {
            val clientSocket: Socket;

            try {
                clientSocket = serverSocket.accept()
            } catch (e: Exception) {
                Log.exception("Error during client connecting", e)
                continue
            }

            Log.info("New client connected")
            val client = Client(
                clientSocket.getInputStream(),
                clientSocket.getOutputStream(),
                chat
            )

            client.start()
        }

    } catch (e: Exception) {
        Log.exception("Server crashed", e)
        exitProcess(1)
    } finally {
        serverSocket?.close()
    }
}