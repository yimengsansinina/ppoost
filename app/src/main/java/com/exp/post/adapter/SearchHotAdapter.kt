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

class SearchHotAdapter(
    val click: (String) -> Unit
) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_hot_history) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.seq_tv, ""+(holder.adapterPosition+1))
        holder.setText(R.id.name_tv, item)
        holder.itemView.setOnClickListener {
            click(item)
        }
    }
}