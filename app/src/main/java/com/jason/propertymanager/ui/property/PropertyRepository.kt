package com.jason.propertymanager.ui.property

import android.util.Log
import com.jason.propertymanager.data.local.AppDataBase
import com.jason.propertymanager.data.model.Property
import com.jason.propertymanager.data.model.User
import com.jason.propertymanager.data.network.APICallCenter
import com.jason.propertymanager.other.get_all_property_success
import com.jason.propertymanager.other.tag_d
import com.jason.propertymanager.other.update_property_success
import data.GetAllPropertyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

class PropertyRepository: APICallCenter.APICallBack {
    val propertyDao = AppDataBase.getDataBase().propertyDao()
    var repoCallBack: RepoCallBack? = null
    var myPropertyList: List<Property>? = null
        set(value){
            field = value
            repoCallBack?.updateMyProperty(myPropertyList!!)
        }
    var userId: String? =null
    val instance: PropertyRepository = this

    interface RepoCallBack{
        fun updateMyProperty(propertyList: List<Property>)
    }

    override fun notify(message: String?, responseBody: Any?) {
        Log.d(tag_d, " message $message responseBody $responseBody")
        when(message){
            get_all_property_success -> {
                val propertyList = (responseBody as? GetAllPropertyResponse)!!.data
                var tempList: ArrayList<Property> = arrayListOf()
                for (property in propertyList){
                    if (property.userId == userId){
                        tempList.add(property)
                    }
                }
                myPropertyList = tempList

            }
            update_property_success -> {
                //
            }

        }
    }

    suspend fun getAllProperty(){
        APICallCenter.getAllProperty(this)
    }

    suspend fun saveAllProperty(propertyList: List<Property>){
        withContext(Dispatchers.IO){
            propertyDao.insertAll(propertyList)
        }
    }

    suspend fun readAllProperty(): List<Property> {
        withContext(Dispatchers.IO){
            myPropertyList = propertyDao.readAll()
        }
        return myPropertyList!!

    }

    suspend fun saveProperty(property: Property){
        withContext(Dispatchers.IO){
            propertyDao.insert(property)
        }
    }

    suspend fun deleteProperty(property: Property){
        APICallCenter.deleteProperty(this, property._id)
        withContext(Dispatchers.IO){
            propertyDao.delete(property)

        }
    }

    suspend fun uploadImage(file: InputStream) {
        withContext(Dispatchers.IO){
            APICallCenter.uploadImage(instance, file)

        }
    }

    suspend fun getMyProperty(user: User) {
        userId = user._id
        withContext(Dispatchers.IO){
            getAllProperty()
        }


    }


}