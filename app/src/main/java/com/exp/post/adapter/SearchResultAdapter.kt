package com.exp.post.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.exp.post.R
import com.exp.post.dbs.HistoryPageBean
import com.exp.post.dbs.PageBean
import com.exp.post.tools.GlideUtils
import com.exp.post.tools.MusicProgressUtil
import com.exp.post.tools.TimeUtil

class SearchResultAdapter(
    val click: (PageBean) -> Unit
) : BaseQuickAdapter<PageBean, BaseViewHolder>(R.layout.item_search_result),LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: PageBean) {
        holder.setText(R.id.name_tv, item.playName)
        holder.setText(R.id.year_tv, item.playYear)
        holder.setText(R.id.actor_tv, "演员:"+item.playActor)
        holder.setText(R.id.area_tv, item.playArea)
        holder.setText(R.id.info_tv, "摘要:"+item.playDesInfo?.trim())
        GlideUtils.loadImageCenterCrop(context, item.cover, holder.getView<ImageView>(R.id.cover_iv))
        holder.itemView.setOnClickListener {
            click(item)
        }
    }
}