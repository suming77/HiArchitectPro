package com.sum.hi.hiui.search

import android.text.Editable
import android.text.TextWatcher

/**
 * @创建者 mingyan.su
 * @创建时间 2022/09/18 11:22
 * @类描述 ${TODO}这样不用每个方法都实现
 */
open class SimpleTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }
}