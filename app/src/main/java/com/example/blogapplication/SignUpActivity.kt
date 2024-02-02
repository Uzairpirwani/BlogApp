package com.example.blogapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.blogapplication.data.ApiRequest
import com.example.blogapplication.data.ApiResponse
import com.example.blogapplication.databinding.ActivitySignUpBinding
import com.example.blogapplication.network.IRequestContact
import com.example.blogapplication.network.NetworkClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity(), Callback<ApiResponse> {

    private lateinit var binding: ActivitySignUpBinding
    private val retrofitClient = NetworkClient.getNetworkClient()
    private val requestContract: IRequestContact =
        retrofitClient.create(IRequestContact::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            btnSignUp.setOnClickListener {
                if (isValidInput(
                        editTextUsername.text.toString(),
                        editTextPassword.text.toString(),
                        editTextEmail.text.toString()
                    )
                ) {
                    val data = ApiRequest(
                        action = "REGISTER_USER",
                        userName = editTextUsername.text.toString(),
                        password = editTextPassword.text.toString(),
                        email = editTextEmail.text.toString()
                    )
                    val response = requestContract.makeApiCall(data)
                    response.enqueue(this@SignUpActivity)
                } else {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Invalid input. Please check your entries.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun isValidInput(username: String, password: String, email: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty() && isEmailValid(email)
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = Patterns.EMAIL_ADDRESS
        return emailPattern.matcher(email).matches()
    }

    override fun onResponse(
        call: Call<ApiResponse>,
        response: Response<ApiResponse>
    ) {
        Log.d("BLOG_RESPONSE", response.body()?.message.toString())
        Log.d("BLOG_RESPONSE", response.body()?.responseCode.toString())

        if (response.body()?.responseCode == 0) {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
        Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
    }
}