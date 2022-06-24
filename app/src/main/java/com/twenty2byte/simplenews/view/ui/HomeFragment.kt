package com.twenty2byte.simplenews.view.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.twenty2byte.simplenews.R
import com.twenty2byte.simplenews.repository.NewsRepository
import com.twenty2byte.simplenews.repository.RoomRepository
import com.twenty2byte.simplenews.source.local.NewsEntity
import com.twenty2byte.simplenews.source.remote.RemoteDataSource
import com.twenty2byte.simplenews.source.remote.Resource
import com.twenty2byte.simplenews.source.remote.RestApiRequests
import com.twenty2byte.simplenews.view.adapter.NewsRecyclerViewAdapter
import com.twenty2byte.simplenews.vm.NewsViewModel
import com.twenty2byte.simplenews.vm.RoomViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class HomeFragment : Fragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var noDataRelativeLayout: RelativeLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: NewsRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private val roomViewModel: RoomViewModel by viewModel()
    private val viewModel: NewsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val sharedPreferencesApiKey = context?.getSharedPreferences("ApiKey", Context.MODE_PRIVATE)
        val apiKey = sharedPreferencesApiKey?.getString("ApiKey", "").toString()
        val getLang = Locale.getDefault().toString()
        val lang = if(getLang == "ru_RU" || getLang == "ru") "ru" else "us"

        viewModel.getNews(lang, apiKey)

        val view = layoutInflater.inflate(R.layout.fragment_home, container, false)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        noDataRelativeLayout = view.findViewById(R.id.noDataRel)
        swipeRefreshLayout = view.findViewById(R.id.swipe_container_home)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.home_color,
            R.color.favorite_color, R.color.teal_700)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.newsResponse.observe(viewLifecycleOwner, { resource ->
            when (resource){
                is Resource.Success -> {
                    roomViewModel.setNewsToDb(resource.value.articles)
                    roomViewModel.getAllNewsObservers().observe(viewLifecycleOwner, {
                        adapter = NewsRecyclerViewAdapter(requireContext(), it, this)
                        recyclerView.adapter = adapter
                        if(resource.value.status == "ok" && !roomViewModel.isDbEmpty()) {
                            recyclerView.visibility = View.VISIBLE
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                        }
                    })
                }
                is Resource.Failure -> {
                    if (!roomViewModel.isDbEmpty()) {
                        Handler().postDelayed( {
                            recyclerView.visibility = View.VISIBLE
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                        }, 500)
                        roomViewModel.getAllNewsObservers().observe(viewLifecycleOwner, {
                            adapter = NewsRecyclerViewAdapter(requireContext(), it, this)
                            recyclerView.adapter = adapter
                        })
                        Toast.makeText(requireContext(), if (resource.isNetworkError == true)
                            context?.getString(R.string.showingFromCache) else context?.getString(R.string.errorApiKey)
                                + resource.errorCode.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(requireContext(), if (resource.isNetworkError == true)
                            context?.getString(R.string.errorInternet) else context?.getString(R.string.errorApiKey) + resource.errorCode.toString(),
                            Toast.LENGTH_SHORT).show()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
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
                    .setChooserTitle(context?.getString(R.string.shareString)).setText(tag).startChooser()
            }
            R.id.itemNewsClick -> {
                val tag = v.tag as String
                val webView = WebViewFragment.getUrl(tag)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.setCustomAnimations(R.anim.blink, 0)?.replace(R.id.frgView, webView)
                    ?.addToBackStack("BackFromWeb")?.commit()
            }
        }
    }

    override fun onRefresh() {
        Handler().postDelayed( {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frgView, HomeFragment())?.commit()
            swipeRefreshLayout.isRefreshing = false
        }, 3000)
    }
}