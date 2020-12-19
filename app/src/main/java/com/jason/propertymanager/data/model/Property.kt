package com.jason.propertymanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jason.propertymanager.other.delimiters

@Entity(tableName = "property_table")
data class Property(
    val __v: Int,
    @PrimaryKey val _id: String,
    val address: String,
    val city: String,
    val country: String,
    val image: String,
    val latitude: String,
    val longitude: String,
    val mortageInfo: Boolean,
    val propertyStatus: Boolean,
    val purchasePrice: String,
    val state: String,
    val userId: String,
    val userType: String
) {
    fun getAddressString(): String {
        return "$address, $city, $state, $country"
    }

    fun getDetailString(): String {
        val rentStatus = if (propertyStatus) {
            "is rented"
        } else {
            "not rented"
        }
        val mortageStatus = if (mortageInfo) {
            "is on mortage"
        } else {
            "not on mortage"
        }
        return "${rentStatus}, $mortageStatus"
    }


    companion object {
        fun imageListToString(imageList: ArrayList<String>): String {
            return when (imageList.size) {
                0 -> {
                    ""
                }
                1 -> {
                    imageList[0] + delimiters
                }
                else -> {
                    var text = ""
                    for (i in imageList.indices - 1) {
                        text += imageList[i] + delimiters
                    }
                    text += imageList[imageList.size - 1]
                    text
                }
            }
        }

        fun imageStringToImageList(imageString: String): List<String> {
            val aa = imageString.split(delimiters)
            if (aa.isNotEmpty() && aa[0] != imageString) {
                return aa
            }
            val bb = imageString.split(",")
            return if (bb.isNotEmpty() && bb[0] != imageString) {
                bb
            } else {
                arrayListOf(imageString)
            }
        }
    }

}