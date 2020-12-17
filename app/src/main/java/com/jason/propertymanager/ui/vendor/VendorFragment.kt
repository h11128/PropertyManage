package com.jason.propertymanager.ui.vendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jason.propertymanager.R
import com.jason.propertymanager.databinding.FragmentTransactionBinding
import com.jason.propertymanager.databinding.FragmentVendorBinding
import com.jason.propertymanager.ui.tenant.PropertyViewModel

class VendorFragment : Fragment() {

    private lateinit var galleryViewModel: PropertyViewModel
    private var _binding: FragmentVendorBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProvider(this).get(PropertyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_vendor, container, false)
        _binding = FragmentVendorBinding.bind(root)
        val textView: TextView = binding.textSlideshow
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}