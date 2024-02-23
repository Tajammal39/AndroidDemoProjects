package com.example.musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bumptech.glide.Glide
import kotlin.system.exitProcess

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            MyNotification.PREVIOUS -> preNextSong(false,context)
            MyNotification.PLAY -> if (PlayerActivity.isPlaying) pauseMusic() else playMusic()
            MyNotification.NEXT -> preNextSong(true,context)
            MyNotification.EXIT -> {
                PlayerActivity.musicService!!.stopForeground(true)
                PlayerActivity.musicService = null
                exitProcess(1)
            }
        }

    }

    private fun preNextSong(increment: Boolean, context: Context) {
        setSongPosition(increment)
        createMediaPlayer()
        Glide
            .with(context)
            .load(PlayerActivity.MusicListPA[PlayerActivity.songPosition].artUrl)
            .centerCrop()
            .placeholder(R.drawable.musical_player).centerCrop()
            .into(PlayerActivity.binding.musicIcon)

        PlayerActivity.binding.songTitle.text = PlayerActivity.MusicListPA[PlayerActivity.songPosition].title

    }


   /* private fun playMusic(){
        PlayerActivity.isPlaying = true
        PlayerActivity.musicService!!.mediaPlayer?.start()
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
        PlayerActivity.binding.playPauseBtn.setIconResource(R.drawable.ic_pause)
    }
    private fun pauseMusic(){
        PlayerActivity.isPlaying = false
        PlayerActivity.musicService!!.mediaPlayer?.pause()
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_play_arrow)
        PlayerActivity.binding.playPauseBtn.setIconResource(R.drawable.ic_play_arrow)
    }
*/
}