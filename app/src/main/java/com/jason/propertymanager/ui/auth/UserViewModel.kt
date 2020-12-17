package com.jason.propertymanager.ui.auth

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.propertymanager.data.model.LoginBody
import com.jason.propertymanager.data.model.RegisterBody
import com.jason.propertymanager.data.model.User
import com.jason.propertymanager.other.tag_d
import kotlinx.coroutines.launch


class UserViewModel() : ViewModel(), UserRepository.RepoCallBack {

    private val userRepository = UserRepository().apply {
        repoCallBack = this@UserViewModel
    }

    var currentUser = MutableLiveData<User>()
    val registerType = MutableLiveData<Int>()
    val userNameValid = MutableLiveData<Boolean>()
    val passwordValid = MutableLiveData<Boolean>()
    val showMessage = MutableLiveData<String?>()

    init {
        registerType.value = -1
        currentUser.value = null
        userNameValid.value = false
        passwordValid.value = false
        showMessage.value = null
        checkUser()
    }

    override fun onSuccess(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
            checkUser()
        }
    }

    override fun onMessage(message: String) {
        Log.d(tag_d, message)
        showMessage.value = message
    }

    fun loginDataChanged(email: String, password: String) {
        userNameValid.value = isUserNameValid(email)
        passwordValid.value = isPasswordValid(password)
    }


    fun login(email: String, password: String) {
        val loginData = LoginBody(email, password)
        userRepository.login(loginData)
    }

    fun logOut() {
        viewModelScope.launch {
            userRepository.deleteUser()
            checkUser()
        }
    }

    private fun checkUser() {
        viewModelScope.launch{
            currentUser.value = userRepository.getUsers()!![0]
            Log.d(tag_d, "check login, current User ${currentUser.value!!}")
        }
    }

    fun register(registerBody: RegisterBody) {
        userRepository.register(registerBody)
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }


}