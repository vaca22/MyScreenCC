package com.viatom.screencc

import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.viatom.screencc.databinding.ActivityMainBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var mScreenDensity = 0
    lateinit var binding: ActivityMainBinding

    companion object {
        private val REQUEST_MEDIA_PROJECTION = 1
        lateinit var mMediaProjectionManager: MediaProjectionManager
        lateinit var mMediaProjection: MediaProjection
        lateinit var mVirtualDisplay: VirtualDisplay
        var mResultCode = 0
        var mResultData: Intent? = null
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        mScreenDensity = metrics.densityDpi

        Log.e("fcuk",mScreenDensity.toString())

        mMediaProjectionManager =
            getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(
            mMediaProjectionManager.createScreenCaptureIntent(),
            REQUEST_MEDIA_PROJECTION
        )


    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            mResultCode = resultCode
            mResultData = data
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(this@MainActivity, RecordService::class.java))
                MainScope().launch {
                    while (mMediaProjection == null) {

                    }
                    mVirtualDisplay = mMediaProjection.createVirtualDisplay(
                        "ScreenCapture",
                        binding.fuck.getWidth(), binding.fuck.getHeight(), mScreenDensity,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        binding.fuck.holder.surface, null, null
                    )

                }
            }

        }
    }


}