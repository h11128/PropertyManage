package data

import com.jason.propertymanager.data.model.PictureResponse
import com.jason.propertymanager.data.model.Property
import com.jason.propertymanager.data.model.Tenant
import com.jason.propertymanager.data.model.User


data class RegisterResponse(
    val `data`: User,
    val error: Boolean,
    val message: String
)
data class LoginResponse(
    val token: String,
    val user: User
)

data class GetUserResponse(
    val `data`: User,
    val error: Boolean
)

data class UpdateUserResponse(
    val `data`: User,
    val error: Boolean,
    val message: String
)

data class GetAllUserResponse(
    val count: Int,
    val `data`: List<User>,
    val error: Boolean
)
data class GetAllPropertyResponse(
    val count: Int,
    val `data`: List<Property>,
    val error: Boolean
)

data class UpdatePropertyResponse(
    val `data`: Property,
    val error: Boolean,
    val message: String
)

data class GetUserPropertyResponse(
    val `data`: List<Property>,
    val error: Boolean
)
data class GetAllTenantsResponse(
    val count: Int,
    val `data`: List<Tenant>,
    val error: Boolean
)

data class GetLandLordFromTenantResponse(
    val `data`: LandLordData,
    val error: Boolean
)

data class LandLordData(
    val __v: Int,
    val _id: String
)

data class UploadPictureResponse(
    val `data`: PictureResponse,
    val error: Boolean,
    val message: String
)

