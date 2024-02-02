package com.example.blogapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapplication.data.ApiResponse
import com.example.blogapplication.data.Blogs
import com.example.blogapplication.databinding.ItemBlogBinding

class BlogListingAdapter(
    private val dataList: List<Blogs>,
    private val listener: UpdateAndDelete,
) : RecyclerView.Adapter<BlogListingAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemBlogBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBlogBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.binding.apply {
            textBlogName.text = data.blogName
            textBlogDescription.text = data.blogDescription
            imageDelete.setOnClickListener {
                listener.deleteBlog(data.blogId)
            }
            imageEdit.setOnClickListener {
                listener.updateBlog(data)
            }
        }


    }


    override fun getItemCount(): Int = dataList.size
}

interface UpdateAndDelete {
    fun deleteBlog(id: Int)
    fun updateBlog(data:Blogs)
}