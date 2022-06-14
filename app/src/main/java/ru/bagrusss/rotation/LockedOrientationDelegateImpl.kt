package ru.bagrusss.rotation

import android.content.Context
import android.database.ContentObserver
import android.provider.Settings
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import ru.bagrusss.multiwindow.MultiWindowDelegate
import java.util.concurrent.TimeUnit

class LockedOrientationDelegateImpl(
    private val context: Context
) : LockedOrientationController, LockedOrientationDelegate {

    private var orientationDisposable = Disposable.disposed()

    private val views = mutableListOf<DeviceOrientationListener>()

    @Volatile
    private var currentSystemAutorotateEnabled = false

    private val rotationUri = Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION)
    private val observer = object : ContentObserver(null) {

        override fun onChange(selfChange: Boolean) {
            if (!syncSystemSettingsValue()) {
                currentAngle = DeviceOrientationListener.NO_ROTATION.also(::notifyRotation)
            }
        }

    }

    private var splitDelegate: MultiWindowDelegate = MultiWindowDelegate.EMPTY

    @Volatile
    override var currentAngle: Float = DeviceOrientationListener.NO_ROTATION
        private set

    override val isHorizontal: Boolean
        get() = currentAngle.let { it == DeviceOrientationListener.ROTATE_DEGREES_270 || it == DeviceOrientationListener.ROTATE_DEGREES_90 }

    override fun enable(splitDelegate: MultiWindowDelegate) {
        this.splitDelegate = splitDelegate
        syncSystemSettingsValue()
        context.contentResolver.registerContentObserver(rotationUri, false, observer)
        orientationDisposable = SensorsHelper.getRotationUpdates(context)
            .throttleLast(300, TimeUnit.MILLISECONDS)
            .filter { currentSystemAutorotateEnabled }
            .filter { !this.splitDelegate.isInMultiWindowMode }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { rotation ->
                /**
                 * Для того, чтобы повернуть View на угол, который пришел от сенсора, необходимо использовать отрицательное значение
                 * (или `360° - x°`) т.к. это, фактически, - компенсация поворота. Причина в том, в значении угла поворота сенсора
                 * и View.rotation есть различия:
                 * 90° у сенсора означает, что сверху находится левая сторона
                 * 90° у View означает, что сверу находится правая сторона
                 * ¯\_(ツ)_/¯
                 */
                val angle = when (rotation) {
                    Rotation.ROTATION_0 -> DeviceOrientationListener.NO_ROTATION
                    Rotation.ROTATION_90 -> DeviceOrientationListener.ROTATE_DEGREES_270
                    Rotation.ROTATION_180 -> DeviceOrientationListener.ROTATE_DEGREES_180
                    else -> DeviceOrientationListener.ROTATE_DEGREES_90
                }
                currentAngle = angle
                notifyRotation(angle)
            }
        notifyRotation(currentAngle)
    }

    override fun disable() {
        splitDelegate = MultiWindowDelegate.EMPTY
        context.contentResolver.unregisterContentObserver(observer)
        orientationDisposable.dispose()
    }

    override fun addListener(rotatable: DeviceOrientationListener) {
        views.add(rotatable)
        rotatable.onOrientationChanged(currentAngle)
    }

    override fun removeListener(rotatable: DeviceOrientationListener) {
        views.remove(rotatable)
    }

    private fun notifyRotation(angle: Float) = views.forEach { view ->
        view.onOrientationChanged(angle)
    }

    private fun syncSystemSettingsValue(): Boolean {
        return (Settings.System.getInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1).also {
            currentSystemAutorotateEnabled = it
        }
    }
}