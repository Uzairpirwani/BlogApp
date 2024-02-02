package com.example.blogapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blogapplication.adapter.BlogListingAdapter
import com.example.blogapplication.adapter.UpdateAndDelete
import com.example.blogapplication.data.ApiRequest
import com.example.blogapplication.data.ApiResponse
import com.example.blogapplication.data.Blogs
import com.example.blogapplication.databinding.ActivityDashBoardBinding
import com.example.blogapplication.network.IRequestContact
import com.example.blogapplication.network.NetworkClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashBoardActivity : AppCompatActivity(), Callback<ApiResponse> {

    private lateinit var binding: ActivityDashBoardBinding
    private val retrofitClient = NetworkClient.getNetworkClient()
    private val requestContract: IRequestContact =
        retrofitClient.create(IRequestContact::class.java)
    private lateinit var blogAdapter: BlogListingAdapter
    private var blogs = arrayListOf<Blogs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnCreateBlog.setOnClickListener {
                val intent = Intent(this@DashBoardActivity, CreateBlogActivity::class.java)
                startActivity(intent)
            }
            search.doOnTextChanged { text, _, _, _ ->
                val searchQuery = text?.toString()?.trim()

                val allBlog: List<Blogs> = blogs
                val filteredBlogs = if (!searchQuery.isNullOrBlank()) {
                    allBlog.filter { it.blogName.contains(searchQuery, ignoreCase = true) }
                } else {
                    allBlog
                }
                setBlogRecyclerView(filteredBlogs)
            }
        }

        getAllBlogs()
    }

    override fun onResume() {
        super.onResume()
        getAllBlogs()
    }

    private fun getAllBlogs() {
        val data = ApiRequest(
            action = "GET_ALL_BLOGS",
        )
        val response = requestContract.makeApiCall(data)
        response.enqueue(this@DashBoardActivity)
    }

    private fun setBlogRecyclerView(dataList: List<Blogs>?) {
        blogAdapter = BlogListingAdapter(dataList ?: arrayListOf(), object : UpdateAndDelete {


            override fun deleteBlog(id: Int) {
                val data = ApiRequest(
                    action = "DELETE_BLOG",
                    blogId = id
                )
                val response = requestContract.makeApiCall(data)
                response.enqueue(this@DashBoardActivity)
            }

            override fun updateBlog(data: Blogs) {
                val intent = Intent(this@DashBoardActivity, CreateBlogActivity::class.java)
                intent.putExtra("Blogs", data)
                startActivity(intent)
            }


        })
        binding.recyclerView.adapter = blogAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
        Log.d("BLOG_RESPONSE", response.body()?.message.toString())
        Log.d("BLOG_RESPONSE", response.body()?.responseCode.toString())
        if (response.body()?.message == "Blog Deleted Successfully") {
            getAllBlogs()
        }

        if (response.body()?.message == "Blog Updated Successfully") {

        } else {
            if (response.body()?.responseCode == 0) {
                blogs = response.body()?.blogs ?: arrayListOf()
                setBlogRecyclerView(response.body()?.blogs)
            } else {
                Toast.makeText(this, response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
        Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
    }
}
