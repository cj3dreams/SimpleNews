package com.twenty2byte.simplenews.view.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.twenty2byte.simplenews.R
import com.twenty2byte.simplenews.base.RoomViewModelFactory
import com.twenty2byte.simplenews.base.ViewModelFactory
import com.twenty2byte.simplenews.repository.NewsRepository
import com.twenty2byte.simplenews.repository.RoomRepository
import com.twenty2byte.simplenews.source.local.NewsEntity
import com.twenty2byte.simplenews.source.remote.RemoteDataSource
import com.twenty2byte.simplenews.source.remote.Resource
import com.twenty2byte.simplenews.source.remote.RestApiRequests
import com.twenty2byte.simplenews.view.adapter.NewsRecyclerViewAdapter
import com.twenty2byte.simplenews.vm.NewsViewModel
import com.twenty2byte.simplenews.vm.RoomViewModel
import java.util.*
import android.os.Build
import android.os.Handler
import androidx.activity.OnBackPressedCallback
import com.facebook.shimmer.ShimmerFrameLayout


class HomeFragment : Fragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
//    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var noDataRelativeLayout: RelativeLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: NewsRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var roomViewModel: RoomViewModel
    private lateinit var viewModel: NewsViewModel

    private val factory = ViewModelFactory(NewsRepository(RemoteDataSource().buildApi(RestApiRequests::class.java)))

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val roomFactory = RoomViewModelFactory(RoomRepository(context))
        roomViewModel = ViewModelProvider(this, roomFactory)[RoomViewModel::class.java]
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel.getNews("us", "3d1ea4a2c49e477fafc161982d26ea57")

        val view = layoutInflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        noDataRelativeLayout = view.findViewById(R.id.noDataRel)
//        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container)
        swipeRefreshLayout = view.findViewById(R.id.swipe_container_home)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.home_color, R.color.favorite_color,
            R.color.teal_700)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.newsResponse.observe(viewLifecycleOwner, Observer { resource ->
            when (resource){
                is Resource.Success -> {
                    roomViewModel.setNewsToDb(resource.value.articles)
                    roomViewModel.getAllNewsObservers().observe(viewLifecycleOwner, Observer {
                        adapter = NewsRecyclerViewAdapter(requireContext(), it, this)
                        recyclerView.adapter = adapter
                    })
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(),
                        if (resource.isNetworkError == true) context?.getString(R.string.errorInternet) else context?.getString(R.string.errorApiToken),
                        Toast.LENGTH_SHORT).show()
                    if (!roomViewModel.isDbEmpty())
                        roomViewModel.getAllNewsObservers().observe(viewLifecycleOwner, Observer {
                            adapter = NewsRecyclerViewAdapter(requireContext(), it, this)
                            recyclerView.adapter = adapter
                        })
                    else {
                        recyclerView.visibility = View.GONE
                        noDataRelativeLayout.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.itemFavoriteBtn -> {
                val tag = v.tag as NewsEntity
                if (tag.isFavorite == 0) roomViewModel.updateNews(tag.copy(isFavorite = 1)) else roomViewModel.updateNews(tag.copy(isFavorite = 0))
            }
            R.id.itemShareBtn -> {
                val tag = v.tag as String
                ShareCompat.IntentBuilder(requireContext()).setType("text/plain")
                    .setChooserTitle(context?.getString(R.string.shareString)).setText(tag)
                    .startChooser()
            }
        }
    }

    override fun onRefresh() {
        Handler().postDelayed(Runnable {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frgView, HomeFragment())?.commit()
            swipeRefreshLayout.isRefreshing = false
        }, 3000)
    }
}