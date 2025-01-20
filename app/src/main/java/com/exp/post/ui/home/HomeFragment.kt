package com.exp.post.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.exp.post.databinding.FragmentHomeBinding
import com.exp.post.tools.SPTools
import com.exp.post.ui.SearchActivity
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchFl.setOnClickListener {
            SearchActivity.nav(requireActivity())
        }
        val pageList = SPTools.getPageList()
        val adapter = if (pageList.isNotEmpty() ) {
            val with = FragmentPagerItems.with(activity)
            pageList.forEach {loginAppBean ->
                with.add(
                    loginAppBean.title,
                    ShowFragment::class.java,
                    bundleOf("topC" to loginAppBean.topC)
                )
            }
            FragmentPagerItemAdapter(childFragmentManager,with.create())
        } else {
            FragmentPagerItemAdapter(
                childFragmentManager, FragmentPagerItems.with(activity)
                    .add("tv", ShowFragment::class.java, bundleOf("topC" to 2))
                    .add("mov", ShowFragment::class.java, bundleOf("topC" to 1))
                    .add("dm", ShowFragment::class.java, bundleOf("topC" to 3))
                    .add("zy", ShowFragment::class.java, bundleOf("topC" to 4))
                    .add("record", ShowFragment::class.java, bundleOf("topC" to 5))
                    .create()
            )
        }

        binding.viewpager.offscreenPageLimit = 5
        binding.viewpager.adapter = adapter

        binding.viewpagertab.setViewPager(binding.viewpager)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}