package com.exp.post.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.exp.post.R
import com.exp.post.dbs.PageBean
import com.exp.post.tools.GlideUtils

class WatchHistoryAdapter(
    val click: (PageBean) -> Unit
) : BaseQuickAdapter<PageBean, BaseViewHolder>(R.layout.item_watch_history) {
    override fun convert(holder: BaseViewHolder, item: PageBean) {
        holder.setText(R.id.name, item.playName)
        holder.setText(R.id.info_tv, item.playDesInfo)
        holder.setText(R.id.mark_tv, item.playMark)
        GlideUtils.loadImageCenterCrop(context, item.cover, holder.getView<ImageView>(R.id.cover_iv))
        holder.itemView.setOnClickListener {
            click(item)
        }
    }
}