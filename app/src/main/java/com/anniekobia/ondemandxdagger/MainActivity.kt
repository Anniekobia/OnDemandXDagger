package com.anniekobia.ondemandxdagger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

class MainActivity : AppCompatActivity() {

    private val manager: SplitInstallManager by lazy {
        SplitInstallManagerFactory.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpOnDemandModule()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        SplitCompat.installActivity(this)
    }

    private fun setUpOnDemandModule() {
        val downloadButton = findViewById<Button>(R.id.downloadBtn)
        val accessButton = findViewById<Button>(R.id.accessBtn)

        downloadButton.setOnClickListener {
            val request = SplitInstallRequest.newBuilder()
                .addModule("dynamicfeature")
                .build()

            val listener = SplitInstallStateUpdatedListener { state ->
                    when (state.status()) {
                        SplitInstallSessionStatus.DOWNLOADING -> {
                            val totalBytes = state.totalBytesToDownload()
                            val progress = state.bytesDownloaded()
                            Log.d("ONDEMAND: ","Downloading on demand module: %d of %d bytes - Progress: "+ progress.toString() +" Total Bytes: "+totalBytes)
                        }
                        SplitInstallSessionStatus.INSTALLED -> {
                            Log.d("ONDEMAND: ","Downloading on demand module: state INSTALLED")

//                            SplitCompat.install(this)

                            accessButton.isEnabled = true
                        }
                        SplitInstallSessionStatus.CANCELED -> {
                            Log.d("ONDEMAND: ","Downloading on demand module: state CANCELED")
                        }
                        SplitInstallSessionStatus.CANCELING -> {
                            Log.d("ONDEMAND: ","Downloading on demand module: state CANCELING")
                        }
                        SplitInstallSessionStatus.DOWNLOADED -> {
                            Log.d("ONDEMAND: ","Downloading on demand module: state DOWNLOADED")
                        }
                        SplitInstallSessionStatus.FAILED -> {
                            Log.d("ONDEMAND: ","Downloading on demand module: state FAILED")
                        }
                        SplitInstallSessionStatus.INSTALLING -> {
                            Log.d("ONDEMAND: ","Downloading on demand module: state INSTALLING")
                        }
                        SplitInstallSessionStatus.PENDING -> {
                            Log.d("ONDEMAND: ","Downloading on demand module: state PENDING")
                        }
                        SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                            Log.d("ONDEMAND: ","Downloading on demand module: state REQUIRES USER CONFIRMATION")
                        }
                        SplitInstallSessionStatus.UNKNOWN -> {
                            Log.d("ONDEMAND: ","Downloading on demand module: state UNKNOWN")
                        }
                    }
            }

            manager.registerListener(listener)
            manager.startInstall(request)
                .addOnSuccessListener {
                    Log.e("ONDEMAND: ", "Successfully installed ondemandfeature")

                   // accessButton.isEnabled = true
                }
                .addOnFailureListener { exception ->
                    Log.e("ONDEMAND: ", "Error installing ondemandfeature")
                }
        }

        accessButton.setOnClickListener {
            val intent = Intent()
            intent.setClassName(BuildConfig.APPLICATION_ID, "com.anniekobia.dynamicfeature.DynamicFeatureMainActivity")
            startActivity(intent)
        }

    }


}