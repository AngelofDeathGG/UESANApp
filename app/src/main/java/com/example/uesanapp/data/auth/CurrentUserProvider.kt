package com.example.uesanapp.data.auth

import android.content.Context
import android.provider.Settings
import com.google.firebase.auth.FirebaseAuth

class CurrentUserProvider(private val context: Context) {

    fun getUserId(): String {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) return firebaseUser.uid
        return deviceId
    }

    fun isAuthenticated(): Boolean = FirebaseAuth.getInstance().currentUser != null

    private val deviceId: String by lazy {
        Settings.Secure.getString(
            context.applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "anonymous"
    }
}
