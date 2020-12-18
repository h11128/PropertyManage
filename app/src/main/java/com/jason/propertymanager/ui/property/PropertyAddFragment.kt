package com.jason.propertymanager.ui.property

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jason.propertymanager.R
import com.jason.propertymanager.data.model.UploadPropertyBody
import com.jason.propertymanager.data.model.User
import com.jason.propertymanager.databinding.FragmentPropertyAddBinding
import com.jason.propertymanager.other.REQUEST_CODE_LOAD_IMAGE
import com.jason.propertymanager.other.default_string
import com.jason.propertymanager.other.load_status_1
import com.jason.propertymanager.other.tag_d
import com.jason.propertymanager.ui.home.MainActivity

class PropertyAddFragment : Fragment() {
    private val propertyViewModel: PropertyViewModel by activityViewModels()
    private var _binding: FragmentPropertyAddBinding? = null
    private val binding get() = _binding!!
    private var user: User? = null
    private lateinit var imageListAdapter: ImageListAdapter

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun init() {
        if (user != null){
            binding.buttonSubmit.isEnabled = true
        }
        imageListAdapter = ImageListAdapter()
        binding.recyclerPropertyImageList.apply {
            adapter = imageListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        propertyViewModel.imageList.observe(viewLifecycleOwner, {
            imageListAdapter.refreshDataList(it!!)
            Log.d(tag_d, "observe imageList, size ${it.size}")
            if (it.isNotEmpty()){
                binding.recyclerPropertyImageList.visibility = View.VISIBLE
            }
            else{
                binding.recyclerPropertyImageList.visibility = View.GONE

            }
        })

/*        propertyViewModel.imageListSize.observe(viewLifecycleOwner, {
            imageListAdapter.refreshDataList(propertyViewModel.imageList)
            Log.d(tag_d, "observe imageList, size ${it}")
            if (it > 0){
                binding.recyclerPropertyImageList.visibility = View.VISIBLE
            }
            else{
                binding.recyclerPropertyImageList.visibility = View.GONE

            }
        })*/

        binding.buttonAddPhoto.setOnClickListener {
            selectImage()

        }
        binding.buttonSubmit.setOnClickListener {
            val address = binding.editAddress.text.toString()
            val city = binding.editCity.text.toString()
            val state = binding.editState.text.toString()
            val country = binding.editCountry.text.toString()
            val latitude = default_string
            val longitude = default_string
            val purchasePrice = binding.editPrice.text.toString()
            val mortageInfo = binding.checkboxMortageInfo.isChecked
            val propertyStatus = binding.checkboxRentStatus.isChecked
            val userId = user?._id!!
            val userType = user?.type!!
            val image = imageListToString(imageListAdapter.mList)
            val uploadPropertyBody = UploadPropertyBody(
                address,
                city,
                country,
                image,
                latitude,
                longitude,
                mortageInfo,
                propertyStatus,
                purchasePrice,
                state,
                userId,
                userType
            )
            propertyViewModel.addProperty(uploadPropertyBody)
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
                imageListAdapter.updateItem(imageListAdapter.itemCount, load_status_1)
                binding.recyclerPropertyImageList.scrollToPosition(imageListAdapter.itemCount)
                propertyViewModel.upload(inputSteam!!)
            }
        }
    }

    companion object {
        fun imageListToString(imageList: ArrayList<String>): String {
            return if (imageList.size == 0) {
                ""
            } else if (imageList.size == 1) {
                imageList[0]
            } else {
                var text = ""
                for (i in imageList.indices - 1) {
                    text += imageList[i] + ","
                }
                text += imageList[imageList.size - 1]
                text
            }
        }

        fun imageStringToImageList(imageString: String): List<String> {
            return imageString.split(",") as ArrayList<String>
        }


    }


}