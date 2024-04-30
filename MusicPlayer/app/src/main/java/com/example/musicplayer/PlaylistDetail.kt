package com.example.musicplayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicplayer.databinding.ActivityPlaylistDetailBinding

@Suppress("DEPRECATION")
class PlaylistDetail : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistDetailBinding

    companion object {
        var currentPlaylistPosition: Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        currentPlaylistPosition = intent.getIntExtra("index", 1)
        currentPlaylistPosition = intent.extras?.get("index") as Int
    }
}