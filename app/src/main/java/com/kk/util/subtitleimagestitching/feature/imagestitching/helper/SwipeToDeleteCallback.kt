package com.kk.util.subtitleimagestitching.feature.imagestitching.helper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kk.util.subtitleimagestitching.R

class SwipeToDeleteCallback(
    private val onDelete: (Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val background = ColorDrawable(Color.RED)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        onDelete(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView

        // 绘制红色背景
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)

        // 绘制删除图标
        val deleteIcon = ContextCompat.getDrawable(
            recyclerView.context,
            R.drawable.ic_delete
        )
        deleteIcon?.let {
            val iconMargin = (itemView.height - it.intrinsicHeight) / 2
            it.setBounds(
                itemView.right - iconMargin - it.intrinsicWidth,
                itemView.top + iconMargin,
                itemView.right - iconMargin,
                itemView.bottom - iconMargin
            )
            it.draw(c)
        }

        super.onChildDraw(
            c, recyclerView, viewHolder,
            dX, dY, actionState, isCurrentlyActive
        )
    }
}