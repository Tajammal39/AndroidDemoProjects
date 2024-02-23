package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import com.example.musicplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity(), ServiceConnection {
    private lateinit var runnable: Runnable

    companion object {
        var isPlaying: Boolean = false
        var musicService: MusicService? = null
        var songPosition: Int = 0
        lateinit var MusicListPA: ArrayList<MusicData>

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityPlayerBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)

        initializeLayout()
        binding.playPauseBtn.setOnClickListener {
            if (isPlaying) pauseMusic() else playMusic()
        }

        binding.nextIcon.setOnClickListener {
            preNextSong(true)
        }
        binding.previousIcon.setOnClickListener {
            preNextSong(false)
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) musicService!!.mediaPlayer!!.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
    }

    private fun initializeLayout() {
        songPosition = intent.getIntExtra("Index", 0)

        when (intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                MusicListPA = ArrayList(MainActivity.MusicListMA)
                setLayout(this)
            }

            "MainActivity" -> {
                MusicListPA = ArrayList(MainActivity.MusicListMA)
                //randomly play song in shuffle screen
                MusicListPA.shuffle()
                setLayout(this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaPlayer()
    }

    private fun releaseMediaPlayer() {
        musicService!!.mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        musicService!!.mediaPlayer = null
    }

    private fun preNextSong(increment: Boolean) {
        Log.d("preNextSong1", "$increment")
        setSongPosition(increment)
        setLayout(this)
        createMediaPlayer()
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        createMediaPlayer()
        seekBarSetup()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

    private fun seekBarSetup() {
        runnable = Runnable {
            binding.startTv.text =
                formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
            binding.seekBar.progress =
                musicService!!.mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }
}