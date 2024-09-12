package com.exp.post.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.exp.post.R
import com.exp.post.dbs.HistoryPageBean
import com.exp.post.dbs.PageBean
import com.exp.post.tools.GlideUtils
import com.exp.post.tools.MusicProgressUtil
import com.exp.post.tools.TimeUtil

class WatchHistoryAdapter(
    val click: (HistoryPageBean) -> Unit
) : BaseQuickAdapter<HistoryPageBean, BaseViewHolder>(R.layout.item_watch_history) {
    override fun convert(holder: BaseViewHolder, item: HistoryPageBean) {
        holder.setText(R.id.name, item.playName)
        holder.setText(R.id.mark_tv , TimeUtil.formatFriendlyTimestamp(item.time*1000L))
        holder.setText(R.id.info_tv,"觀看至:"+ MusicProgressUtil.formatMusicProgress(item.historyProgress))
        GlideUtils.loadImageCenterCrop(context, item.cover, holder.getView<ImageView>(R.id.cover_iv))
        holder.itemView.setOnClickListener {
            click(item)
        }
    }
}