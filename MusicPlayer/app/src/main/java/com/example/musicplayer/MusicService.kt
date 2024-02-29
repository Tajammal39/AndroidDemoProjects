package com.example.musicplayer

import androidx.core.app.NotificationCompat
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MusicService : Service() {
    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    // help in code again and again execute
    override fun onBind(intent: Intent?): IBinder {
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification(playPauseBtn: Int) {
        val preIntent =
            Intent(this, NotificationReceiver::class.java).setAction(MyNotification.PREVIOUS)
        val prevPendingIntent =
            PendingIntent.getBroadcast(baseContext, 1, preIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent =
            Intent(this, NotificationReceiver::class.java).setAction(MyNotification.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            2,
            nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val playIntent =
            Intent(this, NotificationReceiver::class.java).setAction(MyNotification.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            3,
            playIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val exitIntent =
            Intent(this, NotificationReceiver::class.java).setAction(MyNotification.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            4,
            exitIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val preAction =
            NotificationCompat.Action.Builder(R.drawable.ic_back, "previous", prevPendingIntent)
                .build()
        val playAction =
            NotificationCompat.Action.Builder(playPauseBtn, "play", playPendingIntent).build()
        val nextAction =
            NotificationCompat.Action.Builder(R.drawable.ic_next, "next", nextPendingIntent).build()
        val exitAction =
            NotificationCompat.Action.Builder(R.drawable.ic_exit, "exit", exitPendingIntent).build()

        val imgArt = getImageArt(PlayerActivity.MusicListPA[PlayerActivity.songPosition].path)

        val image = if (imgArt != null) {
            BitmapFactory.decodeByteArray(imgArt, 0, imgArt.size)
        } else {
            BitmapFactory.decodeResource(resources, R.drawable.musicplayer_icon_splash_screen)
        }

        val notification = NotificationCompat.Builder(applicationContext, "channel12")
            .setSmallIcon(R.drawable.musical_player)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(preAction)
            .addAction(playAction)
            .addAction(nextAction)
            .addAction(exitAction)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .setContentTitle(PlayerActivity.MusicListPA[PlayerActivity.songPosition].title)
            .setContentText(PlayerActivity.MusicListPA[PlayerActivity.songPosition].artist)
            .setLargeIcon(image)
            .build()

        startForeground(13, notification)

    }

}