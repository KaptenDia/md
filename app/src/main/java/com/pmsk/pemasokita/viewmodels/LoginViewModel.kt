package com.pmsk.pemasokita.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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

    fun signInWithGoogle(idToken: String?, displayName: String?, email: String?,
                         onSuccess: () -> Unit, onError: (String) -> Unit) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid
                    val database = FirebaseDatabase.getInstance().reference.child("users")
                    userId?.let {
                        database.child(it).child("email").setValue(email)
                        database.child(it).child("name").setValue(displayName)
                    }
                    onSuccess.invoke()
                } else {
                    onError.invoke("Failed to sign in with Google.")
                }
            }
            .addOnFailureListener { exception ->
                onError.invoke(exception.localizedMessage ?: "Failed to sign in with Google.")
            }
    }
}