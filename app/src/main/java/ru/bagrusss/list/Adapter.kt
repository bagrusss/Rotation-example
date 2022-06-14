package ru.bagrusss.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.bagrusss.list.holders.ActionViewHolder
import ru.bagrusss.list.holders.RotatableViewHolder
import ru.bagrusss.list.holders.SquareViewHolder
import ru.bagrusss.list.items.ActionItem
import ru.bagrusss.list.items.ListItem
import ru.bagrusss.list.items.SquareItem
import ru.bagrusss.rotation.LockedOrientationDelegate
import ru.bagrusss.rotationexample.R

class Adapter(
    private val orientationDelegate: LockedOrientationDelegate
) : RecyclerView.Adapter<RotatableViewHolder<*>>() {

    private val items: MutableList<ListItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RotatableViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_1 -> ActionViewHolder(inflater.inflate(R.layout.action_list_item, parent, false), orientationDelegate)
            TYPE_2 -> SquareViewHolder(inflater.inflate(R.layout.square_list_item, parent, false), orientationDelegate)
            else -> TODO()
        }
    }

    override fun getItemViewType(position: Int) = when (items[position]) {
        is ActionItem -> TYPE_1
        else -> TYPE_2
    }

    override fun onBindViewHolder(holder: RotatableViewHolder<*>, position: Int) {
        val item = items[position]
        when (holder) {
            is ActionViewHolder -> holder.onBind(item as ActionItem)
            is SquareViewHolder -> holder.onBind(item as SquareItem)
        }
    }

    override fun getItemCount() = items.size

    override fun onViewAttachedToWindow(holder: RotatableViewHolder<*>) = holder.onViewAttachedToWindow()

    override fun onViewDetachedFromWindow(holder: RotatableViewHolder<*>) = holder.onViewDetachedFromWindow()

    fun updateItems(newItems: List<ListItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    companion object {
        private const val TYPE_1 = 1
        private const val TYPE_2 = 2
    }

}