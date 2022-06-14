package ru.bagrusss.list.holders

import android.view.View
import android.widget.TextView
import ru.bagrusss.list.items.SquareItem
import ru.bagrusss.rotation.LockedOrientationDelegate
import ru.bagrusss.rotationexample.R

class SquareViewHolder(
    parent: View,
    orientationDelegate: LockedOrientationDelegate
) : RotatableViewHolder<SquareItem>(parent, orientationDelegate) {

    private val text = itemView.findViewById<TextView>(R.id.text_view)

    override val animatedViewsToRotate = listOf(text)

    override fun onBind(item: SquareItem) {
        text.text = item.text
    }

}