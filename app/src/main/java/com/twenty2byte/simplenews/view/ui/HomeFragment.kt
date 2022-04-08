package com.twenty2byte.simplenews.view.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.twenty2byte.simplenews.R
import com.twenty2byte.simplenews.model.Article
import com.twenty2byte.simplenews.model.Source
import com.twenty2byte.simplenews.source.local.NewsEntity
import com.twenty2byte.simplenews.view.adapter.HomeRecyclerViewAdapter

class HomeFragment : Fragment(), View.OnClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: HomeRecyclerViewAdapter


    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = layoutInflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val test = mutableListOf<NewsEntity?>(NewsEntity
            (0,"nicenice0","who","whohjgjhgjhgjhgj","dfgdfgdfgdfg","who","who","nudfgdfgdfgll", null, 1, "ru"),
        (NewsEntity(1,"nicenice0","who","whohjgjhgjhgjhgj","dfgdfgdfgdfg","who","who","nudfgdfgdfgll", null, 0, "ru")))

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = HomeRecyclerViewAdapter(requireContext(), test,this)
        recyclerView.adapter = adapter
    }

    override fun onClick(v: View?) {



//        crash test
//        Log.e("log e", "check e LOG")
//        var a = "123ee"
//        Toast.makeText(requireContext(), a.toString().toInt(), Toast.LENGTH_SHORT).show()
    }
}