package com.sum.hi.hilibrary.cache

import androidx.room.*

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/09 18:26
 * @类描述 ${TODO}
 */
@Dao
interface CacheInfoDao {

    @Insert(entity = CacheInfo::class, onConflict = OnConflictStrategy.REPLACE)
    fun saveCacheInfo(cacheInfo: CacheInfo): Long

    @Query("select * from CacheInfo where 'key'=:key")
    fun getCacheInfo(key: String): CacheInfo?

    @Delete(entity = CacheInfo::class)
    fun deleteCacheInfo(cacheInfo: CacheInfo)
}