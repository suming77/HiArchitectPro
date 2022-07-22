package com.sum.hi.ui.biz.notice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.sum.hi.ui.R
import com.sum.hi.ui.hiitem.HiAdapter
import com.sum.hi.ui.model.CourseNotice
import com.sum.hi.ui.model.Notice
import kotlinx.android.synthetic.main.activity_notice_list.*

@Route(path = "/notice/list")
class NoticeListActivity : AppCompatActivity() {
    private lateinit var adapter: HiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_list)
        initView()
    }

    private fun initView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = HiAdapter(this)
        recycler_view.adapter = adapter

        val list = mutableListOf<NoticeItem>()
        for (i in 0..20) {
            val item = Notice(i.toString(), 0, "0", "标题", "二级标题", "https://www.baidu.com", "", "1658469369")
            list.add(NoticeItem(item))
        }
        adapter.addItems(list, true)
    }
}