package com.exp.post.ui.home
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.exp.post.net.HttpClient
import com.exp.post.net.NetApi
import com.exp.post.bean.HomePageBean
import com.exp.post.bean.HomePageRequest
import com.exp.post.bean.HomePageResponse
import com.exp.post.databinding.FragmentShowBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShowFragment : Fragment() {
    private var _binding: FragmentShowBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    private var topC = 1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            topC = it.getInt("topC")
        }
//        binding.click.setOnClickListener {
//            val intent = Intent(activity, MovieDetailActivity::class.java)
//            activity?.startActivity(intent)
//        }
        adapter.loadMoreModule.setOnLoadMoreListener {
            postNet(true)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            // 模拟数据刷新
            binding.swipeRefreshLayout.setRefreshing(false)
            postNet(false)
        }
        postNet(false)
    }

    private var page = 1
    private fun postNet(loadMore: Boolean) {
        if (loadMore) {
            page++

        } else {
            page = 1

        }
        homePagerT(topC, page, { list, noMore ->
            initView(loadMore,list, noMore)
        }, {
            if (loadMore) {
                adapter.loadMoreModule.loadMoreFail()
            } else {
                //首页失败
            }
        })
    }

    private val adapter by lazy {
        ShowAdapter()
    }

    private fun initView(loadMore: Boolean,list: List<HomePageBean>?, noMore: Boolean) {
        if (binding.recyclerView.adapter==null){
            val layoutManager = GridLayoutManager(requireContext(), 3)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val itemType = binding.recyclerView.adapter?.getItemViewType(position) ?: 0
                    return if (itemType == 0) {
                        3
                    } else {
                        1
                    }
                }
            }
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.adapter = adapter
        }
        if (loadMore){
            adapter.addData(list!!)
        }else{
            adapter.setList(list)

        }
        if (loadMore){
            if (noMore) {
                adapter.loadMoreModule.loadMoreEnd()

            } else {
                adapter.loadMoreModule.loadMoreComplete()

            }
        }else{

        }

    }

    private val mHandler = Handler(Looper.getMainLooper())
    private fun homePagerT(
        top_class: Int,
        pg: Int,
        success: (List<HomePageBean>?, noMore: Boolean) -> Unit,
        fail: (Int) -> Unit
    ) {
//        val map = HashMap<String, String>()
//        map["top_class"] = top_class.toString()
//        map["pg"] = pg.toString()
        val bean = HomePageRequest(top_class.toString(), pg.toString())
        HttpClient.instance.getServer(NetApi::class.java)
            .homePage(bean)
            .enqueue(object : Callback<HomePageResponse> {
                override fun onResponse(
                    call: Call<HomePageResponse>,
                    response: Response<HomePageResponse>
                ) {
                    if (!response.isSuccessful) {
                        mHandler.post {
                            fail(100)
                        }
                        return
                    }
                    val body = response.body()
                    if (body == null) {
                        mHandler.post {
                            fail(101)
                        }
                        return
                    }
                    //
                    if (body.res == null) {
                        mHandler.post {
                            fail(body.code)
                        }
                        return
                    }
                    mHandler.post {
                        success(body.res!!, body.res!!.isEmpty())
                    }
                }

                override fun onFailure(call: Call<HomePageResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: t=" + t.message)
                    mHandler.post {
                        fail(100)
                    }
                }

            })
    }

    companion object {
        const val TAG = "ShowFragment"
        fun INSTANCE(): ShowFragment {
            return ShowFragment()
        }
    }
}
