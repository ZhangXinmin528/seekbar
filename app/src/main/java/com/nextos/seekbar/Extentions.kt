package com.nextos.seekbar

import android.content.res.Resources
import android.util.TypedValue
import java.math.BigDecimal

/**
 * Created by ZhangXinmin on 2022/09/01.
 * Copyright (c) 2022/9/1 . All rights reserved.
 * 扩展工具
 */

val Float.roundHalfUp
    get() = BigDecimal(this.toDouble()).setScale(2, BigDecimal.ROUND_HALF_UP).toFloat()

// dp to px
val Float.dpToPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

// sp to px
val Float.spToPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this, Resources.getSystem().displayMetrics
    )

//dp to px
val Int.dpToPx
    get() = this.toFloat().dpToPx

//dsp to px
val Int.spToPx
    get() = this.toFloat().spToPx
