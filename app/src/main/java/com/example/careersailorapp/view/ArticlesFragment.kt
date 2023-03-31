package com.example.careersailorapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.example.careersailorapp.R
import com.example.careersailorapp.databinding.FragmentArticlesBinding
import com.example.careersailorapp.model.Article
import com.example.careersailorapp.model.ArticleRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class ArticlesFragment : Fragment() {
    private var _binding: FragmentArticlesBinding ?= null
    private val binding get() = _binding!!
    val options = navOptions {
        anim {
            enter = androidx.appcompat.R.anim.abc_fade_in
            exit = androidx.transition.R.anim.abc_fade_out
            popEnter = androidx.transition.R.anim.abc_fade_in
            popExit = androidx.transition.R.anim.abc_fade_out
        }
    }
//    val args: ArticlesFragmentArgs by navArgs()

    private val onItemClick: (Article) -> Unit = { Article ->
        Snackbar.make(binding.root, "Selected Article: ${Article.name}", Snackbar.LENGTH_LONG).show()

        val bundle = bundleOf("the_link" to Article.link)

        binding.root.findNavController().navigate(R.id.action_articlesFragment_to_oneArticleFragment, bundle, options)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_articles, container, false)
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        val view = binding.root

        //        val articleList = ArrayList<Article>()
        val db = FirebaseFirestore.getInstance()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        var articleList = mutableListOf<Article>()

        binding.progressBar2.visibility = View.VISIBLE

        db.collection("articles").get().addOnSuccessListener { documents->
            for (document in documents) {
                Log.d("my_info", "${document.id} => ${document.data}")

                val myMoshi = Moshi.Builder().build() // create a new Moshi instance
                val jsonAdapter = myMoshi.adapter(Map::class.java) // create a new JSON adapter for Map objects
                val json = jsonAdapter.toJson(document.data)
                val article = moshi.adapter(Article::class.java).fromJson(json)
                if (article != null) {
                    articleList.add(article)
                    Log.d("my_info", "getArticles: $article,$articleList")
                    binding.rv.adapter = ArticleAdapter(articleList, onItemClick)
                    binding.progressBar2.visibility = View.GONE
                } else {
                    articleList.add(Article(name = "Hezekiah", link = "https://example.com", description = "Something bad"))

                }
            }
            Log.d("my_info", "get second mwisho Articles: $articleList")
        }



        return(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}