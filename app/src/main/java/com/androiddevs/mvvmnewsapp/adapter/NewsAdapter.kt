package com.androiddevs.mvvmnewsapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.model.Article
import com.androiddevs.mvvmnewsapp.util.ImageLoader
import com.androiddevs.mvvmnewsapp.util.ImageLoaderFactory
import com.bumptech.glide.Glide

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        val article = differ.currentList[position]
//        Glide.with(holder.itemView).load(article.urlToImage).into(holder.ivArticleImage)
        ImageLoaderFactory.getInstance().loadImage(article.urlToImage, holder.ivArticleImage)
        holder.tvSource.text = article.source.name
        holder.tvTitle.text = article.title
        holder.tvDescription.text = article.description
        holder.tvPublishedAt.text = article.publishedAt
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(article) }
        }
    }

    fun setOnItemClickListener(listener: (Article) -> Unit){
        onItemClickListener = listener
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    //舊寫法
//    interface InvitationItemClickListener {
//        fun onItemClicked(invitation: com.machipopo.media17.adapter.StreamerInvitationAdapter.StreamerInvitation?)
//        fun test2()
//    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }



    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ivArticleImage: ImageView
        val tvSource: TextView
        val tvTitle: TextView
        val tvDescription: TextView
        val tvPublishedAt: TextView

        init {
            ivArticleImage = itemView.findViewById(R.id.ivArticleImage)
            tvSource = itemView.findViewById(R.id.tvSource)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvDescription = itemView.findViewById(R.id.tvDescription)
            tvPublishedAt = itemView.findViewById(R.id.tvPublishedAt)
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url  //比對unique article url, 確認item是否相同
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)
}