package com.example.careersailorapp.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.careersailorapp.R
import com.example.careersailorapp.databinding.ActivityProfileBinding
import com.example.careersailorapp.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        binding.fabEdit.setOnClickListener {
            var intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        val docRef = db.collection("userDetails").document(auth.currentUser?.uid.toString())

        docRef.get().addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject(User::class.java)
            Log.d("userDetails", "onCreate: $user")
//            val json = documentSnapshot.data

            binding.txtUsersName.text = user?.name
            binding.txtExp.text = user?.experience.toString()
            binding.txtMen.text = user?.mentor.toString()
            binding.imageView.load(user?.profile_picture){
                transformations(
                    RoundedCornersTransformation(180F, 180F, 180F, 180F)
                )
                crossfade(true)
                crossfade(600)
                build()
            }
            binding.txtSki.text = user?.skill
            binding.txtMen.text = user?.interest.toString()
        }

    }

}