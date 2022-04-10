package com.twenty2byte.simplenews.view.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.twenty2byte.simplenews.R
import com.twenty2byte.simplenews.base.RoomViewModelFactory
import com.twenty2byte.simplenews.repository.RoomRepository
import com.twenty2byte.simplenews.source.local.NewsEntity
import com.twenty2byte.simplenews.view.adapter.NewsRecyclerViewAdapter
import com.twenty2byte.simplenews.vm.NewsViewModel
import com.twenty2byte.simplenews.vm.RoomViewModel

class FavoritesFragment : Fragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var oopsNoFavoritesYet: RelativeLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: NewsRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var roomViewModel: RoomViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val roomFactory = RoomViewModelFactory(RoomRepository(context))
        roomViewModel = ViewModelProvider(this, roomFactory)[RoomViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = layoutInflater.inflate(R.layout.fragment_favorites, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewFav)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        oopsNoFavoritesYet = view.findViewById(R.id.noDataRelFav)
        swipeRefreshLayout = view.findViewById(R.id.swipe_container_favorite)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.home_color, R.color.favorite_color,
            R.color.teal_700)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roomViewModel.getAllFavoriteNewsObservers().observe(viewLifecycleOwner, Observer {
            adapter = NewsRecyclerViewAdapter(requireContext(), it, this)
            recyclerView.adapter = adapter
            if (it.size == 0) {
                recyclerView.visibility = View.GONE
                oopsNoFavoritesYet.visibility = View.VISIBLE
            }
        })

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.itemFavoriteBtn -> {
                val tag = v.tag as NewsEntity
                if (tag.isFavorite == 1) roomViewModel.updateNews(tag.copy(isFavorite = 0))
                onRefresh()
            }
            R.id.itemShareBtn -> {
                val tag = v.tag as String
                ShareCompat.IntentBuilder(requireContext()).setType("text/plain")
                    .setChooserTitle(context?.getString(R.string.shareString))
                    .setText(tag)
                    .startChooser()
            }
            R.id.itemNewsClick -> {
                val tag = v.tag as String
                val webView = WebViewFragment.getUrl(tag)
                onDestroy()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.setCustomAnimations(R.anim.blink, 0)?.replace(R.id.frgView, webView)
                    ?.addToBackStack("BackFromWebFav")?.commit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onRefresh() {
        Handler().postDelayed(Runnable {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frgView, FavoritesFragment())?.commit()
            swipeRefreshLayout.isRefreshing = false
        }, 500)
    }
}
