package com.example.careersailorapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.careersailorapp.model.Article
import com.example.careersailorapp.model.ArticleRepository

class ArticleViewModel(app: Application): AndroidViewModel(app) {

    val articleRepository = ArticleRepository()


}