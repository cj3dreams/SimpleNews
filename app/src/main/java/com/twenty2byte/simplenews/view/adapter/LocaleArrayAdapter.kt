package com.twenty2byte.simplenews.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.twenty2byte.simplenews.R

class LocaleArrayAdapter(context: Context, list: MutableList<String>)
    :ArrayAdapter<String>(context, 0, list)  {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return init(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return init(position, convertView, parent)
    }

    private fun init(position: Int, convertView: View?, parent: ViewGroup): View{

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_spinner, parent, false)

        val flagImg: ImageView = view.findViewById(R.id.spinnerImg)
        val textView: TextView = view.findViewById(R.id.spinnerText)

        val langName = getItem(position)
        textView.text = if(langName == "us") context.getString(R.string.american) else context.getString(R.string.russian)
        if (langName == "us") flagImg.setImageResource(R.drawable.us) else flagImg.setImageResource(R.drawable.ru)

        return view
    }

}