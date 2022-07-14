package com.sum.hi.hilibrary.util

import android.graphics.Color

/**
 * @author smy
 * @date 2022/7/14
 * @desc
 */
object ColorUtil {
    //根据百分比，计算start --> end之间的中间色
    fun getCurrentColor(startColor: Int, endColor: Int, fraction: Float): Int {
        //color= A　Ｒ　Ｇ　Ｂ
        //endColor(argb) - startColor(argb) = diffrence(argb)
        //startColor(argb) + diffrence(argb) * fraction = newColor

        val redStart = Color.red(startColor)
        val greenStart = Color.green(startColor)
        val blueStart = Color.blue(startColor)
        val alphaStart = Color.alpha(startColor)

        val redEnd = Color.red(endColor)
        val greenEnd = Color.green(endColor)
        val blueEnd = Color.blue(endColor)
        val alphaEnd = Color.alpha(endColor)

        val diffRed = redEnd - redStart
        val diffGreen = greenEnd - greenStart
        val diffBlue = blueEnd - blueStart
        val diffAlpha = alphaEnd - alphaStart

        val redCurrent = (redStart + diffRed * fraction).toInt()
        val greenCurrent = (greenStart + diffGreen * fraction).toInt()
        val blueCurrent = (blueStart + diffBlue * fraction).toInt()
        val alphaCurrent = (alphaStart + diffAlpha * fraction).toInt()

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent)
    }
}