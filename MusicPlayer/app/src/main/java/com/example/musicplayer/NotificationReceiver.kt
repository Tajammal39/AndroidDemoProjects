package com.example.musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import kotlin.system.exitProcess

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            MyNotification.PREVIOUS -> preNextSong(false,context)
            MyNotification.PLAY -> if (PlayerActivity.isPlaying) pauseMusic() else playMusic()
            MyNotification.NEXT -> preNextSong(true,context)
            MyNotification.EXIT -> {
              exitApplication()
            }
        }

    }

    private fun preNextSong(increment: Boolean, context: Context) {
        val currentMusic = PlayerActivity.MusicListPA[PlayerActivity.songPosition]
        setSongPosition(increment)
        createMediaPlayer(currentMusic)
        Glide
            .with(context)
            .load(PlayerActivity.MusicListPA[PlayerActivity.songPosition].artUrl)
            .centerCrop()
            .placeholder(R.drawable.musical_player).centerCrop()
            .into(PlayerActivity.binding.musicIcon)

        PlayerActivity.binding.songTitle.text = PlayerActivity.MusicListPA[PlayerActivity.songPosition].title

    }
}