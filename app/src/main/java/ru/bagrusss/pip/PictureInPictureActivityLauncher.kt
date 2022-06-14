package ru.bagrusss.pip

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

class PictureInPictureActivityLauncher(
    private val activity: Activity
) {

    private var isDestroyed = false

    fun isSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && activity.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
            && !isBlackListedDevice()
    }

    fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        if (isDestroyed) {
            return
        }

        // handle action

    }

    fun destroy() {
        isDestroyed = true
    }

    private fun isBlackListedDevice(): Boolean {
        return when (Build.MODEL.orEmpty().trim().uppercase(Locale.US)) {
            // Huawei P40 lite
            "JNY-L21A", "JNY-L01A", "JNY-L21B", "JNY-L22A", "JNY-L02A", "JNY-L22B", "JNY-LX1" -> false
            else -> true
        }
    }

}