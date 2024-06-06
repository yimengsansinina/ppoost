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

        val adapter = FragmentPagerItemAdapter(
            childFragmentManager, FragmentPagerItems.with(activity)
                .add("电视剧", ShowFragment::class.java, bundleOf("topC" to 2))
                .add("电影", ShowFragment::class.java, bundleOf("topC" to 1))
                .add("动漫", ShowFragment::class.java, bundleOf("topC" to 3))
                .add("综艺", ShowFragment::class.java, bundleOf("topC" to 4))
                .add("记录片", ShowFragment::class.java, bundleOf("topC" to 5))
                .create()
        )

        binding.viewpager.adapter = adapter

        binding.viewpagertab.setViewPager( binding.viewpager)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}