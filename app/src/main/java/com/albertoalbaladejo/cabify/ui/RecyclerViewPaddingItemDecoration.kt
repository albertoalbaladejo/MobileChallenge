package com.albertoalbaladejo.cabify.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewPaddingItemDecoration() : RecyclerView.ItemDecoration() {

    companion object {
        private const val PADDING_SPACE = 16
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(PADDING_SPACE, PADDING_SPACE, PADDING_SPACE, PADDING_SPACE)
    }
}