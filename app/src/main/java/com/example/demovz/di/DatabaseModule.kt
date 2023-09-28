package com.example.demovz.di


import android.content.Context
import androidx.room.Room
import com.example.demovz.app.AppDatabase
import com.example.demovz.db.dao.AreaDeviceDao
import com.example.demovz.db.dao.EventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideEventDao(appDatabase: AppDatabase) : EventDao {
        return appDatabase.eventDao()
    }

    @Provides
    fun provideDeviceDao(appDatabase: AppDatabase) : AreaDeviceDao {
        return appDatabase.deviceDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "DEMO_VZ_DB"
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    }


}