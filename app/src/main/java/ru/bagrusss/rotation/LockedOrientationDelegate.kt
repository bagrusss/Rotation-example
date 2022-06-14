package ru.bagrusss.rotation


/**
 * Делегат для управления контейнерами RotatableContainer
 * Хранит текущий угол поворота устройства
 */
interface LockedOrientationDelegate {
    val currentAngle: Float
    val isHorizontal: Boolean

    fun addListener(rotatable: DeviceOrientationListener)
    fun removeListener(rotatable: DeviceOrientationListener)

    companion object {

        val EMPTY by lazy {
            object : LockedOrientationDelegate {
                override val currentAngle = 0f
                override val isHorizontal = false

                override fun addListener(rotatable: DeviceOrientationListener) {}

                override fun removeListener(rotatable: DeviceOrientationListener) {}

            }
        }
    }

}