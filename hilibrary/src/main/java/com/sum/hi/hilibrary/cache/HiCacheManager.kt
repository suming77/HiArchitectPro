package com.sum.hi.hilibrary.cache

import java.io.*
import java.lang.Exception

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/09 18:35
 * @类描述 ${TODO}
 */
object HiCacheManager {
    //body可以是list,object,String等类型
    fun <T> saveCacheInfo(key: String, body: T) {
        val cacheInfo = CacheInfo()
        cacheInfo.key = key
        cacheInfo.data = toByteArray(body)
        CacheInfoDatabase.get().cacheInfoDao.saveCacheInfo(cacheInfo)
    }

    fun <T> getCacheBody(key: String): T? {
        val cacheInfo = CacheInfoDatabase.get().cacheInfoDao.getCacheInfo(key)
        return (if (cacheInfo?.data != null) {
            toObject(cacheInfo.data!!)
        } else null) as? T
    }


    fun deleteCacheInfo(key: String) {
        val cacheInfo = CacheInfo()
        cacheInfo.key = key
        CacheInfoDatabase.get().cacheInfoDao.deleteCacheInfo(cacheInfo)
    }

    private fun <T> toByteArray(body: T): ByteArray? {
        var bos: ByteArrayOutputStream? = null
        var oos: ObjectOutputStream? = null
        try {
            bos = ByteArrayOutputStream()
            oos = ObjectOutputStream(bos)

            oos.writeObject(body)
            oos.flush()
            return bos.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            bos?.close()
            oos?.close()
        }
        return ByteArray(0)
    }

    //反序列化，把二级制数组转化为object对象
    fun toObject(byteArray: ByteArray): Any? {
        var bis: ByteArrayInputStream? = null
        var ois: ObjectInputStream? = null
        try {
            bis = ByteArrayInputStream(byteArray)
            ois = ObjectInputStream(bis)
            return ois.readObject()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            bis?.close()
            ois?.close()
        }
        return null
    }
}