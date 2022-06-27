package com.sproject.winlink.di

import com.sproject.winlink.common.util.NetworkUtils
import com.sproject.winlink.data.remote.PcApi
import com.sproject.winlink.data.remote.PcSocketService
import com.sproject.winlink.data.remote.PcSocketServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
            install(ContentNegotiation)
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
        }
    }

    @Provides
    @Singleton
    fun providePcSocketService(client: HttpClient): PcSocketService {
        return PcSocketServiceImpl(client)
    }

    @Provides
    @Singleton
    fun providePcApi(client: HttpClient): PcApi {
        return PcApi(client)
    }

    @Provides
    @Singleton
    fun provideNetworkUtils(): NetworkUtils {
        return NetworkUtils()
    }
}
