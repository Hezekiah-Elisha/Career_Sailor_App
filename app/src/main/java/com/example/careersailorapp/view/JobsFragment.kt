package com.example.careersailorapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.careersailorapp.R
import com.example.careersailorapp.databinding.FragmentJobsBinding
import com.example.careersailorapp.model.Article
import com.example.careersailorapp.model.Job
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class JobsFragment : Fragment() {
    private var _binding: FragmentJobsBinding ?= null
    private val binding get() = _binding!!

    private val onItemClick: (Job) -> Unit = { Job ->
        Snackbar.make(binding.root, "Selected Article: ${Job.name}", Snackbar.LENGTH_LONG).show()

        val bundle = bundleOf("the_link" to Job.link)

//        binding.root.findNavController().navigate(R.id.action_articlesFragment_to_oneArticleFragment, bundle, options)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_jobs, container, false)
        _binding = FragmentJobsBinding.inflate(inflater, container, false)
        val view = binding.root


        //        val articleList = ArrayList<Article>()
        val db = FirebaseFirestore.getInstance()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        var jobList = mutableListOf<Job>()

        binding.progressBar.visibility = View.VISIBLE

        db.collection("jobs").get().addOnSuccessListener { documents->
            for (document in documents) {
                Log.d("my_info", "${document.id} => ${document.data}")

                val myMoshi = Moshi.Builder().build() // create a new Moshi instance
                val jsonAdapter = myMoshi.adapter(Map::class.java) // create a new JSON adapter for Map objects
                val json = jsonAdapter.toJson(document.data)
                val job = moshi.adapter(Job::class.java).fromJson(json)
                if (job != null) {
                    jobList.add(job)
                    Log.d("my_job", "getArticles: $job,$jobList")
                    binding.rvJobs.adapter = JobAdapter(jobList, onItemClick)
                    binding.progressBar.visibility = View.GONE
                } else {
                    Log.d("this", "onCreateView: job")
                }
            }
            Log.d("my_info", "get second mwisho Articles: $jobList")
        }

        return view
    }
}