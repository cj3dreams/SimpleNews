package com.twenty2byte.simplenews.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class RoomAppDb: RoomDatabase() {

    abstract fun newsDao(): NewsDao?

}