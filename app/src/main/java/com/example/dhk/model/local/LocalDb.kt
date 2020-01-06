package com.example.dhk.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dhk.model.local.room.Dibs
import com.example.dhk.model.local.room.DibsDao

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2019-12-20 <p/>
 */

@Database(entities = [
    Dibs::class
], version = 1)
abstract class LocalDb: RoomDatabase() {
    abstract fun dibsDao(): DibsDao
}