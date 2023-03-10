package com.example.hafidh.uashafidhsyahputra.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hafidh.uashafidhsyahputra.Common.ISO8601DateParser
import com.example.hafidh.uashafidhsyahputra.Interface.ItemClickListener
import com.example.hafidh.uashafidhsyahputra.Model.Article
import com.example.hafidh.uashafidhsyahputra.NewsDetailsActivity
import com.example.hafidh.uashafidhsyahputra.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_layout.view.*
import java.text.ParseException
import java.util.*

// fungsinya untuk menampilkan data yang ada di dalam list
class ListNewsAdapter(val context: Context, val articleList: MutableList<Article>):
    RecyclerView.Adapter<ListNewsAdapter.ListNewsViewHolder>() {

    class ListNewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private lateinit var itemClickListener: ItemClickListener

        var article_title = itemView.article_title
        var article_time = itemView.article_time
        var article_image = itemView.article_image
        init {
            itemView.setOnClickListener(this)
        }

        fun setItemClickListener(itemClickListener: ItemClickListener) {
            this.itemClickListener = itemClickListener
        }

        override fun onClick(v: View?) {
            itemClickListener.onClick(v!!, adapterPosition)
        }
    }

    // fungsi untuk menampilkan layout yang akan digunakan
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNewsViewHolder {
        return ListNewsViewHolder(LayoutInflater.from(context).inflate(R.layout.news_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: ListNewsViewHolder, position: Int) {
        /*Image Load*/
        Picasso.get().load(articleList[position].urlToImage).into(holder.article_image)

        if (articleList[position].title!!.length > 65) {
            holder.article_title.text = articleList[position].title!!.substring(0, 65) + "..."
        }
        else {
            holder.article_title.text = articleList[position].title!!
        }

        if (articleList[position].publishedAt != null) {
            var date: Date? = null
            try {
                date = ISO8601DateParser.parse(articleList[position].publishedAt!!)
            } catch (ex: ParseException) {
                ex.printStackTrace()
            }
            holder.article_time.setReferenceTime(date!!.time)
        }

        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int) {
                val details = Intent(context, NewsDetailsActivity::class.java)
                details.putExtra("webURL", articleList[position].url)
                details.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(details)
            }
        })
    }
}