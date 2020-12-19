package com.jason.propertymanager.ui.property

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jason.propertymanager.R
import com.jason.propertymanager.data.model.Property
import com.jason.propertymanager.data.model.User
import com.jason.propertymanager.databinding.FragmentPropertyBinding
import com.jason.propertymanager.other.landlord_string
import com.jason.propertymanager.other.tag_d
import com.jason.propertymanager.ui.helper.AdapterProperty
import com.jason.propertymanager.ui.home.MainActivity

class PropertyFragment : Fragment() {

    private val propertyViewModel: PropertyViewModel by activityViewModels()
    private var _binding: FragmentPropertyBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterProperty: AdapterProperty
    private var user: User? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        user = (context as MainActivity).user
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_property, container, false)
        _binding = FragmentPropertyBinding.bind(root)
        init()
        return root
    }

    private fun init() {
        Log.d(tag_d, "init function")
        binding.fab.setOnClickListener {
            if (user?.type.equals(landlord_string)){
                Log.d(tag_d, "on fab click")
                activity?.supportFragmentManager?.beginTransaction()
                    ?.addToBackStack("propertyFragment")
                    ?.replace(R.id.nav_host_fragment, PropertyAddFragment())
                    ?.commit()
            }
            else{
                Toast.makeText(requireContext(), "Sorry ${user!!.name}, You can't add property as ${user?.type}", Toast.LENGTH_SHORT).show()
            }
        }
        binding.fab.bringToFront()
        adapterProperty = AdapterProperty().apply {
            mParentFragment = this@PropertyFragment
        }

        binding.recyclerProperty.apply {
            adapter = adapterProperty
            layoutManager = LinearLayoutManager(requireContext())

        }
/*        if (propertyViewModel.actionMessage == load_status_1_begin){
            Log.d(tag_d, "add fake property to adapter")
            adapterProperty.updateItem(adapterProperty.itemCount, Property.fakeProperty(image = load_status_1_begin))
            binding.recyclerProperty.scrollToPosition(adapterProperty.itemCount - 1)
            CoroutineScope(Dispatchers.IO).launch {
                delay(2000L)
            }
            propertyViewModel.actionMessage = null
        }*/
        propertyViewModel.user = user
        propertyViewModel.property.observe(viewLifecycleOwner, {
            Log.d(tag_d, "observe property ${it.size}")

            adapterProperty.refreshDataList(it)
        })
    }

    fun deleteProperty(property: Property){
        propertyViewModel.deleteProperty(property)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.property_option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}