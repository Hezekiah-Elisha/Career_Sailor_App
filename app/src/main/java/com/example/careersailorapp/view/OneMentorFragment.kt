package com.example.careersailorapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.careersailorapp.R
import com.example.careersailorapp.databinding.FragmentOneMentorBinding
import com.example.careersailorapp.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OneMentorFragment : Fragment() {
    private var _binding: FragmentOneMentorBinding ?= null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_one_mentor, container, false)
        _binding = FragmentOneMentorBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val name = arguments?.getString("the_mentor")
        val location = arguments?.getString("the_mentor_location")
        val email = arguments?.getString("the_mentor_email")
        val phone = arguments?.getString("the_mentor_phone")
        val bio = arguments?.getString("the_mentor_bio")
        val field = arguments?.getString("the_mentor_field")
        val id = arguments?.getString("the_mentor_id")?.toInt()
        val image = arguments?.getString("the_mentor_image")

        binding.txtOneMentorName.text = name
        binding.txtMentorBio.text = bio
        binding.txtMentorLocation.text = location
//        binding.txtMentorEmail.text = email
        binding.txtMentorField.text = field
        binding.menImage.load(image){
            transformations(
                RoundedCornersTransformation(166F)
            )
            crossfade(true)
            crossfade(200)
            build()
        }

        binding.fabMentor.setOnClickListener {

            val docRef = db.collection("userDetails").document(auth.currentUser?.uid.toString())

            docRef.get().addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)

                db.collection("userDetails").document(auth.currentUser?.uid.toString())
                    .update("mentor", id)
                Snackbar.make(view, "You selected Mentor: $name", Snackbar.LENGTH_LONG).show()
            }.addOnFailureListener { exception->
                Log.w("user_mentor_info", "Error getting documents: ", exception)

            }
        }
        return view

    }
}