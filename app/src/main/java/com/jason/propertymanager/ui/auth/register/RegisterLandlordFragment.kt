package com.jason.propertymanager.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.jason.propertymanager.R
import com.jason.propertymanager.data.model.RegisterBody
import com.jason.propertymanager.databinding.FragmentRegisterLanlordBinding
import com.jason.propertymanager.other.register_success
import com.jason.propertymanager.other.tag_d
import com.jason.propertymanager.ui.auth.UserViewModel
import com.jason.propertymanager.ui.auth.login.LoginActivity


class RegisterLandlordFragment : Fragment() {
    private var _binding: FragmentRegisterLanlordBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_lanlord, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterLanlordBinding.bind(view)

        init()
    }

    private fun init() {
        userViewModel.showMessage.observe(viewLifecycleOwner, {
            if (it != null && it.contains(register_success)){
                Handler().postDelayed({activity?.finish()}, 1000L)
            }
        })
        binding.buttonRegister.setOnClickListener {
            val email = binding.editUsername.text.toString()
            val password = binding.editConfirmPassword.text.toString()
            val password1 = binding.editPassword.text.toString()
            val name = binding.editName.text.toString()
            val type = "landlord"
            if (password == password1) {
                val registerBody = RegisterBody(email, name, password, type)
                userViewModel.register(registerBody)
            }
            else{
                Log.d(tag_d, "password not correct")
            }
        }

        binding.textRegisterToLogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
    }

}