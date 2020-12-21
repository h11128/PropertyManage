package com.jason.propertymanager.data.network

import com.jason.propertymanager.data.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val url_base = "https://apolis-property-management.herokuapp.com"
const val endpoint_login = "/api/auth/login"
const val endpoint_register = "/api/auth/register"
const val endpoint_all_user = "/api/users"
const val endpoint_user = "/api/users/{id}"
const val endpoint_all_property = "/api/property"
const val endpoint_update_property = "/api/property/{id}"
const val endpoint_property = "/api/property/user/{id}"

const val endpoint_all_tenant = "/api/tenant"
const val endpoint_tenant = "/api/tenant/{id}" //api not built properly
const val endpoint_landlord = "/api/tenant/landlord/{id}"
const val endpoint_upload_picture = "/api/upload/property/picture"

interface PropertyApi{

    companion object{
        var currentUser: User? = null
        operator fun invoke(): PropertyApi {
            return Retrofit.Builder()
                .baseUrl(url_base)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PropertyApi::class.java)
        }


    }

    @POST(endpoint_register)
    fun register(@Body body: RegisterBody): Call<RegisterResponse>

    @POST(endpoint_login)
    fun login(@Body body: LoginBody): Call<LoginResponse>

    @GET(endpoint_all_user)
    fun getAllUsers(): Call<GetAllUserResponse>

    @GET(endpoint_user)
    fun getUser(@Path("id") id: String): Call<GetUserResponse>

    @PUT(endpoint_user)
    fun updateUser(@Path("id") id: String, @Body body: UpdateUserBody): Call<UpdateUserResponse>

    @GET(endpoint_all_property)
    fun getAllProperty(): Call<GetAllPropertyResponse>

    @POST(endpoint_all_property)
    fun uploadProperty(@Body body: UploadPropertyBody): Call<UpdatePropertyResponse>

    @PUT(endpoint_update_property)
    fun updateProperty(@Path("id") id: String, @Body body: UploadPropertyBody): Call<UpdatePropertyResponse>

    @DELETE(endpoint_update_property)
    fun deleteProperty(@Path("id") id: String): Call<UpdatePropertyResponse>

    @GET(endpoint_property)
    fun getUserProperty(@Path("id") id: String): Call<GetUserPropertyResponse>

    @GET(endpoint_all_tenant)
    fun getAllTenants(): Call<GetAllTenantsResponse>

    @GET(endpoint_landlord)
    fun getLandLordFromTenant(@Path("id") id: String): Call<GetLandLordFromTenantResponse>

    @Multipart
    @POST(endpoint_upload_picture)
    fun uploadImage(@Part part : MultipartBody.Part): Call<UploadPictureResponse>

    //@POST(endpoint_upload_picture)
    //fun uploadPicture(@Part("image") part : RequestBody): Call<UploadPictureResponse>




}