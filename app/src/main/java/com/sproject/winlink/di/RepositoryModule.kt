package com.sproject.winlink.di

import com.sproject.winlink.data.local.DataStoreService
import com.sproject.winlink.data.remote.PcApi
import com.sproject.winlink.data.repository.PcRepositoryImpl
import com.sproject.winlink.domain.repository.PcRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePcRepository(pcApi: PcApi, dataStoreService: DataStoreService): PcRepository {
        return PcRepositoryImpl(pcApi, dataStoreService)
    }
}
