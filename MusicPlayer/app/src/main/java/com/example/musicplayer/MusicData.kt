package com.example.musicplayer
import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.bumptech.glide.Glide
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess
data class MusicData(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val path: String,
    val duration: Long = 0,
    val artUrl: String
) : MediaPlayer.OnCompletionListener {
    override fun onCompletion(mp: MediaPlayer?) {
        setSongPosition(true)
        val currentMusic = PlayerActivity.MusicListPA[PlayerActivity.songPosition]
        createMediaPlayer(currentMusic)
        setLayout(PlayerActivity.binding.root.context)

    }
}

fun formatDuration(duration: Long): String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = TimeUnit.SECONDS.convert(
        duration,
        TimeUnit.MILLISECONDS
    ) - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
    return String.format("%02d:%02d", minutes, seconds)
}

fun getImageArt(path: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}

fun setLayout(content: Context) {
    PlayerActivity.fIndex = favouriteChecker(PlayerActivity.MusicListPA[PlayerActivity.songPosition].id)
    Glide
        .with(content)
        .load(PlayerActivity.MusicListPA[PlayerActivity.songPosition].artUrl)
        .centerCrop()
        .placeholder(R.drawable.musical_player).centerCrop()
        .into(PlayerActivity.binding.musicIcon)
    PlayerActivity.binding.songTitle.text =
        PlayerActivity.MusicListPA[PlayerActivity.songPosition].title

    if (PlayerActivity.isFavourite)
        PlayerActivity.binding.favIcon.setImageResource(R.drawable.ic_favorite)
    else PlayerActivity.binding.favIcon.setImageResource(R.drawable.ic_favorite_border)
}

fun createMediaPlayer(musicData: MusicData) {
    try {
        if (PlayerActivity.musicService!!.mediaPlayer == null) PlayerActivity.musicService!!.mediaPlayer =
            MediaPlayer()
        PlayerActivity.musicService!!.mediaPlayer?.reset()
        PlayerActivity.musicService!!.mediaPlayer?.setDataSource(PlayerActivity.MusicListPA[PlayerActivity.songPosition].path)
        PlayerActivity.musicService!!.mediaPlayer?.prepare()
        PlayerActivity.musicService!!.mediaPlayer?.start()

        PlayerActivity.isPlaying = true
        PlayerActivity.binding.playPauseBtn.setIconResource(R.drawable.ic_pause)
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
        PlayerActivity.binding.startTv.text =
            formatDuration(PlayerActivity.musicService!!.mediaPlayer!!.currentPosition.toLong())
        PlayerActivity.binding.endTv.text =
            formatDuration(PlayerActivity.musicService!!.mediaPlayer!!.duration.toLong())
        PlayerActivity.binding.seekBar.progress = 0
        PlayerActivity.binding.seekBar.max = PlayerActivity.musicService!!.mediaPlayer!!.duration
        PlayerActivity.musicService!!.mediaPlayer!!.setOnCompletionListener(musicData)

    } catch (e: Exception) {
        return
    }
}

fun playMusic() {
    PlayerActivity.binding.playPauseBtn.setIconResource(R.drawable.ic_pause)
    PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
    PlayerActivity.isPlaying = true
    PlayerActivity.musicService!!.mediaPlayer?.start()
    NowPlaying.binding.playPauseBtnNp.setIconResource(R.drawable.ic_pause)
}

fun pauseMusic() {
    PlayerActivity.binding.playPauseBtn.setIconResource(R.drawable.ic_play_arrow)
    PlayerActivity.musicService!!.showNotification(R.drawable.ic_play_arrow)
    PlayerActivity.isPlaying = false
    PlayerActivity.musicService!!.mediaPlayer?.pause()
    NowPlaying.binding.playPauseBtnNp.setIconResource(R.drawable.ic_play_arrow)
}

fun setSongPosition(increment: Boolean) {
    if (!PlayerActivity.repeat) {
        val lastIndex = PlayerActivity.MusicListPA.size - 1
        if (increment) {
            PlayerActivity.songPosition =
                if (PlayerActivity.songPosition == lastIndex) 0 else PlayerActivity.songPosition + 1
        } else {
            PlayerActivity.songPosition =
                if (PlayerActivity.songPosition == 0) lastIndex else PlayerActivity.songPosition - 1
        }
    }
}

fun exitApplication() {
    PlayerActivity.musicService!!.stopForeground(true)
    PlayerActivity.musicService!!.mediaPlayer!!.release()
    PlayerActivity.musicService = null
    exitProcess(1)
}
fun favouriteChecker(id: String): Int {
    PlayerActivity.isFavourite = false
    FavouriteActivity.favouriteSongs.forEachIndexed { index, musicData ->
        if (musicData.id == id) {
            PlayerActivity.isFavourite = true
            return index
        }
    }
    return -1
}