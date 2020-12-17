package com.jason.propertymanager.ui.tenant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.propertymanager.data.model.Property
import com.jason.propertymanager.data.model.User
import com.jason.propertymanager.ui.property.PropertyRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

class PropertyViewModel : ViewModel(), PropertyRepository.RepoCallBack {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val property =  MutableLiveData<List<Property>>()
    val text: LiveData<String> = _text
    val repo = PropertyRepository().apply {
        repoCallBack = this@PropertyViewModel
    }

    init {
        property.value = arrayListOf()
        refreshProperty()
    }

    fun upload(file: InputStream) {
        viewModelScope.launch {
            repo.uploadImage(file)
        }
    }

    fun getMyProperty(user: User){
        viewModelScope.launch {
            repo.getMyProperty(user)
        }
    }

    override fun updateMyProperty(propertyList: List<Property>) {
        viewModelScope.launch {
            repo.saveAllProperty(propertyList)
        }
        refreshProperty()
    }

    fun refreshProperty(){
        viewModelScope.launch {
            property.value = repo.readAllProperty()
        }
    }


}