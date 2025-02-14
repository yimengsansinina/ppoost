package com.exp.post.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.exp.post.R
class RouteAdapter(
    val click: (Int) -> Unit
) : BaseQuickAdapter<Int, BaseViewHolder>(R.layout.layout_list_rv_ep) {
    var checkIndex = 0
    override fun convert(holder: BaseViewHolder, item: Int) {
        holder.setText(R.id.name, "线路${item+1}")
        if (item==checkIndex) {
            holder.setTextColor(R.id.name,context.resources.getColor(R.color.puc61a01))
        } else {
            holder.setTextColor(R.id.name,context.resources.getColor(R.color.black))
        }
        holder.itemView.setOnClickListener {
            if (checkIndex == item) {
                return@setOnClickListener
            }
            checkIndex = item
            notifyDataSetChanged()
            click(item)
        }
    }
}