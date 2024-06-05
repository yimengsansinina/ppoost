package com.exp.post.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.exp.post.R
import com.exp.post.bean.PageBean
import com.exp.post.tools.AndroidUtils


class MovieListContentAdapter() :
    BaseQuickAdapter<PageBean, BaseViewHolder>(R.layout.layout_show1), LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: PageBean) {
        val cover = holder.getView<ImageView>(R.id.cover)
        val layoutParams = cover.layoutParams
        val w = (AndroidUtils.phoneW() - AndroidUtils.dp2px(64f)) / 3
        layoutParams.width = w.toInt()
        layoutParams.height = ((347 / 267f) * w).toInt()
        cover.layoutParams = layoutParams
        holder.setText(R.id.name, item.playName)
        val requestOptions = RequestOptions()
            .transform(CenterCrop(), RoundedCorners(AndroidUtils.dp2px(5f))) // 设置圆角为 10px

        Glide.with(context).load(item.cover)
            .apply(requestOptions).into(holder.getView(R.id.cover))
        holder.setText(R.id.score, item.playScore)
        holder.setText(R.id.mark, item.playMark)

    }
}