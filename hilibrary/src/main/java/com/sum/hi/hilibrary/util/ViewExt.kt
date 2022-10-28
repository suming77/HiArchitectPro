package com.sum.hi.hilibrary.util

import android.widget.Toast
import java.time.Duration

/**
 * @创建者 mingyan.su
 * @创建时间 2022/10/23 22:04
 * @类描述 ${TODO}
 */
fun <T> T.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(AppGlobals.get(), message, duration).show()