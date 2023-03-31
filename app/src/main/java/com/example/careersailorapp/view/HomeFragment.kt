package com.example.careersailorapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.careersailorapp.R
import com.example.careersailorapp.databinding.FragmentHomeBinding
import com.example.careersailorapp.model.Mentor
import com.example.careersailorapp.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.math.log

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding ?= null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        var mentorList = mutableListOf<Mentor>()

        val docRef = db.collection("userDetails").document(auth.currentUser?.uid.toString())

        docRef.get().addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject(User::class.java)
            Log.d("userDetails", "onCreate: $user")
//            val json = documentSnapshot.data

            binding.txtUserName.text = "Hi, ${user?.name}"
            binding.userImage.load(user?.profile_picture){
                transformations(
                    RoundedCornersTransformation(180F, 180F, 180F, 180F)
                )
                crossfade(true)
                crossfade(600)
                build()
            }
            binding.txtFieldHome.text = user?.interest.toString().capitalize()

            db.collection("mentors").whereEqualTo("id", user?.mentor).get().addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("here", "onCreateView: ${document.data}")
                    binding.imgMent.load(document.data["image_url"]){
                        transformations(
                            RoundedCornersTransformation(8F)
                        )
                        crossfade(true)
                        crossfade(200)
                        build()
                    }
                    binding.mentLocation.text = document.data["location"] as CharSequence?

                    binding.mentNameTxt.text = document.data["name"] as CharSequence?


                }

            }
        }

        binding.btnProfile.setOnClickListener {
            var intent = Intent(activity, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            var intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.cardMentor.setOnClickListener {
            val mydocRef = db.collection("userDetails").document(auth.currentUser?.uid.toString())
            mydocRef.get().addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)

                db.collection("mentors").whereEqualTo("id", user?.mentor).get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            Log.d("here", "onCreateView: ${document.data}")
                            val phone = document.data["phone"]
                            val name = user?.name
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data =
                                Uri.parse("https://api.whatsapp.com/send?phone=+$phone&text=Hi%20$name")

                            // Verify that WhatsApp is installed on the device
                            if (context?.let { it1 -> intent.resolveActivity(it1.packageManager) } == null) {
                                // WhatsApp is installed, launch the Intent
                                startActivity(intent)
                            } else {
                                // WhatsApp is not installed, show an error message
                                Toast.makeText(
                                    context,
                                    "WhatsApp is not installed on this device",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }


//            val phone = document.data["phone"] as String
//            val name = user?.name
//                        val intent = Intent(Intent.ACTION_VIEW)
//                        intent.data = Uri.parse("https://api.whatsapp.com/send?phone=+254703723423")

                // Verify that WhatsApp is installed on the device
//                        if (context?.let { it1 -> intent.resolveActivity(it1.packageManager) } != null) {
                // WhatsApp is installed, launch the Intent
//                            startActivity(intent)
//                        } else {
                // WhatsApp is not installed, show an error message
//                            Toast.makeText(context, "WhatsApp is not installed on this device", Toast.LENGTH_SHORT).show()
//                        }
                Toast.makeText(activity, "yeey", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }
}