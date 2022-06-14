package ru.bagrusss.list.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.vkontakte.core.platform.orientation.RotatableContainer
import ru.bagrusss.list.items.ListItem
import ru.bagrusss.rotation.LockedOrientationDelegate

abstract class RotatableViewHolder<T: ListItem>(
    parent: View,
    protected val orientationDelegate: LockedOrientationDelegate
): RecyclerView.ViewHolder(parent), RotatableContainer {

    override val viewsToRotate = emptyList<View>()
    override val animatedViewsToRotate = listOf(itemView)

    fun onViewAttachedToWindow() = orientationDelegate.addListener(this)
    fun onViewDetachedFromWindow() = orientationDelegate.removeListener(this)

    abstract fun onBind(item: T)

}