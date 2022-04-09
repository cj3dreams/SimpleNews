package com.twenty2byte.simplenews.view.ui

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.twenty2byte.simplenews.R
import com.twenty2byte.simplenews.base.RoomViewModelFactory
import com.twenty2byte.simplenews.base.ViewModelFactory
import com.twenty2byte.simplenews.repository.NewsRepository
import com.twenty2byte.simplenews.repository.RoomRepository
import com.twenty2byte.simplenews.source.remote.RemoteDataSource
import com.twenty2byte.simplenews.source.remote.Resource
import com.twenty2byte.simplenews.source.remote.RestApiRequests
import com.twenty2byte.simplenews.view.adapter.HomeRecyclerViewAdapter
import com.twenty2byte.simplenews.vm.NewsViewModel
import com.twenty2byte.simplenews.vm.RoomViewModel

class HomeFragment : Fragment(), View.OnClickListener {
    lateinit var adapter: HomeRecyclerViewAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var roomViewModel: RoomViewModel
    lateinit var viewModel: NewsViewModel
    private val factory = ViewModelFactory(NewsRepository(RemoteDataSource().buildApi(RestApiRequests::class.java)))

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val roomFactory = RoomViewModelFactory(RoomRepository(context))
        roomViewModel = ViewModelProvider(this, roomFactory)[RoomViewModel::class.java]
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = layoutInflater.inflate(R.layout.fragment_home, container, false)

        viewModel.getNews("us", "3d1ea4a2c49e477fafc161982d26ea57")
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.newsResponse.observe(viewLifecycleOwner, Observer { resource ->
            when (resource){
                is Resource.Success -> {
                    val newsResponse = resource.value.articles
                    roomViewModel.setNewsToDb(newsResponse)
                    roomViewModel.getAllNewsObservers().observe(viewLifecycleOwner, Observer {
                        Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                        adapter = HomeRecyclerViewAdapter(requireContext(), it, this)
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    })
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "fail", Toast.LENGTH_SHORT).show()
                }
            }
        })



    }

    override fun onClick(v: View?) {
//        crash test
//        Log.e("log e", "check e LOG")
//        var a = "123ee"
//        Toast.makeText(requireContext(), a.toString().toInt(), Toast.LENGTH_SHORT).show()
    }
}