package com.example.careersailorapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.example.careersailorapp.databinding.FragmentOneArticleBinding

class OneArticleFragment : Fragment() {
    private var _binding: FragmentOneArticleBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_one_article, container, false)
        _binding = FragmentOneArticleBinding.inflate(inflater, container, false)
        val view = binding.root
        val progressBar = binding.progressBar4


        val link = arguments?.getString("the_link")


        if (link != null) {
            binding.wvArticle.loadUrl(link)
        } else {
            binding.wvArticle.loadUrl("https://google.com")
        }
        binding.wvArticle.settings.javaScriptEnabled = true
        binding.wvArticle.settings.builtInZoomControls = true


        binding.wvArticle.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    // Hide the progress bar when the page is fully loaded
                    progressBar.visibility = View.GONE
                } else {
                    // Show the progress bar when the page is still loading
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = newProgress
                }
            }
        }



        return(view)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}