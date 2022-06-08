package com.sproject.winlink.common.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.net.*

class NetworkUtils {

    companion object {
        private var CONNECTION_TIMEOUT = 250

        fun inet4AddressFromInt(ip: Int): Inet4Address {
            return Inet4Address.getByAddress(
                "", byteArrayOf(
                    (ip ushr 24 and 0xFF).toByte(),
                    (ip ushr 16 and 0xFF).toByte(),
                    (ip ushr 8 and 0xFF).toByte(),
                    (ip and 0xFF).toByte()
                )
            ) as Inet4Address
        }
    }

    private fun getNetworkInterfaces(): List<InterfaceAddress> {
        return NetworkInterface
            .getNetworkInterfaces()
            .asSequence()
            .flatMap { networkInterface ->
                networkInterface
                    .interfaceAddresses
                    .asSequence()
                    .map {
                        if (!it.address.isLoopbackAddress && it.address is Inet4Address) it
                        else null
                    }.filterNotNull()
            }.toList()
    }

    suspend fun getClients(callback: suspend (ipAddress: String) -> Unit) {
        val interfaces = getNetworkInterfaces()

        //I don't think this is correct 🤔
        val network = interfaces.first()

        val networkSize = 2.shl(32 - network.networkPrefixLength.toInt() - 1)
        val networkAddress = network.address as Inet4Address

        val enumerateAddresses = generateSequence(0) {
            val next = it + 1
            if (next < networkSize) next else null
        }
            .map { networkAddress.maskWith(network.networkPrefixLength).hashCode() + it }
            .map { inet4AddressFromInt(it) }

        withContext(Dispatchers.IO) {
            enumerateAddresses
                .chunked(5)
                .map { ipAddresses ->
                    async {
                        ipAddresses.map { ipAddress ->
                            ipAddress.hostAddress?.let {
                                if (ipAddress.isReachable(CONNECTION_TIMEOUT)) callback(it)
                            }
                        }
                    }
                }
                .toList()
                .awaitAll()
                .flatten()
        }
    }
}

fun Inet4Address.maskWith(maskLength: Short): Inet4Address {
    val mask = (2.shl(maskLength.toInt()) - 1).shl(32 - maskLength)
    val masked = this.hashCode().and(mask)
    return NetworkUtils.inet4AddressFromInt(masked)
}