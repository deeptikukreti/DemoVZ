package com.example.demovz.di

import com.example.demovz.db.dao.AreaDeviceDao
import com.example.demovz.db.dao.EventDao
import com.example.demovz.repo.DeviceRepository
import com.example.demovz.repo.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    fun provideEventRepository(eventDao: EventDao) = EventRepository(eventDao)

    @Provides
    fun provideDeviceRepository(areaDeviceDao: AreaDeviceDao) = DeviceRepository(areaDeviceDao)
}