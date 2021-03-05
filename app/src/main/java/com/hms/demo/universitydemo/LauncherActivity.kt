package com.hms.demo.universitydemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.huawei.hmf.tasks.Task
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.hwid.result.AuthHuaweiId

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        val authParams = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setAuthorizationCode()
            .setAccessToken()
            .setIdToken()
            .setEmail()
            .setMobileNumber()
            .createParams()
        val service = AccountAuthManager.getService(this, authParams)
        val task = service.silentSignIn()
        task.addOnSuccessListener{
            Log.e("Account","Hello from silent sign in")
            Log.e("Account","Displayname: ${it.getDisplayName()} \t AccessToken: ${it.accessToken} \tID Token: ${it.idToken}")
            if(!it.email.isNullOrEmpty()){
                Log.e("Account","Email: ${it.email}")
            }

            startActivity(Intent(this@LauncherActivity,MainActivity::class.java))
        }

        task.addOnFailureListener{
            startActivity(Intent(this,AccountActivity::class.java))
        }

    }
}