package com.apisov.percusoid.util

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface

fun getNetworkAddress(): InetAddress? {
    try {
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val networkInterface = en.nextElement()
            val addresses = networkInterface.inetAddresses

            while (addresses.hasMoreElements()) {
                val address = addresses.nextElement()
                if (!address.isLoopbackAddress && !address.isAnyLocalAddress && !address.isLinkLocalAddress && address is Inet4Address) {
                    return address
                }
            }
        }
    } catch (ex: Exception) {
        System.out.println(ex.stackTrace)
    }

    return null
}

fun getBroadcastIpAddress(): String? {
    try {
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val networkInterface = en.nextElement()
            val addresses = networkInterface.inetAddresses

            while (addresses.hasMoreElements()) {
                val address = addresses.nextElement()

                if (!address.isLoopbackAddress && address is Inet4Address) {
                    val host = networkInterface.interfaceAddresses.find { it.broadcast != null }
                        ?.broadcast?.hostAddress

                    if (host != null && host != "") {
                        return host
                    }
                }
            }

        }
    } catch (ex: Exception) {
        System.out.println(ex.stackTrace)
    }

    return null
}