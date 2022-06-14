package com.vkontakte.core.platform.orientation

import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.annotation.CallSuper
import ru.bagrusss.rotation.DeviceOrientationListener

/**
 * Точка входа для операций над контейнером и его вьюхами при повороте экрана:
 * - поворот на угол,
 * - изменение позиции относительно родителя,
 * - масштабирование и пересчет размеров
 */
interface RotatableContainer : DeviceOrientationListener {

    val viewsToRotate: List<View>

    val animatedViewsToRotate: List<View>

    /**
     * @param angle - в базовой реализации будем вращать вьюшки на значение этого параметра
     */
    @CallSuper
    override fun onOrientationChanged(angle: Float) {
        viewsToRotate.forEach {
            it.rotation = angle
        }
        animateViewsRotation(animatedViewsToRotate, angle)
    }

}

private fun animateViewsRotation(views: List<View>, angle: Float) {
    if (views.isNotEmpty()) {
        val currentAngle = views.first().rotation

        /**
         * Вот тут есть нюанс. Если вращать с 270° на 0° произойдет поворот вьюхи против часовой стрелки
         * на 270° в сторону оменьшения (вместо 90° и поворота с 270 до 360). Чтобы аниматор правильно отработал и высчитал изменение угла
         * на 90° градусов - необходимо сделать коррекцию при переходе от 270° к 0° и наоборот.
         * В конце анимации необходимо установить значение, пришедшее от серсора, чтобы привести значение угла
         * к 0°, 90°, 180° и 270° градусов и вернуться в интервал [0; 360)
         */
        val angleForRotation = when {
            currentAngle % 360 == DeviceOrientationListener.NO_ROTATION && angle == DeviceOrientationListener.ROTATE_DEGREES_270 -> {
                -DeviceOrientationListener.ROTATE_DEGREES_90
            }
            currentAngle == DeviceOrientationListener.ROTATE_DEGREES_270 && angle % 360 == DeviceOrientationListener.NO_ROTATION -> {
                360f
            }
            else -> angle
        }
        views.forEach {
            it.animate()
                .rotation(angleForRotation)
                .setInterpolator(OvershootInterpolator(4f))
                .withEndAction {
                    it.rotation = angle
                }
                .start()
        }
    }
}