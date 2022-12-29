package com.example.hafidh.uashafidhsyahputra

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hafidh.uashafidhsyahputra.Adapter.ListNewsAdapter
import com.example.hafidh.uashafidhsyahputra.Common.Common
import com.example.hafidh.uashafidhsyahputra.Interface.NewsService
import com.example.hafidh.uashafidhsyahputra.Model.News
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_list_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// list news untuk menampilkan berita
class ListNewsActivity : AppCompatActivity() {

    var source = ""
    var webHotUrl: String? = ""

    lateinit var dialog: AlertDialog
    lateinit var mService: NewsService
    lateinit var adapter: ListNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_news)

        /*Init View*/
        mService = Common.newsService

        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()

        swipe_to_refresh.setOnRefreshListener {
            loadNews(source, true)
        }

        diagonallayout.setOnClickListener {
            val detail = Intent(baseContext, NewsDetailsActivity::class.java)
            detail.putExtra("webURL", webHotUrl)
            startActivity(detail)
        }

        list_news.setHasFixedSize(true)
        list_news.layoutManager = LinearLayoutManager(this)

        if (intent != null) {
            source = intent.getStringExtra("source")
            if (!source.isEmpty()) {
                loadNews(source, false)
            }
        }
    }

    private fun loadNews(sources: String?, isRefreshed: Boolean) {
        if (!isRefreshed) {
            dialog.show()

            mService.getNewsFromSource(Common.getNewsAPI(sources!!)).enqueue(object :
                Callback<News> {
                override fun onFailure(call: Call<News>, t: Throwable) {

                }

                override fun onResponse(call: Call<News>, response: Response<News>) {
                    dialog.dismiss()

                    // mengambil artikel terbaru
                    Picasso.get().load(response.body()!!.articles!![0].urlToImage).into(top_image)

                    top_title.text = response.body()!!.articles!![0].title
                    top_author.text = response.body()!!.articles!![0].author

                    webHotUrl = response.body()!!.articles!![0].url

                    // fungsi untuk mengambil data dari API
                    val removeFirstItem = response.body()!!.articles

                    // karena kita sudah mengambil data pertama, maka kita hapus data pertama
                    removeFirstItem!!.removeAt(0)

                    adapter = ListNewsAdapter(baseContext, removeFirstItem)
                    adapter.notifyDataSetChanged()
                    list_news.adapter = adapter
                }

            })
        } else {
            swipe_to_refresh.isRefreshing = true
            mService.getNewsFromSource(Common.getNewsAPI(sources!!)).enqueue(object :
                Callback<News> {
                override fun onFailure(call: Call<News>, t: Throwable) {

                }

                override fun onResponse(call: Call<News>, response: Response<News>) {
                    swipe_to_refresh.isRefreshing = false

                    Picasso.get().load(response.body()!!.articles!![0].urlToImage).into(top_image)

                    top_title.text = response.body()!!.articles!![0].title
                    top_author.text = response.body()!!.articles!![0].author

                    webHotUrl = response.body()!!.articles!![0].url

                    val removeFirstItem = response.body()!!.articles

                    removeFirstItem!!.removeAt(0)

                    adapter = ListNewsAdapter(baseContext, removeFirstItem)
                    adapter.notifyDataSetChanged()
                    list_news.adapter = adapter
                }

            })
        }
    }
}
