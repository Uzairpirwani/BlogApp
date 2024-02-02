package com.example.blogapplication.sharedPreference

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)

    var isLoggedIn: Int
        get() = sharedPreferences.getInt("IS_LOGGED_IN_KEY", 0)
        set(value){
            sharedPreferences.edit().putInt("IS_LOGGED_IN_KEY", value).apply()
        }


    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }
}

