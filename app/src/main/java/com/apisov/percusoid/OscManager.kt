package com.apisov.percusoid

import com.apisov.percusoid.util.getBroadcastIpAddress
import com.apisov.percusoid.util.getNetworkAddress
import com.illposed.osc.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.uiThread
import java.io.IOException
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress

// Hardcoded port that must be equal to that we use in Osc Server on a computer
const val BROADCAST_PORT = 12000
const val CLIENT_PORT = 12100

class OscManager {
    private lateinit var oscSender: OSCPortOut
    private val oscServerSocket = DatagramSocket(null)
    private val oscServer: OSCPortIn

    init {
        oscServerSocket.reuseAddress = true
        oscServerSocket.bind(InetSocketAddress(getNetworkAddress(), BROADCAST_PORT))
        oscServer = OSCPortIn(oscServerSocket)
    }

    var connected: Boolean = false

    private val addressSelector = AddressSelector { true }  // TODO: Remove passing all messages

    private val broadcastOscAnswerListener = OSCListener { _, message ->
        oscSender.close()

        // get info about OSC server on the computer
        val address = message.arguments[0] as String
        val port = message.arguments[1] as Int

        createOscSender(port, address)

        // Stop the server for broadcast connect message
        oscServer.stopListening()
        oscServer.close()
        oscServerSocket.disconnect()
    }


    /**
     * Creates a server for broadcast 'connect' message and send it immediately
     */
    fun connect() {
        createBroadcastSender()

        startBroadcastAnswerServer()

        send("connect", listOf(getNetworkAddress()?.hostAddress, BROADCAST_PORT))
    }

    /**
     * Send a disconnect message to remove a client from the list of connections on the server
     */
    fun disconnect(sendCompleteCallback: OnSendCompleteCallback) {
        send(
            "disconnect",
            listOf(getNetworkAddress()?.hostAddress, BROADCAST_PORT),
            sendCompleteCallback
        )
    }

    /**
     * Finish session and close all connections
     */
    fun stop() {
        oscSender.close()
    }

    /**
     * Tmp function while we don't have time of a note
     */
    fun hitPercussion(
        channel: Int,
        note: Int,
        velocity: Int
    ) {
        send("noteOn", listOf(channel, note, velocity))
        send("noteOff", listOf(channel, note, velocity))
    }

    fun sendControls(
        channel: Int,
        control: Int,
        velocity: Int
    ) {
        send("controllerChange", listOf(channel, control, velocity))
    }

    private fun startBroadcastAnswerServer() {
        oscServer.startListening()
        oscServer.addListener(addressSelector, broadcastOscAnswerListener)
    }

    private fun createBroadcastSender() {
        createOscSender(CLIENT_PORT, getBroadcastIpAddress()!!)
    }

    private fun createOscSender(port: Int, address: String) {
        val socket = DatagramSocket(port)

        doAsync {
            socket.connect(InetAddress.getByName(address), port)

            if (address != getBroadcastIpAddress()) {
                connected = true
            }
        }

        oscSender = OSCPortOut(
            InetAddress.getByName(address),
            port,
            socket
        )
    }

    private fun send(
        address: String,
        argument: List<*>?,
        onCompleteListener: OnSendCompleteCallback? = null
    ) {
        val message = OSCMessage("/$address")

        argument?.apply {
            this.forEach { message.addArgument(it) }
        }

        doAsync {
            try {
                oscSender.send(message)
            } catch (ignored: IOException) {
                ignored.printStackTrace()
            }

            uiThread {
                onComplete {
                    onCompleteListener?.apply { onComplete() }
                }
            }
        }
    }

    interface OnSendCompleteCallback {
        fun onComplete()
    }
}