package com.viatom.screencc

import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import com.viatom.screencc.MainActivity.Companion.mMediaProjection
import com.viatom.screencc.MainActivity.Companion.mMediaProjectionManager
import com.viatom.screencc.MainActivity.Companion.mResultCode
import com.viatom.screencc.MainActivity.Companion.mResultData

import android.os.Build

import android.R
import android.app.*

import android.graphics.BitmapFactory


class RecordService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("fuck","sdfsdf")

        // This initiates a prompt dialog for the user to confirm screen projection.

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
     /*   mMediaProjectionManager =
            getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager*/
        mMediaProjection=mMediaProjectionManager.getMediaProjection(mResultCode,mResultData!!)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        val builder: Notification.Builder = Notification.Builder(this.applicationContext) //获取一个Notification构造器
        val nfIntent = Intent(this, MainActivity::class.java) //点击后跳转的界面，可以设置跳转数据
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.sym_def_app_icon
                )
            ) // 设置下拉列表中的图标(大图标)
            //.setContentTitle("SMI InstantView") // 设置下拉列表里的标题
            .setSmallIcon(R.mipmap.sym_def_app_icon) // 设置状态栏内的小图标
            .setContentText("is running......") // 设置上下文内容
            .setWhen(System.currentTimeMillis()) // 设置该通知发生的时间

        /*以下是对Android 8.0的适配*/
        //普通notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("notification_id")
        }
        //前台服务notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                "notification_id",
                "notification_name",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notification: Notification = builder.build() // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND //设置为默认的声音
        startForeground(110, notification)
    }

}