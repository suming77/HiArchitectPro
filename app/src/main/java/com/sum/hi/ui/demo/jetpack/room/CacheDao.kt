package com.sum.hi.ui.demo.jetpack.room

import androidx.lifecycle.LiveData
import androidx.room.*
import retrofit2.http.DELETE

/**
 * @author smy
 * @date   2022/7/8 17:35
 * @desc Dao date access Object 数据访问对象
 *
 * 这里定义数据操作的方法，增删改查
 *
 * 这里一般定义为interface
 */
interface CacheDao {

    //这里需要自己输入sql语句
    @Query("select * from table_cache")
    fun query(): List<Cache>

    @Query("select * from table_cache where 'cache_key'=:keyWord")
    fun query(keyWord: String): List<Cache>

    //查询一条
    @Query("select * from table_cache where 'cache_key'=:keyWord limit 1")
    fun queryOne(keyWord: String): Cache

    //可以通过LiveData以观察者的形式获取数据库数据，可以避免不必要的NPE
    //更重要的是可以监听数据库表中的数据的变化
    //一旦发生了insert，update，delete，room会自动读取表中最新的数据，发送给UI层，刷新页面
    //也可以和RXjava的Observer
    @Query("select * from table_cache")
    fun queryForLiveData(): LiveData<List<Cache>>

    @Delete(entity = Cache::class)
    fun delete(keyWord: String)//删除指定keyWord的数据

    @Delete(entity = Cache::class)
    fun delete(cache: Cache)//删除指定对象

    //entity操作的表，OnConflictStrategy冲突策略，
    //OnConflictStrategy.ABORT终止本次操作
    //OnConflictStrategy.IGNORE忽略本次操作，也终止
    @Insert(entity = Cache::class, onConflict = OnConflictStrategy.REPLACE)//覆盖老数据
    fun insert(cache: Cache)//这条cache对象新的数据，cache_key已经存在了这个表当中，此时就会发生冲突

    //不指定的entity也可以，会根据你传入的参数对象来找到你要操作的那张表
    @Update
    fun update(cache: Cache)

}