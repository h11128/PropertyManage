package com.jason.propertymanager.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.jason.propertymanager.R
import com.jason.propertymanager.data.model.User
import com.jason.propertymanager.databinding.FragmentHomeBinding
import com.jason.propertymanager.ui.auth.UserViewModel
import com.jason.propertymanager.ui.property.PropertyViewModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private var user: User? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.bind(root)

        val textView: TextView = binding.textHome
        navController = activity?.findNavController(R.id.nav_host_fragment)!!
        initButton()

        homeViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        val propertyViewModel: PropertyViewModel by activityViewModels()
        val userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        userViewModel.currentUser.observe(viewLifecycleOwner, {
            if (it!= null){
                user = it
                propertyViewModel.user = user
            }
        })


        propertyViewModel.property.observe(viewLifecycleOwner, {
            binding.customBTNProperty.number = it.size
        })
        return root
    }

    private fun initButton(){
        binding.customBTNProperty.setClickListener {
            navController.navigate(R.id.action_to_property)
        }
        binding.customBTNCollectRent.setOnClickListener {
            navController.navigate(R.id.action_to_rent)
        }
        binding.customBTNDocument.setOnClickListener {
            navController.navigate(R.id.action_to_document)
        }
        binding.customBTNTenant.setClickListener{
            navController.navigate(R.id.action_to_tenant)
        }
        binding.customBTNTodo.setClickListener{
            navController.navigate(R.id.action_to_todo)
        }
        binding.customBTNVendors.setOnClickListener {
            navController.navigate(R.id.action_to_vendor)
        }

    }

    
}