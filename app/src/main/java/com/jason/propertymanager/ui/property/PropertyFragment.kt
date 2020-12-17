package com.jason.propertymanager.ui.property

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jason.propertymanager.R
import com.jason.propertymanager.databinding.FragmentDocumentBinding
import com.jason.propertymanager.databinding.FragmentHomeBinding
import com.jason.propertymanager.databinding.FragmentPropertyBinding
import com.jason.propertymanager.ui.tenant.PropertyViewModel

class PropertyFragment : Fragment() {

    private lateinit var propertyViewModel: PropertyViewModel
    private var _binding: FragmentPropertyBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        propertyViewModel =
                ViewModelProvider(this).get(PropertyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_property, container, false)
        _binding = FragmentPropertyBinding.bind(root)
        val textView: TextView = binding.textSlideshow

        propertyViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.property_option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}