package com.example.careersailorapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.careersailorapp.databinding.MentorItemBinding
import com.example.careersailorapp.model.Mentor

class MentorAdapter(private val items: List<Mentor>, private val onItemClick: ( Mentor) -> Unit):
    RecyclerView.Adapter<MentorAdapter.ViewHolder>(){
        inner class ViewHolder(val binding: MentorItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MentorItemBinding.inflate(LayoutInflater.from(parent.context), parent,  false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mentor = items[position]
        with(holder.binding){
            txtMentorName.text = mentor.name
            txtBio.text = mentor.bio
            mentorImage.load(mentor.image_url){
                transformations(
                    RoundedCornersTransformation(166F)
                )
                crossfade(true)
                crossfade(200)
                build()
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick(mentor)
        }
    }
}