package com.jason.propertymanager.ui.auth

import com.jason.propertymanager.data.local.AppDataBase
import com.jason.propertymanager.data.model.LoginBody
import com.jason.propertymanager.data.model.RegisterBody
import com.jason.propertymanager.data.model.User
import com.jason.propertymanager.data.network.APICallCenter
import com.jason.propertymanager.other.login_success
import com.jason.propertymanager.other.register_success
import com.jason.propertymanager.data.model.LoginResponse
import com.jason.propertymanager.data.model.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserRepository : APICallCenter.APICallBack {
    var repoCallBack: RepoCallBack? = null
    private val userDao = AppDataBase.getDataBase().userDao()
    fun login(loginBody: LoginBody) {
        APICallCenter.login(this, loginBody)
    }

    fun register(registerBody: RegisterBody) {
        APICallCenter.register(this, registerBody)
    }

    interface RepoCallBack {
        fun onSuccess(user: User)
        fun onMessage(message: String)
    }

    suspend fun getUsers(): List<User>? {
        var users: List<User>? = null
        withContext(Dispatchers.IO) {
            users = userDao.readAll()
        }
        return users
    }

    suspend fun deleteUser() {
        withContext(Dispatchers.IO) {
            userDao.deleteAll()
        }
    }

    suspend fun insertUser(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insert(user)
        }
    }

    override fun notify(message: String?, responseBody: Any?) {
        when (message) {
            login_success -> {
                val user = (responseBody as? LoginResponse)!!.user

                repoCallBack?.onSuccess(user)
            }
            register_success -> {
                val user = (responseBody as? RegisterResponse)!!.data
                //repoCallBack?.onMessage(message + " email ${user.email}")
                repoCallBack?.onSuccess(user)
            }
            else -> {
                repoCallBack?.onMessage(message!!)
            }

        }
    }


}