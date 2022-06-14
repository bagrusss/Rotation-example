package ru.bagrusss.list.holders

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.bagrusss.list.items.ActionItem
import ru.bagrusss.rotation.LockedOrientationDelegate
import ru.bagrusss.rotationexample.R

class ActionViewHolder(
    patent: View,
    rotationDelegate: LockedOrientationDelegate
): RotatableViewHolder<ActionItem>(patent, rotationDelegate) {

    private val smile = itemView.findViewById<TextView>(R.id.smile_view)
    private val action = itemView.findViewById<View>(R.id.action_view)

    override val animatedViewsToRotate = listOf(smile, action)

    init {
        action.setOnClickListener {
            Toast.makeText(it.context, "Click", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onBind(item: ActionItem) {
        smile.text = item.text
    }

}