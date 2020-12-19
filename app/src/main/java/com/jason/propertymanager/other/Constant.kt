package com.jason.propertymanager.other

import android.webkit.URLUtil

const val tag_d = "abc"
const val REQUEST_CODE_LOAD_IMAGE = 11
const val url_logo = "https://www.logodesign.net/logo/house-with-sea-waves-and-leaves-6212ld.png?size=2&industry=property-management"
const val id_landlord = 1234
const val id_manager = 1235
const val id_tenant = 1236
const val id_vendor = 1237
const val landlord_string = "landlord"
const val tenant_string = "tenant"
const val default_string = "random"
const val login_success = "login success"
const val register_success ="register success"
const val update_user_success = "update user success"
const val upload_picture_success ="upload image success"
const val get_all_property_success = "get all property success"
const val update_property_success = "update property success"
const val upload_property_success = "upload property success"
const val delete_property_success = "delete property success"

const val load_status_1_begin = "Fetching Url from API"
const val load_status_2_onLoading = "Loading..."
const val load_status_4_success = "Load Image Success"
const val load_status_5_fail = "Load Image Failed"
const val delimiters = ",delimiter,"
class Constant{
    companion object {

        fun isValidUrl(url: String): Boolean {
            return URLUtil.isValidUrl(url)
        }


    }
}