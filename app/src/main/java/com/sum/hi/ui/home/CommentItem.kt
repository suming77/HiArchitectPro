package com.sum.hi.ui.home

import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.sum.hi.common.view.loadCircle
import com.sum.hi.hilibrary.HiViewHolder
import com.sum.hi.hilibrary.util.HiDisplayUtil
import com.sum.hi.ui.R
import com.sum.hi.ui.hiitem.HiDataItem
import com.sum.hi.ui.model.DetailModel
import kotlinx.android.synthetic.main.layout_detail_item_comment.view.*
import kotlinx.android.synthetic.main.layout_detail_item_comment_item.view.*
import kotlin.math.min

/**
 * @author smy
 * @date 2022/7/13
 * @desc
 */
class CommentItem(val model: DetailModel) : HiDataItem<DetailModel, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        holder.itemView.comment_title.text = model.commentCountTitle
        val commentTags: String? = model.commentTags
        if (commentTags != null) {
            val split = commentTags.split(" ")
            if (!split.isNullOrEmpty()) {
                //这样写会存在复用的问题,在创建标签时，需要检查复用
                val chipGroup = holder.itemView.chip_group
                for (index in split.indices) {
                    val chipLabel = if (index < chipGroup.childCount) {
                        chipGroup[index] as Chip
                    } else {
                        val chip = Chip(context)
                        chip.chipCornerRadius = HiDisplayUtil.dp2px(4f).toFloat()
                        chip.chipBackgroundColor =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    R.color.color_faf0
                                )
                            )
                        chip.setTextColor(ContextCompat.getColor(context, R.color.color_999))
                        chip.textSize = 14f
                        chip.gravity = Gravity.CENTER
                        chip.isCheckedIconVisible = false
                        chip.isCheckable = false
                        chip.isChipIconVisible = false

                        chipGroup.addView(chip)
                        chip
                    }
                    chipLabel.text = split[index]
                }
            }
        }

        model.commentModels?.let {
            val commentContainer = holder.itemView.comment_container
            //最多显示三条
            for (index in 1..min(it.size - 1, 3)) {
                val commentModel = it[index]
                //复用问题
                val itemView = if (index < commentContainer.childCount) {
                    commentContainer.getChildAt(index)
                } else {
                    val view = LayoutInflater.from(context)
                        .inflate(R.layout.layout_detail_item_comment_item, commentContainer, false)
                    commentContainer.addView(view)
                    view
                }
                itemView.user_avatar.loadCircle(commentModel.avatar)
                itemView.user_name.text = commentModel.nickName
                itemView.comment_content.text = commentModel.content
            }
        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_comment
    }
}