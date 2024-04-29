package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.databinding.PlaylistViewBinding

class PlaylistAdapterView(private val context: Context, private var playListList: ArrayList<Playlist>) :
    RecyclerView.Adapter<PlaylistAdapterView.MyViewHolder>() {
    class MyViewHolder(binding: PlaylistViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.playlistImage
        val name = binding.playlistName
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            PlaylistViewBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return playListList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = playListList[position].name
        holder.name.isSelected = true

    }

    fun refreshPlaylist(){
        playListList = ArrayList()
        playListList.addAll(PlaylistActivity.musicPlaylist.ref)
        notifyDataSetChanged()
    }

}