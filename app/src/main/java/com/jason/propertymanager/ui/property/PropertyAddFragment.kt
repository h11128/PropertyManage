package com.jason.propertymanager.ui.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.jason.propertymanager.R
import com.jason.propertymanager.databinding.FragmentPropertyAddBinding

class PropertyAddFragment : Fragment() {
    private val propertyViewModel: PropertyViewModel by activityViewModels()
    private var _binding: FragmentPropertyAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_property_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPropertyAddBinding.bind(view)
    }

}