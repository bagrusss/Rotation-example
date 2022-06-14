package ru.bagrusss.utils

import android.content.res.Resources
import kotlin.math.floor

fun getDensity() = Resources.getSystem().displayMetrics.density

val Int.dp: Int
    get() = floor((getDensity() * this)).toInt()