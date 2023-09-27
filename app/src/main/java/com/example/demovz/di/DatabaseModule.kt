package com.example.demovz.di


import android.content.Context
import com.example.demovz.db.devices.AreaDeviceDao
import com.example.demovz.db.devices.DevicesRoomDb
import com.example.demovz.db.events.EventDao
import com.example.demovz.db.events.RoomDb
import com.example.demovz.repo.DeviceRepository
import com.example.demovz.repo.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideEventDao(@ApplicationContext appContext: Context) : EventDao {
        return RoomDb.getInstance(appContext).eventDao()
    }

    @Provides
    fun provideEventRepository(eventDao: EventDao) = EventRepository(eventDao)

    @Provides
    fun provideDeviceDao(@ApplicationContext appContext: Context) : AreaDeviceDao {
        return DevicesRoomDb.getInstance(appContext).areaDeviceDao()
    }

    @Provides
    fun provideDeviceRepository(areaDeviceDao: AreaDeviceDao) = DeviceRepository(areaDeviceDao)
}