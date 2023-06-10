package com.pmsk.pemasokita.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.pmsk.pemasokita.data.Users

class RegisterViewModel : ViewModel() {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    fun registerUser(
        username: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (username.isEmpty()) {
            onError.invoke("Nama lengkap wajib diisi")
            return
        }
        if (username.length < 5) {
            onError.invoke("Nama harus lebih dari 5 karakter")
            return
        }
        if (email.isEmpty()) {
            onError.invoke("Email wajib diisi")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onError.invoke("Email tidak valid")
            return
        }
        if (password.isEmpty()) {
            onError.invoke("Password wajib diisi")
            return
        }
        if (password.length < 8) {
            onError.invoke("Password harus lebih dari 8")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val databaseRef = database.reference.child("users").child(userId ?: "")
                    val users = Users(username, email, userId ?: "")
                    databaseRef.setValue(users)
                        .addOnCompleteListener { innerTask ->
                            if (innerTask.isSuccessful) {
                                onSuccess.invoke()
                            } else {
                                onError.invoke("Terjadi kesalahan, coba lagi ya!")
                            }
                        }
                } else {
                    onError.invoke("Terjadi kesalahan, coba lagi ya!")
                }
            }
    }
}
