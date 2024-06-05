package com.exp.post.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.exp.post.R
import com.exp.post.bean.MovieListBean

class MovieListAdapter(
    val click: (MovieListBean) -> Unit
) : BaseQuickAdapter<MovieListBean, BaseViewHolder>(R.layout.layout_list_rv1) {
     var checkPos = 0
    override fun convert(holder: BaseViewHolder, item: MovieListBean) {
        val adapterPosition = holder.bindingAdapterPosition
        holder.setText(R.id.name, item.name)
        if (adapterPosition==checkPos) {
            holder.setBackgroundResource(R.id.name, R.drawable.shape_normal_yes)
            holder.setTextColor(R.id.name,context.resources.getColor(R.color.puc61a01))
        } else {
            holder.setTextColor(R.id.name,context.resources.getColor(R.color.pu8c8e94))
            holder.setBackgroundResource(R.id.name, R.drawable.shape_normal_no)
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
