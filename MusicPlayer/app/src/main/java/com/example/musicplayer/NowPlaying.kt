package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.FragmentNowPlayingBinding

class NowPlaying : Fragment() {
    companion object {
        lateinit var binding: FragmentNowPlayingBinding
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_now_playing, container, false)
        binding = FragmentNowPlayingBinding.bind(view)
        binding.root.visibility = View.GONE

        binding.playPauseBtnNp.setOnClickListener {
            if (PlayerActivity.isPlaying) {
                pauseMusic()
            }
            else {
                playMusic()
            }
        }

        binding.nextBtnNp.setOnClickListener {
            setSongPosition(true)
            setLayout(requireContext())
            setLayouts()
            createMediaPlayer(PlayerActivity.MusicListPA[PlayerActivity.songPosition])
        }
        binding.root.setOnClickListener {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("Index", PlayerActivity.songPosition)
            intent.putExtra("class", "NowPlaying")
            ContextCompat.startActivity(requireContext(), intent, null)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        setLayouts()
    }

    private fun setLayouts() {
        if (PlayerActivity.musicService != null) {
            binding.root.visibility = View.VISIBLE
            binding.songNameNp.isSelected = true
            Glide
                .with(requireContext())
                .load(PlayerActivity.MusicListPA[PlayerActivity.songPosition].artUrl)
                .centerCrop()
                .placeholder(R.drawable.musical_player).centerCrop()
                .into(binding.songImgNp)
            binding.songNameNp.text = PlayerActivity.MusicListPA[PlayerActivity.songPosition].title

        }
    }

}