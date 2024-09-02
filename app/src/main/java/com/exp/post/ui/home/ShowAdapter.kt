package com.exp.post.ui.home

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.exp.post.R
import com.exp.post.bean.HomePageBean
import com.exp.post.dbs.PageBean
import com.exp.post.tools.AndroidUtils

class ShowAdapter(val  click:(PageBean)->Unit) : BaseMultiItemQuickAdapter<HomePageBean, BaseViewHolder>(), LoadMoreModule {
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
               val w = (AndroidUtils.phoneW() - AndroidUtils.dp2px(60f)) / 3
               layoutParams.width = w.toInt()
               layoutParams.height = ((347 / 267f) * w).toInt()
               cover.layoutParams = layoutParams
               holder.setText(R.id.name, item.entity?.playName)
               val requestOptions = RequestOptions()
                   .transform(CenterCrop(), RoundedCorners(AndroidUtils.dp2px(5f))) // 设置圆角为 10px

               Glide.with(context).load(item.entity?.cover)
                   .apply(requestOptions).into(holder.getView(R.id.cover))
               holder.setText(R.id.score, item.entity?.playScore)
               holder.setText(R.id.mark, item.entity?.playMark)
               holder.itemView.setOnClickListener {
                   click(item.entity!!)
               }
            }
        }
    }
}