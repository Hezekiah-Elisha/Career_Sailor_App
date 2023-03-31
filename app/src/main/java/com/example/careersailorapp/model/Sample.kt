package com.example.careersailorapp.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Sample {
}

fun main() {
    val db = FirebaseFirestore.getInstance()
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val articlesRef = db.collection("articles")

    articlesRef.get().addOnSuccessListener { querySnapshot ->
        val articles = mutableListOf<Article>()
        for (document in querySnapshot){
            val json = document.data.toString()
            val article = moshi.adapter(Article::class.java).fromJson(json)
            article?.let { articles?.add(it) }
            println(article)
        }
    }.addOnFailureListener {exception->
//        Log.d("Error", "getArticles: $exception")
        println(exception)
    }
}
//
//collectionRef.get()
//.addOnSuccessListener { querySnapshot ->
//    if (!querySnapshot.isEmpty){
////                    for (data in it.documents){
//////                        val json = data.data.toJson()
////                        val article:Article? = data.toObject(Article::class.java)
////
////                        if (article != null){
////                            articleList.add(article)
////                            Log.d("my_info", ": $article")
////                        }
////                    }
//        val type =
//            for(document in querySnapshot.documents){
////                        val article: Article ?= document.toObject(Article::class.java)
////
////                        val jsonAdapter = moshi.adapter(Map::class.java)
////                        val json = jsonAdapter.toJson(document.data)
////
////                        val article = moshi.adapter(Article::class.java).fromJson(json)
//
//                val
//
//                article?= let{
//
//                }
//
//            }
//
//
//    }
//}.addOnFailureListener {
//    Log.d("my_info", ": $it")
//
//}
//return articleList!!
