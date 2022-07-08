package com.sum.hi.ui.demo.jetpack.room

import android.graphics.Bitmap
import androidx.annotation.NonNull
import androidx.room.*

/**
 * @author smy
 * @date   2022/7/8 16:56
 * @desc
 */
//定义表，需要添加Entity注解，指定表名，如果不指定表名则默认为类名Cache
@Entity(tableName = "table_cache")
class Cache {
    //room数据库，它对一张表是有要求的，必须至少要有一个主键，不能为空，autoGenerate表示cache_key的值是否由数据库自动生成
    //这里因为是String类型，所以需要自己生成
    @PrimaryKey(autoGenerate = false)
    @NonNull
    var cache_key: String = ""

    //如果是数值类型则可以指定由数据库自动生成
//    @PrimaryKey(autoGenerate = true)
    //如果想改变cache_id这个字段在数据库中的名称，可以使用ColumnInfo来改变,defaultValue指定默认值
    @ColumnInfo(name = "cacheId", defaultValue = "1")
    var cache_id: Long = 0

    //有一些字段并不想映射到数据库表中，添加Ignore注解忽略，这样就不会成为数据库表中的类名了
    @Ignore
    var bitmap: Bitmap? = null

    //嵌套对象的意思,如果cache对象在映射成表的时候，也想把User对象中的name和age也映射到这个表里面去
    //要求：那么User也需要添加Entity，并且有一个不为空的主键PrimaryKey
    @Embedded
    var user: User? = null
}

@Entity(tableName = "table_user")
class User {
    @PrimaryKey
    @NonNull
    val name: String = ""
    val age: Int = 0
}