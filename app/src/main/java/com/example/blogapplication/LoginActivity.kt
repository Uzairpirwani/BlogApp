package com.example.blogapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.blogapplication.data.ApiRequest
import com.example.blogapplication.data.ApiResponse
import com.example.blogapplication.databinding.ActivityLoginBinding
import com.example.blogapplication.network.IRequestContact
import com.example.blogapplication.network.NetworkClient
import com.example.blogapplication.sharedPreference.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), Callback<ApiResponse> {

    private lateinit var binding: ActivityLoginBinding
    private val retrofitClient = NetworkClient.getNetworkClient()
    private val requestContract: IRequestContact =
        retrofitClient.create(IRequestContact::class.java)
    private lateinit var appPreferences: AppPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appPreferences = AppPreferences(this@LoginActivity)

        binding.apply {
            btnLogin.setOnClickListener {
                val enteredUsername = editTextUsername.text.toString()
                val enteredPassword = editTextPassword.text.toString()

                if (enteredUsername.isNotEmpty() && enteredPassword.isNotEmpty()) {
                    val data = ApiRequest(
                        action = "LOGIN_USER",
                        email = enteredUsername,
                        password = enteredPassword
                    )
                    val response = requestContract.makeApiCall(data)
                    response.enqueue(this@LoginActivity)
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter both username and password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            signup.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        }

    }

    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
        Log.d("BLOG_RESPONSE",response.body()?.message.toString())
        Log.d("BLOG_RESPONSE",response.body()?.responseCode.toString())
        if (response.body()?.responseCode == 0) {
            appPreferences.isLoggedIn = response.body()?.userId ?: 0
            val intent = Intent(this@LoginActivity, DashBoardActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, response.body()?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
        Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
    }
}