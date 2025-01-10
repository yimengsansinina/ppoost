package com.exp.post.tools

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GradleItemDecord : RecyclerView.ItemDecoration() {
     val COW_LINE  by lazy {
         AndroidUtils.dp2px(13f)
     }
    val ROW_LINE  by lazy {
         AndroidUtils.dp2px(15f)
     }
    companion object{

    }
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
//        super.getItemOffsets(outRect, view, parent, state)
        val layoutManager = parent.layoutManager as GridLayoutManager
        val spanCount = layoutManager.spanCount
        val childAdapterPosition = parent.getChildAdapterPosition(view)
        val col = childAdapterPosition%spanCount
        outRect.left = COW_LINE-col*COW_LINE/spanCount
        outRect.right = (col+1)*COW_LINE/spanCount
        outRect.bottom = COW_LINE
    }
}