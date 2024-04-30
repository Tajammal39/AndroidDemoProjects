package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog

import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.databinding.PlaylistViewBinding

class PlaylistAdapterView(private val context: Context, private var playListList: ArrayList<Playlist>) :
    RecyclerView.Adapter<PlaylistAdapterView.MyViewHolder>() {
    class MyViewHolder(binding: PlaylistViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.playlistImage
        val name = binding.playlistName
        val root = binding.root
        val delete = binding.playlistDeleteBtn
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
        holder.delete.setOnClickListener {

            val builder = AlertDialog.Builder(context)
                .setTitle(playListList[position].name)
                .setMessage("Do You Want to delete playlist?")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton("Yes") {dialog, _ ->
                PlaylistActivity.musicPlaylist.ref.removeAt(position)
                refreshPlaylist()
                dialog.dismiss()
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        holder.root.setOnClickListener {
            context.startActivity(Intent(context,PlaylistDetail::class.java)
                .putExtra("index",position))
        }

    }

    fun refreshPlaylist(){
        playListList = ArrayList()
        playListList.addAll(PlaylistActivity.musicPlaylist.ref)
        notifyDataSetChanged()
    }

}