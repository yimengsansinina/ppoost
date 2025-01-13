package com.exp.post.ui.notifications

import android.R.attr.value
import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.exp.post.R
import com.exp.post.bean.PayBean

class MemberPayAdapter(val click: (PayBean) -> Unit) :
    BaseMultiItemQuickAdapter<PayBean, BaseViewHolder>() {
    init {
        addItemType(0, R.layout.layout_pay_member_title);
        addItemType(1, R.layout.layout_pay_member);
    }

    //R.layout.layout_pay_member
    private var selectPos = 1
    override fun convert(holder: BaseViewHolder, item: PayBean) {

        if (holder.itemViewType == 0) {
        } else {
            holder.setText(R.id.title_tv,item.title)
            holder.setText(R.id.num_tv,"$${item.num}")
            holder.setVisible(R.id.choose, holder.bindingAdapterPosition == 1)
            holder.getView<View>(R.id.item_root).setOnClickListener {
                val pos = holder.bindingAdapterPosition
                if (pos == selectPos) {
                    return@setOnClickListener
                }
                selectPos = pos
                click(item)
                notifyDataSetChanged()
            }
            holder.getView<View>(R.id.cb).setOnClickListener {
                val pos = holder.bindingAdapterPosition
                if (pos == selectPos) {
                    return@setOnClickListener
                }
                selectPos = pos
                click(item)
                notifyDataSetChanged()
            }
            if (holder.bindingAdapterPosition == selectPos) {
                holder.getView<View>(R.id.item_root).isSelected = true
                holder.getView<View>(R.id.cb).isSelected = true
            } else {
                holder.getView<View>(R.id.item_root).isSelected = false
                holder.getView<View>(R.id.cb).isSelected = false
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            0
        } else {
            1
        }
    }
}