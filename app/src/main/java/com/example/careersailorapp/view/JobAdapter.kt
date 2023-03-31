package com.example.careersailorapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.careersailorapp.databinding.JobItemBinding
import com.example.careersailorapp.model.Job

class JobAdapter(private val items: List<Job>, private val onItemClick: (Job) -> Unit):
    RecyclerView.Adapter<JobAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: JobItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            JobItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = items[position]

        with(holder.binding){
            txtJobTitle.text = job.name?.capitalize()
            txtJobDescription.text = job.description
        }

        holder.itemView.setOnClickListener {
            onItemClick(job)
        }
    }
}