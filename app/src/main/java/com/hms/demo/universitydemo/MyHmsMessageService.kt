package com.hms.demo.universitydemo

import android.util.Log
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage

class MyHmsMessageService : HmsMessageService() {

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        token?.let {
            Log.e("onToken",it)
        }

    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

    }
}