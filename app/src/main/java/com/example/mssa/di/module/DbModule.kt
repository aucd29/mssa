package com.example.mssa.di.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 12. 6. <p/>
 */

@Module
class DbModule {
    companion object {
        const val DB_NAME = "local.db"
    }

//    @Singleton
//    @Provides
//    fun provideDb(app: Application): LocalDb =
//        Room.databaseBuilder(app, LocalDb::class.java, DB_NAME)
//            .fallbackToDestructiveMigration()
//            .build()
}