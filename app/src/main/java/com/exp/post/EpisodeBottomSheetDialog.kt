//package com.exp.post
//import android.content.Context
//import android.os.Bundle
//import android.widget.ImageView
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.exp.post.tools.GradleItemDecord
//import com.google.android.material.bottomsheet.BottomSheetDialog
//
//class EpisodeBottomSheetDialog(
//    context: Context,
//    private val totalEpisodes: Int,
//    private val initialSelected: Int = 0,
//    theme :Int,
//    private val onEpisodeSelected: (Int) -> Unit
//) : BottomSheetDialog(context,theme) {
//
//    private lateinit var adapter: EpisodeAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.dialog_episode_list)
//
//        setupViews()
//    }
//
//    private fun setupViews() {
//        findViewById<ImageView>(R.id.ivClose)?.setOnClickListener {
//            dismiss()
//        }
//
//        adapter = EpisodeAdapter(initialSelected) { position ->
//            onEpisodeSelected(position)
//        }
//
//        findViewById<RecyclerView>(R.id.recyclerView)?.apply {
//            layoutManager = GridLayoutManager(context, 5)
//            adapter = this@EpisodeBottomSheetDialog.adapter
//            addItemDecoration(GradleItemDecord())
//        }
//
//        // 生成集数列表
//        val episodes = (1..totalEpisodes).toList()
//        adapter.updateData(episodes)
//    }
//}