package com.jason.propertymanager.ui.rent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jason.propertymanager.R
import com.jason.propertymanager.databinding.FragmentRentBinding
import com.jason.propertymanager.ui.property.PropertyViewModel

class RentFragment : Fragment() {

    private lateinit var galleryViewModel: PropertyViewModel
    private var _binding: FragmentRentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProvider(this).get(PropertyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_rent, container, false)
        _binding = FragmentRentBinding.bind(root)
        val textView: TextView = binding.textSlideshow
        galleryViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }
}