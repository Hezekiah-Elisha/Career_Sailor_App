package com.example.careersailorapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.careersailorapp.databinding.ArticleItemBinding
import com.example.careersailorapp.model.Article

class ArticleAdapter(private val items: List<Article>, private val onItemClick: (Article) -> Unit):
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>(){
        inner class ViewHolder(val binding: ArticleItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ArticleItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = items[position]

        with(holder.binding){
            txtTitle.text = article.name?.capitalize()
            txtDescription.text = article.description
        }

        holder.itemView.setOnClickListener {
            onItemClick(article)
        }
    }
}