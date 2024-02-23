package com.example.musicplayer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MyNotification:Application() {
    companion object{
//        const val CHANNEL_ID = "channel11"
        const val PLAY = "play"
        const val NEXT = "next"
        const val PREVIOUS = "previous"
        const val EXIT = "exit"
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = getString(R.string.channelName)
            val descriptionText = getString(R.string.channelDescription)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel("channel12", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

    }

}