package ru.bagrusss.rotation

import androidx.annotation.IntDef

@IntDef(value = [Rotation.ROTATION_0, Rotation.ROTATION_90, Rotation.ROTATION_180, Rotation.ROTATION_270])
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class Rotation {
    companion object {
        /**
         * Rotation constant: 0 degree rotation (natural orientation)
         */
        const val ROTATION_0 = 0

        /**
         * Rotation constant: 90 degree rotation.
         */
        const val ROTATION_90 = 1

        /**
         * Rotation constant: 180 degree rotation.
         */
        const val ROTATION_180 = 2

        /**
         * Rotation constant: 270 degree rotation.
         */
        const val ROTATION_270 = 3
    }
}