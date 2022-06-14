package ru.bagrusss.rotation

interface DeviceOrientationListener {

    fun onOrientationChanged(angle: Float)

    companion object {
        const val NO_ROTATION = 0f
        const val ROTATE_DEGREES_90 = 90f
        const val ROTATE_DEGREES_180 = 180f
        const val ROTATE_DEGREES_270 = 270f
    }
}