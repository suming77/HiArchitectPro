package com.sum.hi.ui.demo.jetpack.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/08 23:27
 * @类描述 ${TODO}抽象类
 */
//有哪些表，version必须指定版本,exportSchema生成一个json文件，方便排查问题，还需要在build.gradle文件中配置
@Database(entities = [Cache::class, User::class], version = 1, exportSchema = true)
abstract class CacheDatabase : RoomDatabase() {
    //抽象方法或者抽象类标记
    abstract fun cacheDao(): CacheDao

    //单例
    companion object {
        private var database: CacheDatabase? = null

        @Synchronized//同步锁，可能在多个线程中同时调用
        fun get(context: Context): CacheDatabase {
            if (database == null) {
                //内存数据库，这种存储的数据库，只会存在内存当中，进程被杀死后，数据会随之丢失，比如多个页面之间的数据共享
                //database = Room.inMemoryDatabaseBuilder(context, CacheDatabase::class.java).build()
                database =
                    Room.databaseBuilder(context, CacheDatabase::class.java, "HiArchitectPro")
                        //允许主线程操作数据库，默认数不允许的，如果直接在主线程操作数据库会报错
//                        .allowMainThreadQueries()
//                        .addCallback(callBack)
//                        .setQueryExecutor{}//指定数据查询的线程池，不指定会有个默认的
                        //数据库升级 1-->2， 怎么升级，以什么规则升级
                        //不是在头部version将1改成2才去指定这个migration1_2，
                        // 而是只要表，任何数据库有变更版本都需要升级，升级的同时需要指定migration，如果不指定则会报错
//                        .addMigrations(migration1_2)
                        //它是用来创建supportsqliteopenhelper,否则默认是frameworksqliteopenhelperFractory,用来链接room和SQLite
                        //可以利用自行创建supportsqliteopenhelper,来实现数据库加密
//                        .openHelperFactory()
                        .build()
            }
            return database!!
        }

        val callBack = object : RoomDatabase.Callback() {
            //创建回调
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }

            //打开数据库回调
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }

        //版本1到版本2
        val migration1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //比如说为table_cache表增加字段
                database.execSQL("alter table table_cache add column cache_time LONG")
            }

        }
    }

}