package com.pmsk.pemasokita.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pmsk.pemasokita.databinding.FragmentProfileBinding
import com.pmsk.pemasokita.ui.auth.LoginScreenActivity
import com.pmsk.pemasokita.utils.SharedPrefManager


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private lateinit var auth: FirebaseAuth
    private val binding get() =_binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding?.root
    }


    override fun onViewCreated(view : View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fullNameTextView = binding?.userName
        val emailTextView = binding?.userEmail
        val progress = binding?.loading
        fullNameTextView?.text = "Loading..." // Set default value
        emailTextView?.text = "Loading..." // Set default value
        progress?.visibility = View.GONE
        auth = FirebaseAuth.getInstance()
        val user : FirebaseUser? = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val databaseReferences = FirebaseDatabase.getInstance().reference.child("users").child(userId)

            databaseReferences.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val fullName = snapshot.child("username").value.toString()
                        val email = snapshot.child("email").value.toString()

                        fullNameTextView?.text = fullName
                        emailTextView?.text = email
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database read error
                }
            })
        } else {
            fullNameTextView?.text = "Not Logged In"
            emailTextView?.text = "N/A"
        }
        binding?.btnLogout?.setOnClickListener{
            progress?.visibility = View.VISIBLE
            logoutUser()
        }
    }

    private fun logoutUser() {
        // Clear login state using SharedPrefManager
        SharedPrefManager.setLoggedIn(requireContext(), false)

        // Logout user from Firebase
        auth.signOut()

        // Redirect to LoginActivity
        val intent = Intent(this@ProfileFragment.context, LoginScreenActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }


}