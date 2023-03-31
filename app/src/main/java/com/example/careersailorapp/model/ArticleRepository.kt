package com.example.careersailorapp.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class ArticleRepository {
    val db = FirebaseFirestore.getInstance()
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun getArticles(): List<Article> {
//        val articleList = ArrayList<Article>()
        var articleList = mutableListOf<Article>()

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

                } else {
                    articleList.add(Article(name = "Hezekiah", link = "https://example.com", description = "Something bad"))

                }
            }
            Log.d("my_info", "get second mwisho Articles: $articleList")
        }

        Log.d("my_info", "get mwisho Articles: $articleList")

        return articleList

    }
}