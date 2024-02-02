package com.example.blogapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.blogapplication.data.ApiRequest
import com.example.blogapplication.data.ApiResponse
import com.example.blogapplication.data.Blogs
import com.example.blogapplication.databinding.ActivityCreateBlogBinding
import com.example.blogapplication.network.IRequestContact
import com.example.blogapplication.network.NetworkClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateBlogActivity : AppCompatActivity(), Callback<ApiResponse> {
    private lateinit var binding: ActivityCreateBlogBinding
    private val retrofitClient = NetworkClient.getNetworkClient()
    private val requestContract: IRequestContact =
        retrofitClient.create(IRequestContact::class.java)
    private var blog: Blogs? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        blog = intent.getParcelableExtra("Blogs")


        binding.apply {

            if (blog != null) {
                buttonCreate.text = "Update Blog"
                editTextBlogName.setText(blog?.blogName)
                editTextBlogDescription.setText(blog?.blogDescription)
            }

            buttonCreate.setOnClickListener {
                if (blog != null) {
                    updateBlog()
                } else {
                    createBlog()
                }

            }
        }
    }

    private fun updateBlog() {
        val data = ApiRequest(
            action = "UPDATE_BLOG",
            blogId = blog?.blogId,
            blogName = binding.editTextBlogName.text.toString(),
            blogDescription = binding.editTextBlogDescription.text.toString()
        )
        val response = requestContract.makeApiCall(data)
        response.enqueue(this@CreateBlogActivity)
    }

    private fun createBlog() {
        val data = ApiRequest(
            action = "CREATE_BLOG",
            blogName = binding.editTextBlogName.text.toString(),
            blogDescription = binding.editTextBlogDescription.text.toString()
        )
        val response = requestContract.makeApiCall(data)
        response.enqueue(this@CreateBlogActivity)
    }

    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
        Log.d("BLOG_RESPONSE", response.body()?.message.toString())
        Log.d("BLOG_RESPONSE", response.body()?.responseCode.toString())
        if (response.body()?.responseCode == 0) {
            finish()
        } else {
            Toast.makeText(this, response.body()?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
        Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
    }
}