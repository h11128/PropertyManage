package com.jason.propertymanager.ui.property

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

class PropertyViewModel : ViewModel(), PropertyRepository.RepoCallBack {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val property = MutableLiveData<List<Property>>()
    val text: LiveData<String> = _text
    var imageList = MutableLiveData<ArrayList<String>>()
    private val repo = PropertyRepository().apply {
        repoCallBack = this@PropertyViewModel
    }

    init {
        property.value = arrayListOf()
        imageList.value = arrayListOf()
        refreshProperty()
    }

    fun upload(file: InputStream) {
        viewModelScope.launch {
            repo.uploadImage(file)
        }
    }

    // get property online, save it in database
    fun getMyProperty(user: User) {
        viewModelScope.launch {
            repo.getMyProperty(user)
        }
    }

    fun addProperty(uploadPropertyBody: UploadPropertyBody) {
        viewModelScope.launch {
            repo.uploadProperty(uploadPropertyBody)
        }
    }

    override fun onPropertyChange() {
        refreshProperty()
    }

    override fun onUpdateSuccess(property: Property) {
        viewModelScope.launch {
            repo.insertProperty(property)
        }
    }

    override fun onUploadImageSuccess(url: String) {
        val temp = imageList.value!!
        temp[temp.size - 1] = url
        imageList.value = temp
        //imageList.value = temp as List<String>
        Log.d(tag_d, "add $url in imageList, size ${imageList.value?.size}")
    }

    private fun refreshProperty() {
        viewModelScope.launch {
            property.value = repo.readAllProperty()
        }
    }


}