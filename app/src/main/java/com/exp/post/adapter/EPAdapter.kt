package com.exp.post.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.exp.post.R
import com.exp.post.bean.EpBean

class EPAdapter(
    val click: (EpBean) -> Unit
) : BaseQuickAdapter<EpBean, BaseViewHolder>(R.layout.layout_list_rv_ep) {
    var checkPos = 0
    override fun convert(holder: BaseViewHolder, item: EpBean) {
        val adapterPosition = holder.bindingAdapterPosition
        holder.setText(R.id.name, item.epName)
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