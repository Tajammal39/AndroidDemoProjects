package com.example.musicplayer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.ActivityPlaylistDetailBinding

@Suppress("DEPRECATION")
class PlaylistDetail : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistDetailBinding
    lateinit var adpater: MusicAdapter

    companion object {
        var currentPlaylistPosition: Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentPlaylistPosition = intent.getIntExtra("index", -1)

//        currentPlaylistPosition = intent.extras?.get("index") as Int
        binding.playlistDetailRv.setItemViewCacheSize(10)
        binding.playlistDetailRv.setHasFixedSize(true)
        PlaylistActivity.musicPlaylist.ref[currentPlaylistPosition].playlist.addAll(MainActivity.MusicListMA)
        PlaylistActivity.musicPlaylist.ref[currentPlaylistPosition].playlist.shuffle()

        adpater = MusicAdapter(
            this,
            PlaylistActivity.musicPlaylist.ref[currentPlaylistPosition].playlist,
            playlistDetail = true
        )
        binding.playlistDetailRv.adapter = adpater

        binding.backBtnPd.setOnClickListener { finish() }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        binding.playListNamePd.text =
            PlaylistActivity.musicPlaylist.ref[currentPlaylistPosition].name
        binding.moreInfoPD.text = "Total ${adpater.itemCount} Songs.\n\n" +
                "Created On:\n${PlaylistActivity.musicPlaylist.ref[currentPlaylistPosition].createdOn}\n\n" +
                " -- ${PlaylistActivity.musicPlaylist.ref[currentPlaylistPosition].createdBy}"

        if (adpater.itemCount > 0) {
            Glide
                .with(this)
                .load(PlaylistActivity.musicPlaylist.ref[currentPlaylistPosition].playlist[0].artUrl)
                .centerCrop()
                .placeholder(R.drawable.musical_player).centerCrop()
                .into(binding.playlistImgPd)

            binding.shuffleBtnPd.visibility = View.VISIBLE
        }
    }
}