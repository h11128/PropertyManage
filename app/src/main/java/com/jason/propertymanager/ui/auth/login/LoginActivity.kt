package com.jason.propertymanager.ui.auth.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jason.propertymanager.R
import com.jason.propertymanager.databinding.ActivityLoginBinding
import com.jason.propertymanager.other.tag_d
import com.jason.propertymanager.other.url_logo
import com.jason.propertymanager.ui.auth.UserViewModel
import com.jason.propertymanager.ui.auth.register.RegisterActivity
import com.jason.propertymanager.ui.home.MainActivity
import com.squareup.picasso.Picasso

class LoginActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }
    private fun initViewModel(){
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.currentUser.observe(this, {
            if (it != null) {
                //val user = it[0]
                loginSuccess("email ${it.email} name ${it.name}")
                //loginSuccess("email ${user.email} name ${user.name}")
                Log.d(tag_d, "login success")
                setResult(Activity.RESULT_OK)
                finish()
            }

        })
        userViewModel.showMessage.observe(this, {
            if (it!=null){
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                userViewModel.showMessage.value = null
            }
        })
    }

    private fun init() {
        val username = binding.editUsername
        val password = binding.editPassword
        val login = binding.buttonLogin
        Picasso.get().load(url_logo).into(binding.imageLogo)
        username.afterTextChanged {
            userViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }
        password.apply {
            afterTextChanged {
                userViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        userViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }


        }

        login.setOnClickListener {
            userViewModel.userNameValid.observe(this, {
                login.isEnabled = it
                if (!it) {
                    username.error = getString(R.string.invalid_username)
                }
                else{
                    username.error = null
                }
            })

            userViewModel.passwordValid.observe(this, {
                login.isEnabled = it
                if (!it) {
                    password.error = getString(R.string.invalid_password)
                }
                else{
                    password.error = null
                }
            })
            //Log.d(tag_d, "username ${username.text.toString()} password ${password.text.toString()}")
            userViewModel.login(username.text.toString(), password.text.toString())
        }
        binding.textLoginToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginSuccess(message: String) {
        val welcome = getString(R.string.welcome)
        Toast.makeText(
            applicationContext,
            "$welcome $message",
            Toast.LENGTH_LONG
        ).show()
        startActivity(Intent(this, MainActivity::class.java))
    }

}


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}