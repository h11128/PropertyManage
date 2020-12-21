package com.jason.propertymanager.ui.property

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.propertymanager.data.model.Property
import com.jason.propertymanager.data.model.UploadPropertyBody
import com.jason.propertymanager.data.model.User
import com.jason.propertymanager.other.tag_d
import kotlinx.coroutines.launch
import java.io.InputStream

class PropertyViewModel : ViewModel(), PropertyRepository.RepoCallBack, LocationRepository.LocationResultCallBack {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val property = MutableLiveData<ArrayList<Property>>()
    val text: LiveData<String> = _text
    var imageList = MutableLiveData<ArrayList<String>>()
    private val repo = PropertyRepository().apply {
        repoCallBack = this@PropertyViewModel
    }

    var locationRepo: LocationRepository? = null

    val locationResult = MutableLiveData<Location?>()



    var actionMessage: String? = null
    var user: User? = null
        set(value){
            field = value
            repo.userId = value?._id
            Log.d(tag_d, "set user to $user in viewModel, set userId in repo ${value?._id}")
            readAllProperty()
        }


    init {
        property.value = arrayListOf()
        imageList.value = arrayListOf()
        locationResult.value = null
        // read all property from database for the first time
        getMyProperty()

    }

    // send upload image request to API
    fun upload(file: InputStream) {
        viewModelScope.launch {
            repo.uploadImage(file)
        }
    }

    // send get all property request to api
    private fun getMyProperty() {
        viewModelScope.launch {
            repo.getMyProperty()
        }
    }

    // send upload property request to api
    fun addProperty(uploadPropertyBody: UploadPropertyBody) {
        viewModelScope.launch {
            repo.uploadProperty(uploadPropertyBody)
        }
    }
    // send delete property request to api
    fun deleteProperty(property: Property){
        viewModelScope.launch {
            repo.deletePropertyRequest(property)
        }
    }

    // read all property from database
    private fun readAllProperty(){
        viewModelScope.launch {
            repo.readAllProperty()
        }
    }

    // update or upload property request success, insert property response in database
    override fun onUpdateSuccess(property: Property) {
        viewModelScope.launch {
            repo.insertProperty(property)
        }
    }

    // delete request success, delete property in database
    override fun onDeleteSuccess(property: Property) {
        viewModelScope.launch {
            repo.deleteProperty(property)
        }
    }

    // upload image success, update image list
    override fun onUploadImageSuccess(url: String) {
        val temp = imageList.value!!
        temp[temp.size - 1] = url
        imageList.value = temp
        //imageList.value = temp as List<String>
        Log.d(tag_d, "add $url in imageList, size ${imageList.value?.size}")
    }

    // sync myPropertyList with propertyList
    override fun onPropertyChange() {
        Log.d(tag_d, "on Property change")
        refreshProperty()
    }

    // sync property with repo
    private fun refreshProperty() {
        Log.d(tag_d, "refresh property in view model size from ${property.value?.size} to ${repo.myPropertyList?.size}")
        val temp = repo.myPropertyList
        property.value = temp
    }

    override fun update(result: Location?) {
        locationResult.value = result
    }

}