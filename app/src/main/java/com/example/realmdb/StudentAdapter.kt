package com.example.realmdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.realmdb.databinding.ItemStudentBinding

class StudentAdapter(val students : List<StudentRealm>) : RecyclerView.Adapter<StudentAdapter.StudentVH>() {

    class StudentVH(val binding : ItemStudentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : StudentRealm){
            binding.apply {
                data = item
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentVH {
        return StudentVH(ItemStudentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: StudentVH, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount(): Int = students.size

}
