package com.jason.propertymanager.ui.property

import android.util.Log
import com.jason.propertymanager.data.local.AppDataBase
import com.jason.propertymanager.data.model.*
import com.jason.propertymanager.data.network.APICallCenter
import com.jason.propertymanager.other.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

class PropertyRepository : APICallCenter.APICallBack {
    private val propertyDao = AppDataBase.getDataBase().propertyDao()
    var repoCallBack: RepoCallBack? = null
    private var myPropertyList: List<Property>? = null
        // notify automatically for myPropertyList change to PropertyViewModel
        set(value) {
            field = value
            repoCallBack?.onPropertyChange()
        }
    var userId: String? = null
    private val instance: PropertyRepository = this

    interface RepoCallBack {
        fun onPropertyChange()
        fun onUpdateSuccess(property: Property)
        fun onUploadImageSuccess(url: String)
    }

    override fun notify(message: String?, responseBody: Any?) {
        Log.d(tag_d, " message $message responseBody $responseBody")
        when (message) {
            get_all_property_success -> {
                val propertyList = (responseBody as? GetAllPropertyResponse)!!.data
                val tempList: ArrayList<Property> = arrayListOf()
                for (property in propertyList) {
                    if (property.userId == userId) {
                        tempList.add(property)
                    }
                }
                myPropertyList = tempList
                saveAllProperty(myPropertyList as ArrayList<Property>)

            }
            update_property_success, upload_property_success -> {
                val property = (responseBody as? UpdatePropertyResponse)!!.data
                repoCallBack?.onUpdateSuccess(property)
            }
            upload_picture_success -> {
                val pictureResponse = (responseBody as? UploadPictureResponse)!!.data
                val url = pictureResponse.location
                repoCallBack?.onUploadImageSuccess(url)
            }
        }
    }

    private suspend fun getAllProperty() {
        withContext(Dispatchers.IO) {
            APICallCenter.getAllProperty(instance)
        }
    }

    private fun saveAllProperty(propertyList: List<Property>) {
        CoroutineScope(Dispatchers.IO).launch {
            propertyDao.insertAll(propertyList)
        }

    }

    suspend fun readAllProperty(): List<Property> {
        withContext(Dispatchers.IO) {
            myPropertyList = propertyDao.readAll()
        }
        return myPropertyList!!

    }

    suspend fun insertProperty(property: Property) {
        withContext(Dispatchers.IO) {
            propertyDao.insert(property)
        }
        readAllProperty()
    }

    suspend fun deleteProperty(property: Property) {
        withContext(Dispatchers.IO) {
            APICallCenter.deleteProperty(instance, property._id)
            propertyDao.delete(property)
        }
        readAllProperty()
    }

    suspend fun uploadImage(file: InputStream) {
        withContext(Dispatchers.IO) {
            APICallCenter.uploadImage(instance, file)
        }

    }

    suspend fun getMyProperty(user: User) {
        userId = user._id
        withContext(Dispatchers.IO) {
            getAllProperty()
        }
    }

    suspend fun uploadProperty(uploadPropertyBody: UploadPropertyBody) {
        withContext(Dispatchers.IO) {
            APICallCenter.uploadProperty(instance, uploadPropertyBody)
        }
    }


}