package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicplayer.databinding.ActivityFavouiteBinding

class FavouriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouiteBinding
    private lateinit var favAdapter: FavouriteAdapter

    companion object{
        var favouriteSongs:ArrayList<MusicData> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouiteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.favBackIcon.setOnClickListener {
            finish()
        }

        favAdapter = FavouriteAdapter(this, favouriteSongs)
        binding.favRv.adapter = favAdapter
    }
}