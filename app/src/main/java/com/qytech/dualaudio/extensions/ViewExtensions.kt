package com.qytech.dualaudio.extensions

import android.view.SurfaceView
import timber.log.Timber
import kotlin.math.roundToInt

fun SurfaceView.setFixedVideoSize(videoWidth: Int, videoHeight: Int) {
    var resultWidth = measuredWidth
    var resultHeight = measuredHeight
    Timber.d("video size $videoWidth x $videoHeight view size $resultWidth x $resultHeight")
    val wr = measuredWidth / videoWidth
    val hr = measuredHeight / videoHeight
    val ar = videoWidth.toFloat() / videoHeight
    if (wr > hr) {
        resultWidth = (resultHeight * ar).roundToInt()
    } else {
        resultHeight = (resultWidth / ar).roundToInt()
    }
    Timber.d("video size scaled to  $resultWidth x $resultHeight")
    holder.setFixedSize(resultWidth, resultHeight)
}