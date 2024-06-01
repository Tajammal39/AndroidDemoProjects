package com.example.musicplayer

import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.musicplayer.databinding.ActivityPlaylistBinding
import com.example.musicplayer.databinding.AddPlaylistDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var playlistAdapterView: PlaylistAdapterView

    companion object {
        var musicPlaylist: MusicPlayerList = MusicPlayerList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playListBackIcon.setOnClickListener {
            finish()
        }

        playlistAdapterView = PlaylistAdapterView(this, musicPlaylist.ref)
        binding.playlistRv.adapter = playlistAdapterView
        binding.addPlaylist.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.add_playlist_dialog, null)
        val binder = AddPlaylistDialogBinding.bind(dialogView)
        builder.setView(dialogView)
            .setTitle("Playlist Details")
            .setPositiveButton("ADD") { dialog, _ ->
                val playlistName = binder.playlistName.text.toString().trim()
                val createdBy = binder.playlistName.text.toString().trim()

                if (playlistName != null && createdBy != null) {
                    addPlayList(playlistName, createdBy)
                }
                dialog.dismiss()
            }.show()
    }

    private fun addPlayList(playlistName: String, createdBy: String) {
        var playListExists = false

        for (i in musicPlaylist.ref){
            if(playlistName == i.name){
                playListExists = true
                break
            }
        }

        if(playListExists){
            Toast.makeText(this,"Playlist Exist!!",Toast.LENGTH_SHORT).show()
        }
        else{
            //create  class object
            val tempPlaylist = Playlist()
            tempPlaylist.name = playlistName
            tempPlaylist.createdBy = createdBy
            tempPlaylist.playlist = ArrayList()
            val calender = Calendar.getInstance().time
            tempPlaylist.createdOn = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(calender)
            musicPlaylist.ref.add(tempPlaylist)
            playlistAdapterView.refreshPlaylist()
        }
    }
}