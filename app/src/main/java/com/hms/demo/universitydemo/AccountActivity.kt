package com.hms.demo.universitydemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.hwid.ui.HuaweiIdAuthButton

class AccountActivity : AppCompatActivity() {

    companion object {
        const val TAG = "Account"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val hwid = findViewById<HuaweiIdAuthButton>(R.id.hwid)
        hwid.setOnClickListener {
            val authParams = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setAuthorizationCode()
                .setAccessToken()
                .setEmail()
                .setMobileNumber()
                .createParams()
            val service = AccountAuthManager.getService(this@AccountActivity, authParams)
            startActivityForResult(service.signInIntent, 8888)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 8888) {
            val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
            if (authAccountTask.isSuccessful) {
                // The sign-in is successful, and the user's ID information and authorization code are obtained.
                val authAccount = authAccountTask.result
                Log.i(TAG, "serverAuthCode:" + authAccount.authorizationCode)
                Log.e(
                    "Account",
                    "Displayname: ${authAccount.getDisplayName()} \t AccessToken: ${authAccount.accessToken} \tID Token: ${authAccount.idToken}"
                )
                if (!authAccount.email.isNullOrEmpty()) {
                    Log.e("Account", "Email: ${authAccount.email}")
                    startActivity(Intent(this@AccountActivity, MainActivity::class.java))
                } else {
                    // The sign-in failed.
                    Log.e(
                        TAG,
                        "sign in failed:" + (authAccountTask.exception as ApiException).statusCode
                    )
                }
            }
        }
    }
}