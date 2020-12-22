package com.jason.propertymanager

import android.util.Log
import com.google.common.truth.Truth.assertThat
import com.jason.propertymanager.data.model.RegisterBody
import com.jason.propertymanager.data.model.RegisterResponse
import com.jason.propertymanager.data.network.APICallCenter
import com.jason.propertymanager.other.tag_d
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun propertyAPI_RegisterResponse(){
        val registerBody = RegisterBody("email", "name", "password", "landlord")
        Log.d(tag_d, "AVC")

        val callback = object : APICallCenter.APICallBack {
            override fun notify(message: String?, responseBody: Any?) {
                Log.d(tag_d, " message $message responseBody $responseBody")
                assertThat(responseBody).isInstanceOf(RegisterResponse::class.java)
            }

        }
        APICallCenter.register(callback, registerBody)
    }
}