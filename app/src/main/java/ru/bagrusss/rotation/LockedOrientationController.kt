package ru.bagrusss.rotation

import ru.bagrusss.multiwindow.MultiWindowDelegate


/**
 * Включает и отключает рассылку угла поворота устройства
 * для заинтересованных RotatableContainer
 */
interface LockedOrientationController {

    fun enable(splitDelegate: MultiWindowDelegate)
    fun disable()

}