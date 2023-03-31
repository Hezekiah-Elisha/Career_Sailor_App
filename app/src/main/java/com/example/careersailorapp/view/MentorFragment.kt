package com.example.careersailorapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.example.careersailorapp.R
import com.example.careersailorapp.databinding.FragmentMentorBinding
import com.example.careersailorapp.model.Article
import com.example.careersailorapp.model.Mentor
import com.example.careersailorapp.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MentorFragment : Fragment() {

    private var _binding: FragmentMentorBinding ?= null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    val options = navOptions {
        anim {
            enter = androidx.appcompat.R.anim.abc_fade_in
            exit = androidx.transition.R.anim.abc_fade_out
            popEnter = androidx.transition.R.anim.abc_fade_in
            popExit = androidx.transition.R.anim.abc_fade_out
        }
    }

//    val args: MentorFragmentArgs by navArgs()


    private val onItemClick: (Mentor) -> Unit = { Mentor ->
        Snackbar.make(binding.root, "Selected Mentor is situated at: ${Mentor.location}", Snackbar.LENGTH_LONG).show()

//        val action = MentorFragmentDirections.actionMentorFragmentToOneMentorFragment(Mentor)
//        binding.root.findNavController().navigate(action)
        val bundles = bundleOf(
            "the_mentor_id" to Mentor.id,
            "the_mentor" to Mentor.name,
            "the_mentor_location" to Mentor.location,
            "the_mentor_email" to Mentor.email,
            "the_mentor_phone" to Mentor.phone,
            "the_mentor_bio" to Mentor.bio,
            "the_mentor_image" to Mentor.image_url,
            "the_mentor_field" to Mentor.field,
        )

        binding.root.findNavController().navigate(R.id.action_mentorFragment_to_oneMentorFragment, bundles, options)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_mentor, container, false)
        _binding = FragmentMentorBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()


        val db = FirebaseFirestore.getInstance()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        var mentorList = mutableListOf<Mentor>()

        binding.progressBar3.visibility = View.VISIBLE

        db.collection("userDetails").document(auth.currentUser?.uid.toString())
            .get().addOnSuccessListener { documentSnapshot->
                val user = documentSnapshot.toObject(User::class.java)
                db.collection("mentors").whereEqualTo("field", user?.interest).get().addOnSuccessListener { documents ->
                    for (document in documents) {
//                        val myMoshi = Moshi.Builder().build()
                        val jsonAdapter = moshi.adapter(Map::class.java)
                        val json = jsonAdapter.toJson(document.data)
                        val mentor = moshi.adapter(Mentor::class.java).fromJson(json)
                        if (mentor != null) {
                            mentorList.add(mentor)
                            Log.d("my_info", "getArticles: $mentor,$mentorList")
                            binding.rvMentor.adapter = MentorAdapter(mentorList, onItemClick)
                            binding.progressBar3.visibility = View.GONE
                        } else {
                            Log.d("mentor_adapter", "onCreateView: Not Working kabisa")
                        }

                    }
                }
            }

        return view

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}