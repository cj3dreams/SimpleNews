package com.twenty2byte.simplenews.view.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.*
import com.twenty2byte.simplenews.R
import com.twenty2byte.simplenews.view.adapter.LocaleArrayAdapter
import java.util.*
import android.content.Intent
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.twenty2byte.simplenews.base.NewsViewModelFactory
import com.twenty2byte.simplenews.base.RoomViewModelFactory
import com.twenty2byte.simplenews.repository.NewsRepository
import com.twenty2byte.simplenews.repository.RoomRepository
import com.twenty2byte.simplenews.source.remote.RemoteDataSource
import com.twenty2byte.simplenews.source.remote.Resource
import com.twenty2byte.simplenews.source.remote.RestApiRequests
import com.twenty2byte.simplenews.vm.NewsViewModel
import com.twenty2byte.simplenews.vm.RoomViewModel

class OthersFragment : Fragment() {
    private lateinit var apiKeyEditText: AutoCompleteTextView
    private lateinit var logoImg: ImageView
    private lateinit var logoTx: TextView
    private lateinit var spinner: Spinner
    private lateinit var isCorrectApiImg: ImageView
    private lateinit var roomViewModel: RoomViewModel
    private lateinit var viewModel: NewsViewModel
    private val factory = NewsViewModelFactory(NewsRepository(RemoteDataSource().buildApi(RestApiRequests::class.java)))

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val roomFactory = RoomViewModelFactory(RoomRepository(context))
        roomViewModel = ViewModelProvider(this, roomFactory)[RoomViewModel::class.java]
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = layoutInflater.inflate(R.layout.fragment_others, container, false)

        logoImg = view.findViewById(R.id.newsInfoAppLogo)
        logoTx = view.findViewById(R.id.newsInfoTx)
        spinner = view.findViewById(R.id.spinnerOfLanguages)
        apiKeyEditText = view.findViewById(R.id.apiKeyEditText)
        isCorrectApiImg = view.findViewById(R.id.isApiCorrectImg)
        isCorrectApiImg.setOnClickListener{
            Toast.makeText(requireContext(),context?.getString(R.string.apiKeyInfoMessage),
                Toast.LENGTH_SHORT).show()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayTexts = arrayOf("Simple " + context?.getString(R.string.news),
            context?.getString(R.string.cj3dreams), context?.getString(R.string.everyDayNew),
            context?.getString(R.string.ads),context?.getString(R.string.telegram),
            context?.getString(R.string.cj3dimensional))
        logoTx.post(object : Runnable {
            var i = 0
            override fun run() {
                logoTx.text = arrayTexts[i]
                i++
                if (i == arrayTexts.size) i = 0
                logoTx.postDelayed(this, 5100)
            }
        })

        val anim = AlphaAnimation(0.0f, 1.0f)
        with(anim){
            duration = 2500
            startOffset = 50
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }
        logoTx.startAnimation(anim)
        logoImg.startAnimation(anim)

        val getLang = Locale.getDefault().toString()
        val lang = if (getLang == "en" || getLang == "en_Us") "us" else if(getLang == "ru_RU") "ru" else getLang

        val spinnerList = mutableListOf("us","ru")
        val adapter = LocaleArrayAdapter(requireContext(), spinnerList)
        spinner.adapter = adapter
        spinner.setSelection(spinnerList.indexOf(lang))
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when (position) {
                    0 -> if (lang != "us") changeLang("us")
                    1 -> if (lang != "ru") changeLang("ru")
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        val sharedPreferencesApiKey = context?.getSharedPreferences("ApiKey", Context.MODE_PRIVATE)
        apiKeyEditText.setText(sharedPreferencesApiKey?.getString("ApiKey","").toString())

        apiKeyEditText.addTextChangedListener {
            viewModel.getNews(lang, apiKeyEditText.text.toString())
            viewModel.newsResponse.observe(viewLifecycleOwner, {
               when(it){
                    is Resource.Success -> {
                        isCorrectApiImg.setImageResource(R.drawable.ic_ok)
                        sharedPreferencesApiKey?.edit()?.putString("ApiKey",apiKeyEditText.text.toString())?.apply()
                    }
                    is Resource.Failure -> if (it.isNetworkError == false) isCorrectApiImg.setImageResource(R.drawable.ic_error_api)
                }
            })
        }
    }

    private fun changeLang(lang: String) {
        val activity = activity as MainActivity
        val config = resources.configuration
        val locale = Locale(lang)
        Locale.setDefault(locale)
        config.setLocale(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            activity.createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)

        val sharedPreferences = context?.getSharedPreferences("Language", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putString("Language", lang)?.apply()

        roomViewModel.deleteAll()
        context?.cacheDir?.deleteRecursively()

        activity.finish()
        activity.startActivity(Intent(context, MainActivity::class.java))
        activity.finishAffinity()
    }
}