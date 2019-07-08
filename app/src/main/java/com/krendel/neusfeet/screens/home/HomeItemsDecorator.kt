package com.krendel.neusfeet.screens.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.krendel.neusfeet.screens.common.applyDP

class HomeItemsDecorator: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val pos = parent.getChildAdapterPosition(view)
        val margin = view.applyDP(15f)
        outRect.top = if (pos == 0) {
            margin
        } else {
            0
        }
        outRect.left = margin
        outRect.right = margin
        outRect.bottom = margin
    }
}