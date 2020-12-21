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
    var myPropertyList: ArrayList<Property>? = null
    var userId: String? = null
    private val instance: PropertyRepository = this

    interface RepoCallBack {
        // notify automatically change of myPropertyList to PropertyViewModel
        fun onPropertyChange()
        fun onUpdateSuccess(property: Property)
        fun onDeleteSuccess(property: Property)
        fun onUploadImageSuccess(url: String)
    }
    // call back when receive network response
    override fun notify(message: String?, responseBody: Any?) {
        Log.d(tag_d, " message $message responseBody $responseBody")
        when (message) {
            get_all_property_success -> {
                // receive GetAllPropertyResponse from api, filter property, save result in database
                val propertyList = (responseBody as? GetAllPropertyResponse)!!.data
                saveAllProperty(filterProperty(propertyList, userId!!))

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
            delete_property_success -> {
                val property = (responseBody as? UpdatePropertyResponse)!!.data
                repoCallBack?.onDeleteSuccess(property)
            }
        }
    }

    // send upload image request to API
    suspend fun uploadImage(file: InputStream) {
        withContext(Dispatchers.IO) {
            APICallCenter.uploadImage(instance, file)
        }
    }

    // save userId, send get all property request to api
    suspend fun getMyProperty() {
        getAllProperty()
    }

    // send get all property request to api
    private suspend fun getAllProperty() {
        withContext(Dispatchers.IO) {
            APICallCenter.getAllProperty(instance)
        }
    }

    // send delete request to api
    suspend fun deletePropertyRequest(property: Property) {
        withContext(Dispatchers.IO) {
            APICallCenter.deleteProperty(instance, property._id)

        }
    }

    // send upload property request to api
    suspend fun uploadProperty(uploadPropertyBody: UploadPropertyBody) {
        withContext(Dispatchers.IO) {
            APICallCenter.uploadProperty(instance, uploadPropertyBody)
        }
    }

    suspend fun updateProperty(uploadPropertyBody: UploadPropertyBody) {
        withContext(Dispatchers.IO) {
            APICallCenter.updateProperty(instance, uploadPropertyBody)
        }
    }

    // database method
    // save a list of property in database
    private fun saveAllProperty(propertyList: ArrayList<Property>) {
        CoroutineScope(Dispatchers.IO).launch {
            propertyDao.insertAll(propertyList)
            readAllProperty()
        }
    }

    // read all property in database and set value to myPropertyList
    suspend fun readAllProperty(): List<Property> {

        withContext(Dispatchers.IO) {
            val templist = propertyDao.readAll()
            Log.d(tag_d, "read all property in db")
            myPropertyList = if (userId!= null){
                filterProperty(templist, userId!!)
            } else{
                Log.d(tag_d, "userId is null")
                arrayListOf()
            }

        }
        Log.d(tag_d, "mypropertylist size read from database ${myPropertyList?.size}")
        withContext(Dispatchers.Main){
            repoCallBack?.onPropertyChange()

        }
        return myPropertyList!!
    }

    // insert property in database
    suspend fun insertProperty(property: Property) {
        Log.d(tag_d, "insert property $property to db")
        withContext(Dispatchers.IO) {
            propertyDao.insert(property)
        }
        readAllProperty()
    }

    // delete property in database
    suspend fun deleteProperty(property: Property) {
        withContext(Dispatchers.IO) {
            propertyDao.delete(property)
        }
        readAllProperty()
    }

    private fun filterProperty(propertyList: List<Property>, userId: String): ArrayList<Property> {
        val tempList: ArrayList<Property> = arrayListOf()
        for (property in propertyList) {
            if (property.userId == userId) {
                tempList.add(property)
            }
        }
        Log.d(tag_d, "filter ${propertyList.size} property to ${tempList.size}")

        return tempList
    }


}