package com.pmsk.pemasokita.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val database = FirebaseDatabase.getInstance().reference.child("users")
                    userId?.let {
                        database.child(it).child("email").setValue(email)
                    }
                    onSuccess.invoke()
                } else {
                    onError.invoke("Terjadi kesalahan, coba lagi ya!")
                }
            }
            .addOnFailureListener { exception ->
                onError.invoke(exception.localizedMessage ?: "Terjadi kesalahan, coba lagi ya!")
            }
    }
}