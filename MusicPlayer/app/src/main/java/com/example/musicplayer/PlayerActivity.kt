package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.audiofx.AudioEffect
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.musicplayer.databinding.ActivityPlayerBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.system.exitProcess

@Suppress("DEPRECATION")
class PlayerActivity : AppCompatActivity(), ServiceConnection {
    private lateinit var runnable: Runnable

    companion object {
        var isPlaying: Boolean = false
        var musicService: MusicService? = null
        var songPosition: Int = 0
        lateinit var MusicListPA: ArrayList<MusicData>

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityPlayerBinding
        var repeat: Boolean = false

        var min15:Boolean = false
        var min30:Boolean = false
        var min45:Boolean = false
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

        binding.icRepeat.setOnClickListener {
            if (!repeat) {
                repeat = true
                binding.icRepeat.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
            } else {
                repeat = false
                binding.icRepeat.setColorFilter(ContextCompat.getColor(this, R.color.cool_pink))
            }
        }

        binding.playerBackIcon.setOnClickListener { finish() }

        binding.icEqualizer.setOnClickListener {
            try {
                val eqIntent = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
                eqIntent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, musicService!!.mediaPlayer!!.audioSessionId)
                eqIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME,baseContext.packageName)
                eqIntent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE,AudioEffect.CONTENT_TYPE_MUSIC)
                startActivityForResult(eqIntent,13)
            }catch (e:Exception){
                Toast.makeText(this,"Equalizer Feature not Supported!!",Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.icTimer.setOnClickListener {
            var timer = min15 || min30 || min45
            if (!timer) {
                showBottomSheetDialog()
            }
            else{
                val builder = AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("Do You Want to close timer?")
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton("Yes") { _, _ ->
                   min30 = min15 == min45 == false
                    binding.icTimer.setColorFilter(ContextCompat.getColor(this,R.color.dark_red))
                }

                builder.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.show()
            }
        }

        binding.icShare.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "audio/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse(MusicListPA[songPosition].path))
            startActivity(Intent.createChooser(shareIntent,"Sharing Music File!!"))
        }

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

    private fun preNextSong(increment: Boolean) {
        setSongPosition(increment)
        setLayout(this)
        createMediaPlayer(MusicListPA[songPosition])
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        createMediaPlayer(MusicListPA[songPosition])
        seekBarSetup()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

    private fun seekBarSetup() {
        runnable = Runnable {
            musicService?.mediaPlayer?.let { mediaPlayer ->
                binding.startTv.text = formatDuration(mediaPlayer.currentPosition.toLong())
                binding.seekBar.progress = mediaPlayer.currentPosition
            }
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 13 || resultCode == RESULT_OK)
        {
            return
        }
    }

    private fun showBottomSheetDialog(){
    val dialog = BottomSheetDialog(this@PlayerActivity)
        dialog.setContentView(R.layout.bottom_sheet_dialog)
        dialog.show()

        dialog.findViewById<ConstraintLayout>(R.id.section1)!!.setOnClickListener {
            Toast.makeText(this,"Music will stop after 15 minutes",Toast.LENGTH_SHORT).show()
            binding.icTimer.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
            min15 = true

            Thread{Thread.sleep(15 * 60000)
            if (min15) exitApplication()}.start()
            dialog.dismiss()
        }

        dialog.findViewById<ConstraintLayout>(R.id.section2)!!.setOnClickListener {
            Toast.makeText(this,"Music will stop after 30 minutes",Toast.LENGTH_SHORT).show()
            binding.icTimer.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
            min30 = true

            Thread{Thread.sleep(30 * 60000)
                if (min30) exitApplication()}.start()
            dialog.dismiss()
        }

        dialog.findViewById<ConstraintLayout>(R.id.section3)!!.setOnClickListener {
            Toast.makeText(this,"Music will stop after 45 minutes",Toast.LENGTH_SHORT).show()
            binding.icTimer.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
            min45 = true

            Thread{Thread.sleep(45 * 60000)
                if (min45) exitApplication()}.start()
            dialog.dismiss()
        }
    }
}