package com.example.mssa.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mssa.model.local.table.Dibs
import com.example.mssa.model.local.table.DibsDao

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

@Database(entities = [
    Dibs::class
], version = 1)
abstract class LocalDb: RoomDatabase() {
    abstract fun dibsDao(): DibsDao
}