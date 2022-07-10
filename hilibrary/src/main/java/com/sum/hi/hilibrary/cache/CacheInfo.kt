package com.sum.hi.hilibrary.cache

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/09 18:02
 * @类描述 ${TODO}
 */
@Entity(tableName = "CacheInfo")
class CacheInfo : Serializable {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    var key: String = ""

    //缓存数据的二进制
    var data: ByteArray? = null
}