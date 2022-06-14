package ru.bagrusss.multiwindow

import android.app.Activity
import android.os.Build

interface MultiWindowDelegate {
    val isInMultiWindowMode: Boolean

    companion object {
        val EMPTY = object : MultiWindowDelegate {
            override val isInMultiWindowMode = false
        }
    }
}

class MultiWindowDelegateImpl(
    val currentActivity: () -> Activity?
) : MultiWindowDelegate {

    override val isInMultiWindowMode: Boolean
        get() = currentActivity()?.run {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isInMultiWindowMode
        } ?: false
}