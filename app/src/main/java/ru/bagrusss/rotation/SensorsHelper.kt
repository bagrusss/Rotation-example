package ru.bagrusss.rotation

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.SensorManager
import android.view.OrientationEventListener
import io.reactivex.rxjava3.core.Observable

object SensorsHelper {

    fun getRotationUpdates(context: Context) = context.getRotation()

    @JvmStatic
    @Rotation
    @SuppressLint("SupportAnnotationUsage")
    fun Context.getRotation(): Observable<Int> {
        var eventListener: OrientationEventListener? = null
        return Observable.create<Int> { emitter ->
            eventListener = object : OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
                init {
                    enable()
                    if (!canDetectOrientation()) {
                        emitter.onNext(Rotation.ROTATION_0)
                    }
                }
                override fun onOrientationChanged(orientation: Int) {
                    when (orientation) {
                        in 60..140 -> emitter.onNext(Rotation.ROTATION_90)
                        in 140..220 -> emitter.onNext(Rotation.ROTATION_180)
                        in 220..300 -> emitter.onNext(Rotation.ROTATION_270)
                        else -> emitter.onNext(Rotation.ROTATION_0)
                    }
                }
            }
        }.doOnDispose {
            eventListener?.disable()
        }
    }
}
