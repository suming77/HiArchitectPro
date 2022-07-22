package com.sum.hi.ui.biz.notice

import android.content.Intent
import android.net.Uri
import android.text.format.DateUtils
import androidx.core.content.ContextCompat
import com.sum.hi.hilibrary.HiViewHolder
import com.sum.hi.hilibrary.util.HiDateUtil
import com.sum.hi.ui.R
import com.sum.hi.ui.demo.DataUtils
import com.sum.hi.ui.hiitem.HiDataItem
import com.sum.hi.ui.model.Notice
import kotlinx.android.synthetic.main.layou_notice_item.view.*

/**
 * @author smy
 * @date 2022/7/22
 * @desc
 */
internal class NoticeItem(itemNotice: Notice) : HiDataItem<Notice, HiViewHolder>(itemNotice) {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        mData?.apply {
//            holder.itemView.date.text = HiDateUtil.getMMDate(createTime)
            holder.itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                ContextCompat.startActivity(holder.itemView.context, intent, null)
            }
        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layou_notice_item
    }
}