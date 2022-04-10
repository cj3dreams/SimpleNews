package com.twenty2byte.simplenews.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.twenty2byte.simplenews.R
import com.twenty2byte.simplenews.source.local.NewsEntity
import com.twenty2byte.simplenews.view.ui.HomeFragment
import java.lang.Exception

class NewsRecyclerViewAdapter(private val context: Context, private val list: MutableList<NewsEntity?>,
    private val onClickListener: View.OnClickListener): RecyclerView.Adapter<NewsRecyclerViewAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var img:ImageView = view.findViewById(R.id.itemImg)
        var brandNameOnImageViewTx:TextView = view.findViewById(R.id.itemBrandNameImageViewTx)
        var timeTx:TextView = view.findViewById(R.id.itemTimeTx)
        var newsTitleTx:TextView = view.findViewById(R.id.itemNewsTitleTx)
        var newsDescTx:TextView = view.findViewById(R.id.itemNewsDescTx)
        var webIconChangeable:ImageView = view.findViewById(R.id.itemWebIconChangeable)
        var webIconNameTx:TextView = view.findViewById(R.id.itemWebIconNameTx)
        var favoriteImgButton:ImageView = view.findViewById(R.id.itemFavoriteBtn)
        var shareImgButton:ImageView  = view.findViewById(R.id.itemShareBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)

        return HomeViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val itemData = list[position]
        glide(context, holder.img, itemData?.urlToImage) //mainItemImageView

        if (itemData?.author != "null")
            holder.brandNameOnImageViewTx.text = " " + itemData?.author + " "
        else holder.brandNameOnImageViewTx.text =
            if (!itemData.sourceName.isNullOrEmpty())
                " " + itemData.sourceName + " "
            else " Simple " + context.getString(R.string.news) + " "

        holder.timeTx.text = " " + itemData?.publishedAt?.substring(0,10) + " "
        holder.newsTitleTx.text = itemData?.title

        holder.newsDescTx.text = if (itemData?.description.isNullOrEmpty())
            context.getString(R.string.ifDescIsEmptyOrNull) else itemData?.description

        if(itemData?.urlToImage != null)
            glideMini(context, holder.webIconChangeable, "https://" + itemData.url
                ?.substring(8)?.substringBefore("/") + "/favicon.ico") //iconItemImageView

        holder.webIconNameTx.text = itemData?.sourceName

        if(itemData?.isFavorite == 1)
            holder.favoriteImgButton.setImageResource(R.drawable.ic_favorites)

        try {
            holder.itemView.tag = position
            holder.favoriteImgButton.tag = itemData
            holder.shareImgButton.tag = context.getString(R.string.sharingMessage) + itemData?.url +
                    "\n\nSimple " + context.getString(R.string.news)
            holder.itemView.setOnClickListener(onClickListener)
            holder.favoriteImgButton.setOnClickListener(onClickListener)
            holder.shareImgButton.setOnClickListener(onClickListener)

        } catch (ex: Exception) {
            ex.message?.let {
                Log.e(HomeFragment::class.java.simpleName, it)
            }
        }
    }

    override fun getItemCount() = list.size
    //

    private fun glide(context: Context?, imageView: ImageView, urlToImage: String?) {
        context?.let {
            Glide.with(it).load(urlToImage)
                .thumbnail(
                    Glide.with(it).load(R.drawable.ic_launcher_foreground))
                .diskCacheStrategy(DiskCacheStrategy.DATA).dontAnimate().into(imageView)
        }
    }
    private fun glideMini(context: Context?, imageView: ImageView, urlToImage: String?) {
       context?.let {
            Glide.with(it).load(urlToImage).diskCacheStrategy(DiskCacheStrategy.DATA)
                .error(R.drawable.ic_web).into(imageView)
        }
    }
}