package com.jason.propertymanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jason.propertymanager.other.Constant
import com.jason.propertymanager.other.delimiters

@Entity(tableName = "property_table")
data class Property(
    val __v: Int,
    @PrimaryKey val _id: String,
    var address: String,
    var city: String,
    var country: String,
    var image: String,
    var latitude: String,
    var longitude: String,
    var mortageInfo: Boolean,
    var propertyStatus: Boolean,
    var purchasePrice: String,
    var state: String,
    var userId: String,
    var userType: String
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
            val result = arrayListOf<String>()
            for (url in aa) {
                if (Constant.isValidUrl(url)) {
                    result.add(url)
                }
            }
            return result

        }
    }

}