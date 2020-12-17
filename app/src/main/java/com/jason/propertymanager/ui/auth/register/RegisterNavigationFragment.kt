package com.jason.propertymanager.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.jason.propertymanager.R
import com.jason.propertymanager.databinding.FragmentRegisterNavigationBinding
import com.jason.propertymanager.ui.auth.UserViewModel
import com.jason.propertymanager.other.id_landlord
import com.jason.propertymanager.other.id_manager
import com.jason.propertymanager.other.id_tenant
import com.jason.propertymanager.other.id_vendor

class RegisterNavigationFragment : Fragment() {
    private var _binding: FragmentRegisterNavigationBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_register_navigation, container, false)
        _binding = FragmentRegisterNavigationBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.buttonRegisterLandlord.setOnClickListener {
            userViewModel.registerType.value = id_landlord
        }
        binding.buttonRegisterManager.setOnClickListener {
            userViewModel.registerType.value = id_manager

        }
        binding.buttonRegisterTenant.setOnClickListener {
            userViewModel.registerType.value = id_tenant

        }
        binding.buttonRegisterVendor.setOnClickListener {
            userViewModel.registerType.value = id_vendor

        }
    }

}