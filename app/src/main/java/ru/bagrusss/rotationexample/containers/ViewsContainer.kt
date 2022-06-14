package ru.bagrusss.rotationexample.containers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.updateLayoutParams
import com.vkontakte.core.platform.orientation.RotatableContainer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import ru.bagrusss.rotation.DeviceOrientationListener
import ru.bagrusss.rotation.LockedOrientationDelegate
import ru.bagrusss.rotationexample.R
import ru.bagrusss.utils.dp
import java.util.concurrent.TimeUnit

class ViewsContainer(
    private val topView: TextView,
    private val bottomView: TextView,
    private val image: ImageView,
    @IdRes private val bottomId: Int,
    private val orientationDelegate: LockedOrientationDelegate
) : RotatableContainer {

    override val viewsToRotate = listOf(
        topView,
        bottomView,

        image
    )
    override val animatedViewsToRotate: List<View> = emptyList()

    private var intervalDisposable = Disposable.empty()

    init {
        orientationDelegate.addListener(this)

        intervalDisposable = Observable.interval(0, 2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val text = if (it % 2 == 0L) {
                    R.string.top_long_text
                } else {
                    R.string.top_text
                }
                updateTopText(text)
            }
    }

    override fun onOrientationChanged(angle: Float) {
        super.onOrientationChanged(angle)

        when (angle) {
            DeviceOrientationListener.ROTATE_DEGREES_90 -> {
                topView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToTop = bottomId
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

                    startToStart = ConstraintLayout.LayoutParams.UNSET

                    topMargin = 0
                    verticalBias = 0.5f
                }
                topView.doOnPreDraw {
                    it.translationX = it.width / 2f
                }

                bottomView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToTop = bottomId
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID

                    endToEnd = ConstraintLayout.LayoutParams.UNSET

                    bottomMargin = 0
                    verticalBias = 0.5f
                }
                bottomView.doOnPreDraw {
                    it.translationX = it.width / -2f
                }

                // just comment bottom line to see the difference
                scaleBlurredBg(false)
            }
            DeviceOrientationListener.ROTATE_DEGREES_270 -> {
                topView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToTop = bottomId
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID

                    endToEnd = ConstraintLayout.LayoutParams.UNSET

                    topMargin = 0
                    verticalBias = 0.5f
                }
                topView.doOnPreDraw {
                    it.translationX = it.width / -2f
                }

                bottomView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToTop = bottomId
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

                    startToStart = ConstraintLayout.LayoutParams.UNSET

                    bottomMargin = 0
                    verticalBias = 0.5f
                }
                bottomView.doOnPreDraw {
                    it.translationX = it.width / 2f
                }

                // just comment bottom line to see the difference
                scaleBlurredBg(false)
            }
            else -> {
                topView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID

                    bottomToBottom = ConstraintLayout.LayoutParams.UNSET

                    topMargin = 32.dp
                    verticalBias = 0f
                }
                topView.translationX = 0f

                bottomView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    bottomToTop = bottomId
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID

                    topToTop = ConstraintLayout.LayoutParams.UNSET

                    bottomMargin = 12.dp
                    verticalBias = 0f
                }
                bottomView.translationX = 0f
                scaleBlurredBg(true)
            }
        }
    }

    fun destroy() {
        intervalDisposable.dispose()
        orientationDelegate.removeListener(this)
    }

    private fun updateTopText(@StringRes newText: Int) {
        topView.setText(newText)
        when (orientationDelegate.currentAngle) {
            DeviceOrientationListener.ROTATE_DEGREES_90 -> {
                topView.doOnPreDraw {
                    it.translationX = it.width / 2f
                }
            }
            DeviceOrientationListener.ROTATE_DEGREES_270 -> {
                topView.doOnPreDraw {
                    it.translationX = it.width / -2f
                }
            }
            else -> {
                topView.translationX = 0f
            }
        }
    }

    private fun scaleBlurredBg(isVertical: Boolean) = image.run {
        if (width != height) {
            if (isVertical) {
                scaleX = 1f
                scaleY = 1f
            } else {
                if (width > height) {
                    scaleY = width / height.toFloat()
                } else {
                    scaleX = height / width.toFloat()
                }
            }
        }
    }

}