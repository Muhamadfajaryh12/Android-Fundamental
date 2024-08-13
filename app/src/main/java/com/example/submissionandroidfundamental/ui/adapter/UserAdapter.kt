package com.example.submissionandroidfundamental.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionandroidfundamental.data.response.ItemsItem
import com.example.submissionandroidfundamental.databinding.ItemActivityBinding
import com.example.submissionandroidfundamental.ui.activity.DetailActivity

class UserAdapter (private val activity: Activity):ListAdapter<ItemsItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener{
            val dataIntent = Intent(holder.itemView.context,DetailActivity::class.java)
            dataIntent.putExtra(DetailActivity.EXTRA_USERNAME,user.login)
            activity.startActivity(dataIntent)
        }
    }

    class MyViewHolder(val binding: ItemActivityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
         binding.apply {
             Glide.with(itemView.context)
                 .load(user.avatarUrl)
                 .into(binding.TvItemPhoto)
             binding.TvItemUsername.text = user.login
         }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}