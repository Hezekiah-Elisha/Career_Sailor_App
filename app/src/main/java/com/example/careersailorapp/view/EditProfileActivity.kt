package com.example.careersailorapp.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextUtils.isEmpty
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.careersailorapp.databinding.ActivityEditProfileBinding
import com.example.careersailorapp.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
//    private lateinit var imagePickerActivityResult: ActivityResultLauncher<Intent>
    lateinit var storage: FirebaseStorage
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_edit_profile)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val mydocRef = db.collection("userDetails").document(auth.currentUser?.uid.toString())
        mydocRef.get().addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject(User::class.java)
            if (user?.profile_picture != null){
                binding.profilePicture.load(user?.profile_picture.toString()){
                    transformations(
                        RoundedCornersTransformation(8F)
                    )
                    crossfade(true)
                    crossfade(600)
                    build()
                }
            }
//            binding.txtSkills.setText(user?.skill)
//            binding.txtInterests.setText(user?.interest)
            user?.experience?.let { binding.txtExperienceYears.setText(it.toString()) }
        }

        val imagePickerActivityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null){
                val imageUri: Uri?= result.data?.data

                val sd = getFileName(applicationContext, imageUri!!)

                val uploadTask = imageUri?.let { storage.reference.child("users/$sd").putFile(it) }
                val docRef = db.collection("userDetails").document(auth.currentUser?.uid.toString())

                uploadTask?.addOnFailureListener {
                    Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
                }?.addOnSuccessListener {
                    storage.reference.child("users/$sd").downloadUrl.addOnSuccessListener {


                        docRef.update("profile_picture", it.toString())
                        Snackbar.make(binding.root, "Profile picture uploaded", Snackbar.LENGTH_LONG).show()
                        binding.profilePicture.load(it.toString()){
                            transformations(
                                RoundedCornersTransformation(8F)
                            )
                            crossfade(true)
                            crossfade(600)
                            build()
                        }
                        Log.e("Firebase", "download passed")
                    }.addOnFailureListener {
                        Log.e("Firebase", "Failed in downloading")
                    }
                    Toast.makeText(this, "Upload Successful", Toast.LENGTH_SHORT).show()
                }

            }
        }


        binding.btnProfilePicture.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imagePickerActivityResult.launch(galleryIntent)
        }

        binding.btnUpdate.setOnClickListener {
            val myDocRef = db.collection("userDetails").document(auth.currentUser?.uid.toString())
            val updates = hashMapOf<String, Any>(
                "experience" to binding.txtExperienceYears.text.toString().toInt()
            )
            if (!isEmpty(binding.txtSkills.text)){
                updates["skill"] = binding.txtSkills.text.toString()
            }
            if (!isEmpty(binding.txtInterests.text)){
                updates["interest"] = binding.txtInterests.text.toString()
            }

            myDocRef.update(updates).addOnSuccessListener {
                val intent = Intent(this, ProfileActivity::class.java)
                Toast.makeText(this, "Info updated", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                Toast.makeText(this, "Not updated", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if(cursor.moveToFirst()) {

                        val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (displayNameIndex >= 0) {
                            val displayName = cursor.getString(displayNameIndex)
                            // Do something with the display name
                            return cursor.getString(displayNameIndex)

                        } else {
                            // Handle the case where the display name column was not found
                            return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
                        }

                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }
}