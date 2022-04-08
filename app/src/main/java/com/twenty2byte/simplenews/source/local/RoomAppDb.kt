package com.twenty2byte.simplenews.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class RoomAppDb: RoomDatabase() {

    abstract fun getNewsDao(): NewsDao?

    companion object{
        var INSTANCE: RoomAppDb? = null
    }

    fun getAppDb(context: Context): RoomAppDb?{
        if (INSTANCE == null) INSTANCE = Room.databaseBuilder(context.applicationContext,
                RoomAppDb::class.java, "NewsDb").allowMainThreadQueries().build()

        return INSTANCE
    }
}