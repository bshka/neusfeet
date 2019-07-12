package com.krendel.neusfeet.screens.common.list

import android.widget.ViewFlipper
import androidx.recyclerview.widget.RecyclerView
import com.krendel.neusfeet.screens.common.switchToChild

class AdapterObserver(
    private val viewFlipper: ViewFlipper,
    private val adapter: RecyclerView.Adapter<*>
) : RecyclerView.AdapterDataObserver() {

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        viewFlipper.switchToChild(
            when {
                positionStart == 0 && itemCount == 0 -> 0
                adapter.itemCount == 0 -> 1
                else -> 2
            }
        )
    }
}