package com.exp.post.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.exp.post.R
class RouteAdapter(
    val click: (Int) -> Unit
) : BaseQuickAdapter<Int, BaseViewHolder>(R.layout.layout_list_rv_ep) {
    var checkPos = 0
    override fun convert(holder: BaseViewHolder, item: Int) {
        val adapterPosition = holder.bindingAdapterPosition
        holder.setText(R.id.name, "线路${item+1}")
        if (adapterPosition==checkPos) {
            holder.setTextColor(R.id.name,context.resources.getColor(R.color.puc61a01))
        } else {
            holder.setTextColor(R.id.name,context.resources.getColor(R.color.black))
        }
        holder.itemView.setOnClickListener {
            if (checkPos == adapterPosition) {
                return@setOnClickListener
            }
            checkPos = adapterPosition
            notifyDataSetChanged()
            click(item)
        }
    }
}