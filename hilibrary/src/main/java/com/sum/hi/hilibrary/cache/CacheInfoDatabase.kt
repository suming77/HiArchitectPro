package com.sum.hi.hilibrary.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sum.hi.hilibrary.util.AppGlobals

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/09 18:06
 * @类描述 ${TODO}
 */
@Database(entities = [CacheInfo::class], version = 1)
abstract class CacheInfoDatabase : RoomDatabase() {
    //获取操作数据库数据的dao对象
    abstract var cacheInfoDao: CacheInfoDao

    companion object {
        private var database: CacheInfoDatabase? = null
        fun get(): CacheInfoDatabase {
            if(database == null){
                val context = AppGlobals.get()!!.applicationContext
                database =
                    Room.databaseBuilder(context, CacheInfoDatabase::class.java, "CacheInfo").build()
            }
            return database!!
        }
//
//        init {
//            val context = AppGlobals.get()!!.applicationContext
//            database =
//                Room.databaseBuilder(context, CacheInfoDatabase::class.java, "CacheInfo").build()
//        }
    }
}