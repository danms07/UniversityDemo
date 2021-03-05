package com.hms.demo.universitydemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.banner.BannerView
import com.huawei.hms.mlsdk.asr.MLAsrConstants
import com.huawei.hms.mlsdk.asr.MLAsrListener
import com.huawei.hms.mlsdk.asr.MLAsrRecognizer
import com.huawei.hms.mlsdk.common.MLApplication


class MainActivity : AppCompatActivity(), MLAsrListener {

    companion object{
        const val RECORD_CODE=100
    }

    var mSpeechRecognizer:MLAsrRecognizer?=null
    lateinit var recordBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MLApplication.getInstance().apiKey = "CgB6e3x9i51xAxkCW2Yl6h+YuXxNnmWwFrPU4UZN4xHff5VRC7Ytq/zYfw6dReGR5lS8vVuEBJXIMzgCenOjd6M4"
        val extras=intent.extras
        extras?.let{
            for(key in it.keySet()){
                Log.e("Parametro", "clave:$key  valor:${it.get(key)}")
            }
        }
        val bannerAd:BannerView=findViewById(R.id.hw_banner_view)
        bannerAd.adListener=this.adListener;
        bannerAd.loadAd(AdParam.Builder().build())

        recordBtn=findViewById<Button>(R.id.recordBtn).apply {
            setOnClickListener{
                initASR()
            }
        }

    }

    private fun initASR() {
        if(checkMicPermission()){
            startRecording()
        }else requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_CODE)
    }

    private fun startRecording() {
        recordBtn.setOnClickListener(null);
        recordBtn.text = "Grabando"
         mSpeechRecognizer= MLAsrRecognizer.createAsrRecognizer(this)
        mSpeechRecognizer?.let{
            it.setAsrListener(this)
            val mSpeechRecognizerIntent = Intent(MLAsrConstants.ACTION_HMS_ASR_SPEECH)
            mSpeechRecognizerIntent // Set the language that can be recognized to English. If this parameter is not set, English is recognized by default. Example: "zh-CN": Chinese; "en-US": English; "fr-FR": French; "es-ES": Spanish; "de-DE": German; "it-IT": Italian
                .putExtra(
                    MLAsrConstants.LANGUAGE,
                    "es-ES"
                ) .putExtra(
                    MLAsrConstants.FEATURE,
                    MLAsrConstants.FEATURE_WORDFLUX
                )
            it.startRecognizing(mSpeechRecognizerIntent)
        }
    }

    private fun releaseResources(){
        recordBtn.text="Manten pulsado para grabar"
        recordBtn.setOnLongClickListener{initASR(); true}
        mSpeechRecognizer?.apply{
            destroy()
        }
        mSpeechRecognizer=null
    }

    private fun checkMicPermission(): Boolean {
        val mic=ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        return mic==PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(checkMicPermission()) startRecording()
        else {//Desplegar mensaje de permisos
        }
    }

    private val adListener:AdListener=object: AdListener() {
        override fun onAdFailed(code: Int) {
            super.onAdFailed(code)
            Log.e("Ads", "Ad failed with error code: $code")
            when(code){
                3 -> {
                    val root = findViewById<ConstraintLayout>(R.id.root)
                    val bannerView: BannerView = findViewById(R.id.hw_banner_view)
                    root.removeView(bannerView)
                    bannerView.destroy()
                }
            }
        }

        override fun onAdLoaded() {
            super.onAdLoaded()
            Log.e("Ads", "Ad Loaded")
        }
    }

    override fun onResults(p0: Bundle?) {
        releaseResources()
    }

    override fun onRecognizingResults(results: Bundle?) {
        val resultTV=findViewById<TextView>(R.id.result)
        resultTV.text = ""
        results?.let {
            for(key:String in it.keySet()){
                resultTV.append(it.getString(key,""))
            }
        }
    }

    override fun onError(p0: Int, p1: String?) {
        p1?.let{
            Log.e("ASR",it)
        }
    }

    override fun onStartListening() {
        Toast.makeText(this,"Habla ahora",Toast.LENGTH_SHORT).show()
    }

    override fun onStartingOfSpeech() {
    }

    override fun onVoiceDataReceived(p0: ByteArray?, p1: Float, p2: Bundle?) {

    }

    override fun onState(p0: Int, p1: Bundle?) {

    }
}