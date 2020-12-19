package com.jason.propertymanager.data.network

import com.jason.propertymanager.data.model.*
import com.jason.propertymanager.other.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream

class APICallCenter {


    interface APICallBack {
        fun notify(message: String? = null, responseBody: Any? = null)
    }


    companion object {
        private val api = PropertyApi()
        //var _callback: APICallBack? = null
        //val callback get() = _callback!!


        fun login(callback: APICallBack, loginBody: LoginBody) {
            val messageCode = login_success
            api.login(loginBody).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        callback.notify(messageCode, response.body()!!)
                    } else {
                        val jObjError = JSONObject((response.errorBody() ?: return).string())
                        val error = jObjError.getString("message")
                        callback.notify(error, null)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    callback.notify("cause ${t.cause} message ${t.message}", null)

                }
            })
        }

        fun register(callback: APICallBack, registerBody: RegisterBody) {
            val messageCode = register_success
            api.register(registerBody).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        callback.notify(messageCode, response.body()!!)
                    } else {
                        val jObjError = JSONObject((response.errorBody() ?: return).string())
                        val error = jObjError.getString("message")
                        callback.notify(error, null)
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    callback.notify("cause ${t.cause} message ${t.message}", null)

                }
            })
        }

        fun updateUser(callback: APICallBack, userId: String, updateUserBody: UpdateUserBody) {
            val messageCode = update_user_success
            api.updateUser(userId, updateUserBody).enqueue(object : Callback<UpdateUserResponse> {
                override fun onResponse(
                    call: Call<UpdateUserResponse>,
                    response: Response<UpdateUserResponse>
                ) {
                    if (response.isSuccessful) {
                        callback.notify(messageCode, response.body()!!)
                    } else {
                        val jObjError = JSONObject((response.errorBody() ?: return).string())
                        val error = jObjError.getString("message")
                        callback.notify(error, null)
                    }
                }

                override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                    callback.notify("cause ${t.cause} message ${t.message}", null)

                }
            })
        }

        fun getAllProperty(callback: APICallBack) {
            val messageCode = get_all_property_success
            api.getAllProperty().enqueue(object :
                Callback<GetAllPropertyResponse> {
                override fun onResponse(
                    call: Call<GetAllPropertyResponse>,
                    response: Response<GetAllPropertyResponse>
                ) {
                    if (response.isSuccessful) {
                        callback.notify(messageCode, response.body()!!)
                    } else {
                        val jObjError = JSONObject((response.errorBody() ?: return).string())
                        val error = jObjError.getString("message")
                        callback.notify(error, null)
                    }
                }

                override fun onFailure(call: Call<GetAllPropertyResponse>, t: Throwable) {
                    callback.notify("cause ${t.cause} message ${t.message}", null)

                }
            })
        }

        fun uploadProperty(callback: APICallBack, uploadPropertyBody: UploadPropertyBody) {
            val messageCode = upload_property_success
            api.uploadProperty(uploadPropertyBody).enqueue(object :
                Callback<UpdatePropertyResponse> {
                override fun onResponse(
                    call: Call<UpdatePropertyResponse>,
                    response: Response<UpdatePropertyResponse>
                ) {
                    if (response.isSuccessful) {
                        callback.notify(messageCode, response.body()!!)
                    } else {
                        val jObjError = JSONObject((response.errorBody() ?: return).string())
                        val error = jObjError.getString("message")
                        callback.notify(error, null)
                    }
                }

                override fun onFailure(call: Call<UpdatePropertyResponse>, t: Throwable) {
                    callback.notify("cause ${t.cause} message ${t.message}", null)

                }
            })
        }

        fun deleteProperty(callback: APICallBack, propertyId: String) {
            val messageCode = delete_property_success
            api.deleteProperty(propertyId).enqueue(object :
                Callback<UpdatePropertyResponse> {
                override fun onResponse(
                    call: Call<UpdatePropertyResponse>,
                    response: Response<UpdatePropertyResponse>
                ) {
                    if (response.isSuccessful) {
                        callback.notify(messageCode, response.body()!!)
                    } else {
                        val jObjError = JSONObject((response.errorBody() ?: return).string())
                        val error = jObjError.getString("message")
                        callback.notify(error, null)
                    }
                }

                override fun onFailure(call: Call<UpdatePropertyResponse>, t: Throwable) {
                    callback.notify("cause ${t.cause} message ${t.message}", null)

                }
            })
        }

        fun getUserProperty(callback: APICallBack, userId: String) {
            api.getUserProperty(userId).enqueue(object : Callback<GetUserPropertyResponse> {
                override fun onResponse(
                    call: Call<GetUserPropertyResponse>,
                    response: Response<GetUserPropertyResponse>
                ) {
                    if (response.isSuccessful) {
                        callback.notify(
                            "GetUser property success",
                            response.body()!!
                        )
                    } else {
                        val jObjError = JSONObject((response.errorBody() ?: return).string())
                        val error = jObjError.getString("message")
                        callback.notify(error, null)
                    }
                }

                override fun onFailure(call: Call<GetUserPropertyResponse>, t: Throwable) {
                    callback.notify("cause ${t.cause} message ${t.message}", null)

                }
            })
        }

        fun uploadImage(callback: APICallBack, image: InputStream) {
            val messageCode = upload_picture_success
            val requestFile = RequestBody.create(MediaType.parse("image/*"), image.readBytes())
            //val requestFile = RequestBodyUtil.create(MediaType.parse("image/*"), image)
            //val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image)
            val body = MultipartBody.Part.createFormData("image", "abac.jpg", requestFile)
            api.uploadImage(body).enqueue(object : Callback<UploadPictureResponse> {
                override fun onResponse(
                    call: Call<UploadPictureResponse>,
                    response: Response<UploadPictureResponse>
                ) {
                    if (response.isSuccessful) {
                        callback.notify(messageCode, response.body()!!)
                    } else {
                        val jObjError = JSONObject((response.errorBody() ?: return).string())
                        val error = jObjError.getString("message")
                        callback.notify(error, null)
                    }
                }

                override fun onFailure(call: Call<UploadPictureResponse>, t: Throwable) {
                    callback.notify("cause ${t.cause} message ${t.message}", null)

                }
            })
        }

    }

}