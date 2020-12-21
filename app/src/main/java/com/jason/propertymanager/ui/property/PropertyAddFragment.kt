package com.jason.propertymanager.ui.property

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.jason.propertymanager.R
import com.jason.propertymanager.data.model.Property.Companion.imageListToString
import com.jason.propertymanager.data.model.UploadPropertyBody
import com.jason.propertymanager.data.model.User
import com.jason.propertymanager.databinding.FragmentPropertyAddBinding
import com.jason.propertymanager.other.REQUEST_CODE_LOAD_IMAGE
import com.jason.propertymanager.other.default_string
import com.jason.propertymanager.other.load_status_1_begin
import com.jason.propertymanager.other.tag_d
import com.jason.propertymanager.ui.helper.AdapterImage
import com.jason.propertymanager.ui.home.MainActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.*

class PropertyAddFragment : Fragment() {
    private val propertyViewModel: PropertyViewModel by activityViewModels()
    private var _binding: FragmentPropertyAddBinding? = null
    private val binding get() = _binding!!
    private var user: User? = null
    private lateinit var adapterImage: AdapterImage
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geoCoder: Geocoder
    val REQUEST_CHECK_SETTINGS = -5
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
        if (user != null){
            binding.buttonSubmit.isEnabled = true
        }
        adapterImage = AdapterImage()
        binding.recyclerPropertyImageList.apply {
            adapter = adapterImage
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        propertyViewModel.user = user

        propertyViewModel.imageList.observe(viewLifecycleOwner, {
            adapterImage.refreshDataList(it!!)
            Log.d(tag_d, "observe imageList, size ${it.size}")
            if (it.isNotEmpty()){
                binding.recyclerPropertyImageList.visibility = View.VISIBLE
            }
            else{
                binding.recyclerPropertyImageList.visibility = View.GONE

            }
        })

        binding.buttonAddPhoto.setOnClickListener {
            selectImage()

        }

        binding.buttonGetCurrentLocation.setOnClickListener {
            requestLocation()
        }

        binding.buttonSubmit.setOnClickListener {
            val address = binding.editAddress.text.toString()
            val city = binding.editCity.text.toString()
            val state = binding.editState.text.toString()
            val country = binding.editCountry.text.toString()
            val purchasePrice = binding.editPrice.text.toString()
            val mortageInfo = binding.checkboxMortageInfo.isChecked
            val propertyStatus = binding.checkboxRentStatus.isChecked
            val userId = user?._id!!
            val userType = user?.type!!
            val image = imageListToString(adapterImage.mList)
            val uploadPropertyBody = UploadPropertyBody(
                address,
                city,
                country,
                image,
                (latitude ?: 100.0).toString(),
                (longitude ?: 100.0).toString(),
                mortageInfo,
                propertyStatus,
                purchasePrice,
                state,
                userId,
                userType
            )
            propertyViewModel.addProperty(uploadPropertyBody)
            propertyViewModel.imageList.value = arrayListOf()
            //propertyViewModel.actionMessage = load_status_1_begin
            propertyViewModel.property.observe(viewLifecycleOwner, {
                Log.d(tag_d, "observe size change in property add fragment ${it?.size}")
                activity?.onBackPressed()
            })

        }
    }


    private fun requestLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geoCoder = Geocoder(requireContext(), Locale.getDefault())
        val locationCallback: LocationCallback = object: LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                if (p0 == null || p0.locations.size < 0) {
                    return
                }
                else {
                    Log.d("abc", "get ${p0.locations.size} location")
                    val location = p0.locations[p0.locations.size - 1]
                    updateEditLocation(location)
                    fusedLocationClient.removeLocationUpdates(this)
                    binding.buttonGetCurrentLocation.isEnabled = true
                }
            }
        }
        val locationRequest: LocationRequest? = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 300
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        checkLocationSetting(locationRequest)
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            binding.buttonGetCurrentLocation.isEnabled = false
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
        else {
            checkPermission()
        }
    }

    private fun checkLocationSetting(locationRequest: LocationRequest?) {

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest ?: return)

        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            Log.d(tag_d, "location setting satisfied ${locationSettingsResponse.locationSettingsStates}")
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS)
                }
                catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(tag_d, "error ${sendEx.message}")
                }
            }
        }
    }

    private fun checkPermission() {
        Dexter.withContext(requireContext())
            .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if ((p0 ?: return).areAllPermissionsGranted()) {
                        Toast.makeText(activity, "Permission granted", Toast.LENGTH_SHORT).show()
                    }
                    else if (p0.isAnyPermissionPermanentlyDenied) {
                        Toast.makeText(activity, "Need Permission to get location", Toast.LENGTH_SHORT).show()
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage("Permission is needed to get location")
                        builder.setPositiveButton("Go to Setting") { dialog, _ ->
                            dialog.dismiss()
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", activity?.packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                        builder.setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        builder.show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
                    p1?.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }

    private fun updateEditLocation(location: Location){
        latitude = location.latitude
        longitude = location.longitude
        val addresses = geoCoder.getFromLocation(latitude!!, longitude!!, 1)
        binding.editAddress.setText(addresses[0].getAddressLine(0))
        binding.editCity.setText(addresses[0].locality)
        binding.editState.setText(addresses[0].adminArea)
        binding.editCountry.setText(addresses[0].countryName)
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
                adapterImage.updateItem(adapterImage.itemCount, load_status_1_begin)
                binding.recyclerPropertyImageList.scrollToPosition(adapterImage.itemCount - 1)
                propertyViewModel.upload(inputSteam!!)
            }
        }
    }


}