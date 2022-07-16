package com.sum.hi.common.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.sum.hi.common.R
import com.sum.hi.hilibrary.util.HiViewUtil

/**
 * @创建者 mingyan.su
 * @创建时间 2022/06/04 00:05
 * @类描述 ${TODO} 图片加载拓展类
 */

fun ImageView.loadUrl(url: String) {
    if (HiViewUtil.isActivityDestroy(context)) {
        return
    }
    Glide.with(this).load(url).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
        .into(this)
}

fun ImageView.loadUrl(url: String, callBack: (Drawable) -> Unit) {
    if (HiViewUtil.isActivityDestroy(context)) {
        return
    }
    //you cannot load url form destroy activity
    Glide.with(this).load(url).placeholder(R.mipmap.ic_launcher_round)
        .error(R.mipmap.ic_launcher_round).into(object : SimpleTarget<Drawable>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            callBack(resource)
        }
    })
}

fun ImageView.loadCircle(url: String) {
    if (HiViewUtil.isActivityDestroy(context)) {
        return
    }
    Glide.with(this).load(url).transform(CircleCrop()).into(this)
}

//巨坑，glide的图片裁剪和ImageView  scaleType有冲突
fun ImageView.loadCorner(url: String, corner: Int) {
    if (HiViewUtil.isActivityDestroy(context)) {
        return
    }
    Glide.with(this).load(url).transform(CenterCrop(), RoundedCorners(corner)).into(this)
//    Glide.with(this).load(url).transform(RoundedCorners(corner)).into(this)
}

fun ImageView.loadCircleBorder(
    url: String,
    borderWidth: Float = 0f,
    borderColor: Int = Color.WHITE
) {

}

class CircleBorderTransform(val borderWidth: Float, val borderColor: Int) : CircleCrop() {
    private var borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        /**
         * 因为继承自CircleCrop，并且CircleCrop是圆形，在这里获取的bitmap是裁剪后的圆形bitmap
         */
        val transform = super.transform(pool, toTransform, outWidth, outHeight)
        val canvas = Canvas(transform)
        val halfWidth = (outWidth / 2).toFloat()
        val halfHeight = (outHeight / 2).toFloat()
        canvas.drawCircle(
            halfWidth,
            halfHeight,
            Math.min(halfWidth, halfHeight) - borderWidth / 2,
            borderPaint
        )
        canvas.setBitmap(null)
        return transform
    }
}