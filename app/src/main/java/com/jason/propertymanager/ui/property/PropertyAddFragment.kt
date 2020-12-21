package com.jason.propertymanager.ui.property

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.jason.propertymanager.R
import com.jason.propertymanager.data.model.Property
import com.jason.propertymanager.data.model.Property.Companion.imageListToString
import com.jason.propertymanager.data.model.Property.Companion.imageStringToImageList
import com.jason.propertymanager.data.model.UploadPropertyBody
import com.jason.propertymanager.data.model.User
import com.jason.propertymanager.databinding.FragmentPropertyAddBinding
import com.jason.propertymanager.other.REQUEST_CODE_LOAD_IMAGE
import com.jason.propertymanager.other.landlord_string
import com.jason.propertymanager.other.load_status_1_begin
import com.jason.propertymanager.other.tag_d
import com.jason.propertymanager.ui.helper.AdapterImage
import com.jason.propertymanager.ui.home.MainActivity
import java.util.*

class PropertyAddFragment : Fragment() {
    private val propertyViewModel: PropertyViewModel by activityViewModels()
    private var _binding: FragmentPropertyAddBinding? = null
    private val binding get() = _binding!!
    private var user: User? = null
    private var property: Property? = null
    private lateinit var adapterImage: AdapterImage
    var latitude: Double? = null
    var longitude: Double? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        user = (context as MainActivity).user

    }

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
        init()
    }

    private fun init() {
        if (user != null) {
            binding.buttonSubmit.isEnabled = true
        }
        adapterImage = AdapterImage()
        binding.recyclerPropertyImageList.apply {
            adapter = adapterImage
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        propertyViewModel.user = user

        propertyViewModel.imageList.observe(viewLifecycleOwner, {
            adapterImage.refreshDataList(it!!)
            Log.d(tag_d, "observe imageList, size ${it.size}")
            if (it.isNotEmpty()) {
                binding.recyclerPropertyImageList.visibility = View.VISIBLE
            } else {
                binding.recyclerPropertyImageList.visibility = View.GONE

            }
        })

        binding.buttonAddPhoto.setOnClickListener {
            selectImage()

        }

        binding.buttonGetCurrentLocation.setOnClickListener {
            requestLocation()
        }
        initText()
        binding.buttonSubmit.setOnClickListener {

            val userId = user?._id!!
            val userType = user?.type!!
            val addressA = binding.editAddress.text.toString()
            val city1 = binding.editCity.text.toString()
            val state1 = binding.editState.text.toString()
            val country1 = binding.editCountry.text.toString()
            val purchasePrice1 = binding.editPrice.text.toString()
            val mortageInfo1 = binding.checkboxMortageInfo.isChecked
            val propertyStatus1 = binding.checkboxRentStatus.isChecked
            val image1 = imageListToString(adapterImage.mList)
            val uploadPropertyBody = UploadPropertyBody(
                addressA,
                city1,
                country1,
                image1,
                (latitude ?: 100.0).toString(),
                (longitude ?: 100.0).toString(),
                mortageInfo1,
                propertyStatus1,
                purchasePrice1,
                state1,
                userId,
                userType
            )
            if (property == null) {
                propertyViewModel.addProperty(uploadPropertyBody)
            } else {
                propertyViewModel.updateProperty(uploadPropertyBody)
            }
            //propertyViewModel.actionMessage = load_status_1_begin
            clearData()

            propertyViewModel.property.observe(viewLifecycleOwner, {
                Log.d(tag_d, "observe size change in property add fragment ${it?.size}")
                activity?.onBackPressed()
            })

        }
    }

    override fun onStop() {
        super.onStop()
        clearData()

    }

    fun clearData(){
        property = null
        propertyViewModel.imageList.value = arrayListOf()
    }


    private fun requestLocation() {
        propertyViewModel.locationRepo = LocationRepository(requireActivity()).apply {
            callback = propertyViewModel
            this.requestLocation()
        }
        propertyViewModel.locationResult.observe(viewLifecycleOwner, {
            if (it != null) {
                val location = it
                latitude = location.latitude
                longitude = location.longitude
                val geoCoder = Geocoder(requireContext(), Locale.getDefault())

                val addresses = geoCoder.getFromLocation(latitude!!, longitude!!, 1)
                binding.editAddress.setText(addresses[0].getAddressLine(0))
                binding.editCity.setText(addresses[0].locality)
                binding.editState.setText(addresses[0].adminArea)
                binding.editCountry.setText(addresses[0].countryName)
            }
        })
    }

    private fun initText() {
        if (property != null) {
            binding.editAddress.setText(property!!.address)
            binding.editCity.setText(property!!.city)
            binding.editState.setText(property!!.state)
            binding.editCountry.setText(property!!.country)
            binding.editPrice.setText(property!!.purchasePrice)
            binding.checkboxMortageInfo.isChecked = property!!.mortageInfo
            binding.checkboxRentStatus.isChecked = property!!.propertyStatus
            val images = imageStringToImageList(property!!.image)
            Log.d(tag_d, "image $images imageString ${property!!.image}")
            for (image in images) {

                propertyViewModel.imageList.value?.add(image)
            }
        }

    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQUEST_CODE_LOAD_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOAD_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                val inputSteam = activity?.contentResolver?.openInputStream(uri)
                propertyViewModel.imageList.value?.add(load_status_1_begin)
                //adapterImage.updateItem(adapterImage.itemCount, load_status_1_begin)
                binding.recyclerPropertyImageList.scrollToPosition(adapterImage.itemCount - 1)
                propertyViewModel.upload(inputSteam!!)
            }
        }
    }

    companion object {
        fun newInstance(propertyA: Property? = null): PropertyAddFragment {
            return PropertyAddFragment().apply {
                property = propertyA
            }


        }
    }


}