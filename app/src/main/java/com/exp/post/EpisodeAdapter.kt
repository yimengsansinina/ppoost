package com.exp.post
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.exp.post.bean.EpBean
import com.exp.post.tools.AndroidUtils

class EpisodeAdapter(
     var selectedPosition: Int = -1,
    private val onItemClick: (Int,EpBean) -> Unit
) : RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {

    private val episodes = mutableListOf<EpBean>()

    fun updateData(newEpisodes: List<EpBean>) {
        episodes.clear()
        episodes.addAll(newEpisodes)
        notifyDataSetChanged()
    }

    fun updateSelected(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(oldPosition)
        notifyItemChanged(selectedPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_episode, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val phoneW = AndroidUtils.phoneW()
        val with = (phoneW - AndroidUtils.dp2px(13*12f)) / 4
        Log.d("EpisodeAdapter", "onBindViewHolder() called with: with = $with, position = $position")
        val view = holder.itemView.findViewById<ConstraintLayout>(R.id.constraint)
        val layoutParams = view.layoutParams
        layoutParams.width = with
        view.layoutParams = layoutParams
        holder.bind(episodes[position], position == selectedPosition)
    }

    override fun getItemCount() = episodes.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvEpisode: TextView = itemView.findViewById(R.id.tvEpisode)
        private val tvPlaying: TextView = itemView.findViewById(R.id.tvPlaying)
        private val constraint: View = itemView.findViewById(R.id.constraint)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    updateSelected(position)
                    onItemClick(position,episodes[position])
                }
            }
        }

        fun bind(episode: EpBean, isSelected: Boolean) {
            tvEpisode.text = episode.epName
            tvPlaying.visibility = if (isSelected) View.VISIBLE else View.GONE
            if (isSelected){
                constraint.setBackgroundResource(R.drawable.drawable_ep_selected)
            }else{
                constraint.setBackgroundResource(R.drawable.drawable_ep_unselected)
            }
//            cardView.setCardBackgroundColor(
//                if (isSelected)
//                    Color.parseColor("#E3F2FD")
//                else
//                    Color.parseColor("#F5F5F5")
//            )
        }
    }
} 