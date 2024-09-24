package com.example

import com.example.plugins.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.server.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun startUdpServer() {

    CoroutineScope(Dispatchers.IO).launch {
        val port = 9999  // UDP port
        val buffer = ByteArray(1024)

        try {
            val socket = DatagramSocket(port)
            println("UDP server started on port $port")

            while (true) {
                val packet = DatagramPacket(buffer, buffer.size)
                socket.receive(packet)
                val receivedMessage = String(packet.data, 0, packet.length)
                val clientAddress = packet.address
                val clientPort = packet.port

                println("Received message: $receivedMessage from $clientAddress:$clientPort")

                // Send acknowledgment back to client
                val responseMessage = "Message received: $receivedMessage"
                val responsePacket = DatagramPacket(
                    responseMessage.toByteArray(),
                    responseMessage.length,
                    clientAddress,
                    clientPort
                )
                socket.send(responsePacket)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun Application.module() {
    configureMonitoring()
    configureSecurity()
    configureRouting()
    startUdpServer()
}


