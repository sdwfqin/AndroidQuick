package io.github.sdwfqin.quickseed.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import io.github.sdwfqin.quickseed.R
import io.github.sdwfqin.quickseed.ui.main.MainActivity
import io.github.sdwfqin.samplecommonlibrary.utils.ScreenCaptureManager


/**
 * 截屏服务
 * <p>
 *
 * @author 张钦
 * @date 2021/11/30
 */
class ScreenRecorderService : Service() {

    private var mResultCode = -1
    private var mResultData: Intent? = null
    private lateinit var mProjectionManager: MediaProjectionManager
    private var mMediaProjection: MediaProjection? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createNotificationChannel();
        mResultCode = intent.getIntExtra("code", -1);
        mResultData = intent.getParcelableExtra("data");

        mProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mResultData?.let {
            mMediaProjection = mProjectionManager.getMediaProjection(mResultCode, it)
        }

        mMediaProjection?.let {
            ScreenCaptureManager.instance.init(it)
            createNotificationChannel()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        val builder = Notification.Builder(this.applicationContext) //获取一个Notification构造器

        val nfIntent = Intent(this, MainActivity::class.java) //点击后跳转的界面，可以设置跳转数据

        builder.setContentIntent(
            PendingIntent.getActivity(
                this,
                0,
                nfIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        ) // 设置PendingIntent
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.ic_launcher
                )
            ) // 设置下拉列表中的图标(大图标)
            //.setContentTitle("SMI InstantView") // 设置下拉列表里的标题
            .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
            .setContentText("屏幕捕获服务运行中。。。") // 设置上下文内容
            .setWhen(System.currentTimeMillis()) // 设置该通知发生的时间

        /*以下是对Android 8.0的适配*/
        //普通notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("screenshot")
        }
        //前台服务notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                "screenshot",
                "屏幕捕获服务",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification: Notification = builder.build() // 获取构建好的Notification

        startForeground(110, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }
}