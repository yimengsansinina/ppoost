package com.exp.post.ui.home

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.exp.post.R
import com.exp.post.bean.HomePageBean
import com.exp.post.tools.AndroidUtils

class ShowAdapter() : BaseMultiItemQuickAdapter<HomePageBean, BaseViewHolder>(), LoadMoreModule {
    init {
        addItemType(0, R.layout.layout_show)
        addItemType(1, R.layout.layout_show1)
    }
    override fun convert(holder: BaseViewHolder, item: HomePageBean) {
        when (holder.itemViewType) {
           0 -> {
                holder.setText(R.id.tv_title, item.desTitle)
            }
           1 -> {
               val cover = holder.getView<ImageView>(R.id.cover)
               val layoutParams = cover.layoutParams
               val with = AndroidUtils.phoneW()-AndroidUtils.phoneH()
               item.entity?.let {
                   holder.setText(R.id.name, it.playName)
                   Glide.with(context).load(it.cover).into(holder.getView(R.id.cover))
               }
            }
        }
    }
}