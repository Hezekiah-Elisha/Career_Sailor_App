package com.example.careersailorapp.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.careersailorapp.databinding.ActivityRegisterBinding
import com.example.careersailorapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class RegisterActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegisterBinding
    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()


        binding.btnToLogin.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }





        binding.btnRegister.setOnClickListener {
            // Authentification with Firebase
            // Initialize Firebase Auth
            binding.progressBar.visibility = View.VISIBLE
            auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(binding.txtEmailRegister.text.toString().trim() , binding.txtPasswordRegister.text.toString().trim())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        val exp = binding.txtExperienceYears.text.toString().toInt()
                        val name = binding.txtName.text.toString()
                        val email = binding.txtEmailRegister.text.toString()
                        val interests = binding.txtInterests.text.toString()
                        val skills = binding.txtSkills.text.toString()

                        addUserDetails(user?.uid.toString(), name, email, interests, skills, exp)
//                    updateUI(user)
                        var intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                    Log.w("Auth Failed", "createUserWithEmail:failure with ${binding.txtEmailRegister.text.toString()}", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                    }
                }
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun addUserDetails(uId:String, name: String, email: String ,interest:String, skill: String, experience: Int){
//        val hashMap = hashMapOf<String, Any>(
//            "user_id" to uId,
//            "interest" to interest,
//            "skill" to skill,
//            "experience" to experience,
//            "name" to name,
//            "email" to email,
//            "mentor" to 0
//        )
//
//        FirebaseFirestore.getInstance().collection("userDetails")
//            .add(hashMap)
//            .addOnSuccessListener {
//                Log.d("the_sailor", "Added document with ID ${it.id}")
//            }
//            .addOnFailureListener{ exception ->
//                Log.w("the_sailor", "Error adding document $exception" )
//
//            }
        val user = User(email=email, experience = experience, name = name, interest = interest, skill = skill)
        val collectionRef = db.collection("userDetails").document(uId)
        collectionRef.set(user).addOnSuccessListener {
            Toast.makeText(applicationContext, "Registration Successfull", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            Toast.makeText(applicationContext, "Registration Failed", Toast.LENGTH_SHORT).show()
        }



    }


}