package com.yusril.storyapp.core.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yusril.storyapp.R
import com.yusril.storyapp.core.domain.model.Story
import com.yusril.storyapp.databinding.ItemStoryBinding

class StoriesAdapter: RecyclerView.Adapter<StoriesAdapter.RecyclerViewHolder>() {
    private val listStories = ArrayList<Story>()
    private var onItemClickCallback: OnItemClickCallback? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setStories(items: List<Story>) {
        listStories.clear()
        listStories.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class RecyclerViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .into(storyImage)
                storyUserName.text = story.name
                storyDate.text = story.createdAt
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(listStories[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(listStories[position])
        }
    }

    override fun getItemCount(): Int = listStories.size

    interface OnItemClickCallback {
        fun onItemClicked(story: Story)
    }
}